package com.vvw.productservice.api.exceptions;

import java.io.Serial;

public class ProductNotFound extends ProductException{

	@Serial
	private static final long serialVersionUID = 7948253532607767316L;

	public ProductNotFound(String errorMessage) {
		super(errorMessage, ProductBusinessErrorCode.NOT_FOUND);
	}
}