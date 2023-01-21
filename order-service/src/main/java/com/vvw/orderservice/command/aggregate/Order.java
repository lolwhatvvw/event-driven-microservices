package com.vvw.orderservice.command.aggregate;

import com.vvw.orderservice.api.commands.ApproveOrderCommand;
import com.vvw.orderservice.api.commands.CreateOrderCommand;
import com.vvw.orderservice.api.commands.RejectOrderCommand;
import com.vvw.orderservice.api.enums.OrderStatus;
import com.vvw.orderservice.api.events.OrderApprovedEvent;
import com.vvw.orderservice.api.events.OrderCreatedEvent;
import com.vvw.orderservice.api.events.OrderRejectedEvent;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.spring.stereotype.Aggregate;

import static org.axonframework.modelling.command.AggregateLifecycle.apply;

@Slf4j
@Aggregate
@ToString
public class Order {

	@AggregateIdentifier
	private String orderId;

	private String userId;
	private String addressId;
	private String productId;
	private OrderStatus orderStatus;
	private int quantity;


	@CommandHandler
	public Order(CreateOrderCommand command) {
		apply(new OrderCreatedEvent(
				command.orderId(),
				command.userId(),
				command.addressId(),
				command.productId(),
				command.orderStatus(),
				command.createdAt(),
				command.quantity())
		);
	}

	@CommandHandler
	public void handle(ApproveOrderCommand command) {
		apply(new OrderApprovedEvent(command.orderId()));
	}

	@CommandHandler
	public void handle(RejectOrderCommand command) {
		apply(new OrderRejectedEvent(
				command.orderId(),
				command.reason())
		);
	}

	@EventSourcingHandler
	public void on(OrderCreatedEvent event) {
		userId = event.userId();
		addressId = event.addressId();
		productId = event.productId();
		orderStatus = event.orderStatus();
		quantity = event.quantity();
		orderId = event.orderId();
	}

	@EventSourcingHandler
	public void on(OrderApprovedEvent event) {
		this.orderStatus = event.orderStatus();
	}

	@EventSourcingHandler
	public void on(OrderRejectedEvent event) {
		this.orderStatus = event.orderStatus();
	}

	public Order() {

	}
}
