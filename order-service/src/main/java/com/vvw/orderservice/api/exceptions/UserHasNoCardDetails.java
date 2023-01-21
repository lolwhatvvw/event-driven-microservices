package com.vvw.orderservice.api.exceptions;

import java.io.Serial;

public class UserHasNoCardDetails extends FetchUserException {

	@Serial
	private static final long serialVersionUID = 1435690828644542808L;

	public UserHasNoCardDetails(String userid) {
		super("User with id %s has no card details".formatted(userid));
	}

}