package com.vvw.orderservice.api.exceptions;

import java.io.Serial;

public class ProductReservationException extends RuntimeException{

	@Serial
	private static final long serialVersionUID = 7475597703243372547L;

	public ProductReservationException(String message) {
		super(message);
	}

	public ProductReservationException(String message, Throwable cause) {
		super(message, cause);
	}

}
