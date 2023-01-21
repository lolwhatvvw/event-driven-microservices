package com.vvw.orderservice.api.events;

import com.vvw.orderservice.api.enums.OrderStatus;

public record OrderRejectedEvent (
		String orderId,
		String reason,
		OrderStatus orderStatus) {

	public OrderRejectedEvent(String orderId, String reason) {
		this(orderId, reason, OrderStatus.REJECTED);
	}
}