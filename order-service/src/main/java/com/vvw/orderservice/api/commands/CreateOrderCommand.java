package com.vvw.orderservice.api.commands;

import com.vvw.orderservice.api.enums.OrderStatus;
import lombok.Builder;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.time.ZonedDateTime;

@Builder
public record CreateOrderCommand(
		@TargetAggregateIdentifier
		String orderId,
		String userId,
		String addressId,
		String productId,
		OrderStatus orderStatus,
		ZonedDateTime createdAt,
		int quantity) {
}