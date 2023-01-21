package com.vvw.orderservice.api.exceptions;


import java.io.Serial;
import java.util.concurrent.TimeoutException;

public class PaymentProcessingTimeoutException extends TimeoutException {

	@Serial
	private static final long serialVersionUID = -3690874092913597083L;

	public PaymentProcessingTimeoutException(String paymentId) {
		super("Processing payment with paymentId: %s is timed out".formatted(paymentId));
	}
}
