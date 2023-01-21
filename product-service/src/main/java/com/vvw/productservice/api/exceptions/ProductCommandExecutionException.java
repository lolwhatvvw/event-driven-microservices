package com.vvw.productservice.api.exceptions;

import org.axonframework.commandhandling.CommandExecutionException;

import java.io.Serial;

public class ProductCommandExecutionException extends CommandExecutionException {

	@Serial
	private static final long serialVersionUID = -4124293249691382624L;


	public ProductCommandExecutionException(String message, Throwable cause, Object details) {
		super(message, cause, details);
	}
}





