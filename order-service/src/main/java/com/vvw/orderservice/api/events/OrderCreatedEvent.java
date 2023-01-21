package com.vvw.orderservice.api.events;

import com.vvw.orderservice.api.enums.OrderStatus;
import lombok.Builder;

import java.time.ZonedDateTime;

@Builder
public record OrderCreatedEvent(
		String orderId,
		String userId,
		String addressId,
		String productId,
		OrderStatus orderStatus,
		ZonedDateTime createdAt,
		int quantity) {

	public OrderCreatedEvent(String orderId,
	                         String userId,
	                         String addressId,
	                         String productId,
	                         ZonedDateTime createdAt,
	                         int quantity) {
		this(orderId, userId, addressId, productId, OrderStatus.CREATED, createdAt, quantity);
	}

}