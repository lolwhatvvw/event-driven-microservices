package com.vvw.orderservice.api.exceptions;

public class UserNotFoundException extends FetchUserException {

	public UserNotFoundException(String userid) {
		super("Could not find user with id %s".formatted(userid));
	}

}
