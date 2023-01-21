package com.vvw.productservice.api.exceptions;

import java.io.Serial;

public class ProductNotUniqueException extends ProductException{

	@Serial
	private static final long serialVersionUID = -4693649580979398126L;

	public ProductNotUniqueException(String productId, String title) {
		super("Product with id: %s or title %s already exists".formatted(productId, title), ProductBusinessErrorCode.ALREADY_EXISTS);
	}
}
