package com.vvw.productservice.api.exceptions;

import java.io.Serial;

public class EmptyTitle extends ProductException {

	@Serial
	private static final long serialVersionUID = 5725821875303406232L;

	public EmptyTitle() {
		super("Title cannot be empty", ProductBusinessErrorCode.EMPTY_TITLE);
	}
}
