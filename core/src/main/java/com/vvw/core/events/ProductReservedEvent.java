package com.vvw.core.events;

public record ProductReservedEvent(
		String productId,
		int quantity,
		String userId,
		String orderId) {
}
