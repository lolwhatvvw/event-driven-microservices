package com.vvw.core.events.compensating;

public record ProductReservationCancelledEvent (
		String productId,
		String userId,
		String orderId,
		String reason,
		int quantity) {
}