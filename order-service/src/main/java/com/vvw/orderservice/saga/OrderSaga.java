package com.vvw.orderservice.saga;

import com.vvw.core.commands.ProcessPaymentCommand;
import com.vvw.core.commands.ReserveProductCommand;
import com.vvw.core.commands.compensating.CancelProductReservationCommand;
import com.vvw.core.dto.User;
import com.vvw.core.events.PaymentProcessedEvent;
import com.vvw.core.events.ProductReservedEvent;
import com.vvw.core.events.compensating.ProductReservationCancelledEvent;
import com.vvw.core.queries.FetchUserPaymentDetailsQuery;
import com.vvw.orderservice.api.commands.ApproveOrderCommand;
import com.vvw.orderservice.api.commands.RejectOrderCommand;
import com.vvw.orderservice.api.events.OrderApprovedEvent;
import com.vvw.orderservice.api.events.OrderCreatedEvent;
import com.vvw.orderservice.api.events.OrderRejectedEvent;
import com.vvw.orderservice.api.exceptions.*;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.NoHandlerForCommandException;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.deadline.DeadlineManager;
import org.axonframework.deadline.annotation.DeadlineHandler;
import org.axonframework.extensions.reactor.commandhandling.gateway.ReactorCommandGateway;
import org.axonframework.extensions.reactor.queryhandling.gateway.ReactorQueryGateway;
import org.axonframework.modelling.saga.EndSaga;
import org.axonframework.modelling.saga.SagaEventHandler;
import org.axonframework.modelling.saga.SagaLifecycle;
import org.axonframework.modelling.saga.StartSaga;
import org.axonframework.queryhandling.NoHandlerForQueryException;
import org.axonframework.spring.stereotype.Saga;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Function;

@Slf4j
@ProcessingGroup("order-group")
@Saga
public class OrderSaga {

	private final static String PAYMENT_PROCESSING_DEADLINE = "payment-processing-deadline";
	private final static String PRODUCT_RESERVATION_DEADLINE = "product-reservation-deadline";
	private final static Duration DURATION_OF_10_SECONDS = Duration.of(10, ChronoUnit.SECONDS);

	@Autowired
	private transient ReactorCommandGateway reactorCommandGateway;

	@Autowired
	private transient ReactorQueryGateway reactorQueryGateway;

	@Autowired
	private transient DeadlineManager deadlineManager;

	private String scheduleId;


	@StartSaga
	@SagaEventHandler(associationProperty = "orderId")
	private void handle(OrderCreatedEvent event) {

		log.info("OrderCreatedEvent in Saga for orderId: {}", event.orderId());

		var reserveProductCommand = reserveProductCommand(event);

		scheduleId = deadlineManager.schedule(Duration.of(5, ChronoUnit.SECONDS),
				PRODUCT_RESERVATION_DEADLINE,
				event);

		reactorCommandGateway.send(reserveProductCommand)
				.onErrorResume(ex ->
						reactorCommandGateway.send(
								new RejectOrderCommand(reserveProductCommand.orderId(), ex.getMessage())
						))
				.subscribe();
	}

	private User logUser(User it) {
		log.info("user is {}", it);
		return it;
	}

	@SagaEventHandler(associationProperty = "orderId")
	public void handle(ProductReservedEvent event) {

		log.info("PROCESSING USER PAYMENT EVENT: {}", event);

		var query = new FetchUserPaymentDetailsQuery(event.userId());

		reactorQueryGateway.query(query, User.class)
				.filter(Objects::nonNull)
				.switchIfEmpty(Mono.error(new UserNotFoundException(query.userId())))
				.map(this::logUser)
				.filter(this::cardDetailsNotNull)
				.switchIfEmpty(Mono.error(new UserHasNoCardDetails(query.userId())))
				.onErrorResume(ex ->
								ex instanceof NoHandlerForQueryException
								|| ex instanceof FetchUserException,
						rejectOrder(event)
				)
				.flatMap(sendProcessPaymentCommand(event))
				.subscribe();
	}

	@SagaEventHandler(associationProperty = "orderId")
	public void handle(PaymentProcessedEvent event) {
		cancelDeadline();
		reactorCommandGateway.send(new ApproveOrderCommand(event.orderId())).subscribe();
	}

