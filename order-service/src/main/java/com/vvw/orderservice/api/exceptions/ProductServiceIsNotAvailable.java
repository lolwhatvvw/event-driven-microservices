package com.vvw.orderservice.api.exceptions;

import java.io.Serial;

public class ProductServiceIsNotAvailable extends ProductReservationException{

	@Serial
	private static final long serialVersionUID = -3298149496542002576L;

	public ProductServiceIsNotAvailable() {
		this("Product service is not available");
	}
	public ProductServiceIsNotAvailable(String message) {
		super(message);
	}
	public ProductServiceIsNotAvailable(Throwable t) {
		super("Product service is not available", t);
	}
}
