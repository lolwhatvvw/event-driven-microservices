package com.vvw.orderservice.api.exceptions;

import java.io.Serial;

public class FetchUserException extends RuntimeException{

	@Serial
	private static final long serialVersionUID = -8357368956965198250L;

	public FetchUserException(String message) {
		super(message);
	}

	public FetchUserException(String message, Throwable cause) {
		super(message, cause);
	}

}