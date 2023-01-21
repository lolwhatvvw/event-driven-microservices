package com.vvw.orderservice.api.exceptions;

import org.axonframework.commandhandling.CommandExecutionException;

import java.io.Serial;

public class OrderCommandExecutionException extends CommandExecutionException {

	@Serial
	private static final long serialVersionUID = -4690385901363861304L;

	public OrderCommandExecutionException(String message, Throwable cause, Object details) {
		super(message, cause, details);
	}
}