	// End of happy path
	@EndSaga
	@SagaEventHandler(associationProperty = "orderId")
	public void handle(OrderApprovedEvent event) {
		log.info("Successfully approved order with id: {}", event.orderId());
		SagaLifecycle.end();
	}

	@SagaEventHandler(associationProperty = "orderId")
	public void handle(ProductReservationCancelledEvent event) {
		reactorCommandGateway.send(new RejectOrderCommand(event.orderId(), event.reason())).subscribe();
	}

	// End of compensating path
	@EndSaga
	@SagaEventHandler(associationProperty = "orderId")
	public void handle(OrderRejectedEvent event) {
		log.info("Successfully rejected order with id: {}", event.orderId());
		SagaLifecycle.end();
	}

	@DeadlineHandler(deadlineName = PRODUCT_RESERVATION_DEADLINE)
	public void handleProductReservationDeadline(OrderCreatedEvent event) {
		log.info("Product service deadline took place. Sending a compensating command");
		var cancelProductReservationCommand = cancelProductReservationCommand(event, "Product service timed out");
		cancelProductReservation(cancelProductReservationCommand).subscribe();
	}

	@DeadlineHandler(deadlineName = PAYMENT_PROCESSING_DEADLINE)
	public void handlePaymentDeadline(ProductReservedEvent event) {
		log.info("Payment processing deadline took place. Sending a compensating command");
		var cancelProductReservationCommand = cancelProductReservationCommand(event, "Payment timed out");
		cancelProductReservation(cancelProductReservationCommand).subscribe();
	}

	private Mono<User> cancelProductReservation(CancelProductReservationCommand command) {
		cancelDeadline();
		log.info(command.reason());
		return reactorCommandGateway.send(command);
	}

	private void cancelDeadline() {
		if (scheduleId != null) {
			deadlineManager.cancelSchedule(PAYMENT_PROCESSING_DEADLINE, scheduleId);
			deadlineManager.cancelSchedule(PRODUCT_RESERVATION_DEADLINE, scheduleId);
			scheduleId = null;
		}
	}

	private Function<? super Throwable, Mono<? extends User>> rejectOrder(ProductReservedEvent event) {
		return ex -> cancelProductReservation(cancelProductReservationCommand(event, ex.getMessage()));
	}

	private boolean cardDetailsNotNull(User u) {
		return u.cardDetails() != null;
	}

	private Function<User, Mono<?>> sendProcessPaymentCommand(ProductReservedEvent event) {
		scheduleId = deadlineManager.schedule(DURATION_OF_10_SECONDS, PAYMENT_PROCESSING_DEADLINE, event);
		return u -> {
			var command = processPaymentCommand(event, u);
			return reactorCommandGateway.send(command)
					.filter(Objects::nonNull)
					.switchIfEmpty(Mono.error(new PaymentNotFound(command.paymentId())))
					.onErrorResume(ex ->
									ex instanceof NoHandlerForCommandException
									|| ex instanceof PaymentProcessingTimeoutException
									|| ex instanceof PaymentNotFound,
							rejectOrder(event));
		};
	}

	private static ReserveProductCommand reserveProductCommand(OrderCreatedEvent event) {
		return ReserveProductCommand.builder()
				.productId(event.productId())
				.orderId(event.orderId())
				.userId(event.userId())
				.quantity(event.quantity())
				.build();
	}

	private static ProcessPaymentCommand processPaymentCommand(ProductReservedEvent event, User u) {
		return ProcessPaymentCommand.builder()
				.paymentId(UUID.randomUUID().toString())
				.cardDetails(u.cardDetails())
				.orderId(event.orderId())
				.build();
	}

	private static CancelProductReservationCommand cancelProductReservationCommand(OrderCreatedEvent event,
	                                                                               String reason) {
		return CancelProductReservationCommand.builder()
				.productId(event.productId())
				.orderId(event.orderId())
				.userId(event.userId())
				.quantity(event.quantity())
				.reason(reason)
				.build();
	}

	private static CancelProductReservationCommand cancelProductReservationCommand(ProductReservedEvent event,
	                                                                               String reason) {
		return CancelProductReservationCommand.builder()
				.productId(event.productId())
				.orderId(event.orderId())
				.userId(event.userId())
				.quantity(event.quantity())
				.reason(reason)
				.build();
	}
}
