package com.vvw.orderservice.api.events;

import com.vvw.orderservice.api.enums.OrderStatus;

public record OrderApprovedEvent (
		String orderId,
		OrderStatus orderStatus) {

	public OrderApprovedEvent(String orderId) {
		this(orderId, OrderStatus.APPROVED);
	}
}