package com.vvw.productservice.api.exceptions;

import java.io.Serial;

public class ProductException extends RuntimeException {

	@Serial
	private static final long serialVersionUID = -4124293249691382624L;

	private final String errorMessage;
	private final ProductBusinessErrorCode errorCode;

	public ProductException(String errorMessage, ProductBusinessErrorCode errorCode) {
		super(errorMessage);
		this.errorMessage = errorMessage;
		this.errorCode = errorCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public ProductBusinessErrorCode getErrorCode() {
		return errorCode;
	}
}





