package com.vvw.productservice.command.aggregate;

import com.vvw.core.commands.ReserveProductCommand;
import com.vvw.core.commands.compensating.CancelProductReservationCommand;
import com.vvw.core.events.ProductReservedEvent;
import com.vvw.core.events.compensating.ProductReservationCancelledEvent;
import com.vvw.productservice.api.commands.CreateProductCommand;
import com.vvw.productservice.api.events.ProductCreatedEvent;
import com.vvw.productservice.api.exceptions.*;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.messaging.interceptors.ExceptionHandler;
import org.axonframework.modelling.command.AggregateCreationPolicy;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.CreationPolicy;
import org.axonframework.spring.stereotype.Aggregate;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

import static org.axonframework.modelling.command.AggregateLifecycle.apply;
import static org.springframework.util.StringUtils.hasText;

@Slf4j
@ToString
@Aggregate(snapshotTriggerDefinition = "productSnapshot")
public class Product {

	@AggregateIdentifier
	private String productId;

	private String title;
	private BigDecimal price;
	private ZonedDateTime createdAt;
	private int quantity;


	@CommandHandler
	@CreationPolicy(AggregateCreationPolicy.ALWAYS)
	public void handle(CreateProductCommand command) {
		validateCreateProductCommand(command);
		apply(new ProductCreatedEvent(
				command.productId(),
				command.createdAt(),
				command.title(),
				command.price(),
				command.quantity())
		);
	}

	@CommandHandler
	public void handle(ReserveProductCommand command) {
		validateReserveProductCommand(command);
		apply(new ProductReservedEvent(
				command.productId(),
				command.quantity(),
				command.userId(),
				command.orderId())
		);
	}

	@CommandHandler
	public void handle(CancelProductReservationCommand command) {
		apply(new ProductReservationCancelledEvent(
				command.productId(),
				command.userId(),
				command.orderId(),
				command.reason(),
				command.quantity())
		);
	}

	@EventSourcingHandler
	public void handle(ProductCreatedEvent event) {
		title = event.title();
		price = event.price();
		quantity = event.quantity();
		createdAt = event.createdAt();
		productId = event.productId();
	}

	@EventSourcingHandler
	public void on(ProductReservedEvent event) {
		quantity -= event.quantity();
	}

	@EventSourcingHandler
	public void on(ProductReservationCancelledEvent event) {
		quantity += event.quantity();
	}

	@ExceptionHandler(resultType = ProductException.class)
	public void handle(ProductException exception) throws ProductCommandExecutionException {
		log.error("Exception occurred: {}", exception.getClass().getName());
		throw new ProductCommandExecutionException(exception.getErrorMessage(), exception, exception.getErrorCode());
	}


	public Product() {
		// Required by Axon for Event Sourcing
	}

	private void validateCreateProductCommand(CreateProductCommand command) {
		if (command.price().compareTo(BigDecimal.ZERO) <= 0) {
			throw new NegativeOrZeroPrice();
		}
		if (!hasText(command.title())) {
			throw new EmptyTitle();
		}
	}

	private void validateReserveProductCommand(ReserveProductCommand command) {
		if (quantity < command.quantity()) {
			throw new InsufficientStock(command.productId());
		}
	}
}