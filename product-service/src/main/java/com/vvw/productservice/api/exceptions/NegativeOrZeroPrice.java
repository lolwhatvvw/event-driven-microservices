package com.vvw.productservice.api.exceptions;

import java.io.Serial;

public class NegativeOrZeroPrice extends ProductException {

	@Serial
	private static final long serialVersionUID = 179725125866572249L;

	public NegativeOrZeroPrice() {
		super("The price should have no additional text and cannot be less or equals to zero", ProductBusinessErrorCode.INVALID_PRICE);
	}
}