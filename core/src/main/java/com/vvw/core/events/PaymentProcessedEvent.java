package com.vvw.core.events;

public record PaymentProcessedEvent(
		String paymentId,
		String orderId) {
}
