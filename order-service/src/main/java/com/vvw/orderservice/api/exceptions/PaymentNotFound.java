package com.vvw.orderservice.api.exceptions;

import java.io.Serial;

public class PaymentNotFound extends ProductReservationException{

	@Serial
	private static final long serialVersionUID = 6065852628782899477L;

	public PaymentNotFound(String paymentId) {
		super("Payment with paymentId: %s is not found".formatted(paymentId));
	}
}
