package com.vvw.productservice.api.exceptions;

import java.io.Serial;

public class InsufficientStock extends ProductException {

	@Serial
	private static final long serialVersionUID = 5725821875303406232L;

	public InsufficientStock(String productId) {
		super("Insufficient items in stock for product with id: %s".formatted(productId), ProductBusinessErrorCode.INSUFFICIENT_STOCK);
	}
}
