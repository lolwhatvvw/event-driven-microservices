package com.vvw.orderservice.api.exceptions;

import org.axonframework.queryhandling.QueryExecutionException;

import java.io.Serial;

public class OrderQueryExecutionException extends QueryExecutionException {

	@Serial
	private static final long serialVersionUID = -7708604046782526117L;

	public OrderQueryExecutionException(String message, Throwable cause, Object details) {
		super(message, cause, details);
	}
}
