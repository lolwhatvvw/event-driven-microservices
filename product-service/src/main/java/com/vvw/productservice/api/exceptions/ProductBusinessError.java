package com.vvw.productservice.api.exceptions;

public record ProductBusinessError (
		String name,
		String message,
		ProductBusinessErrorCode code) {
}