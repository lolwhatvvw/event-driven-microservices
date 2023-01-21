package com.vvw.productservice.util;

import com.vvw.productservice.api.exceptions.*;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandExecutionException;
import org.axonframework.queryhandling.QueryExecutionException;

import java.util.Optional;

@Slf4j
public abstract class ExceptionMapper {

	private ExceptionMapper() {
		// Utility class
	}

	public static Throwable mapRemoteException(Throwable exception) {
		ProductBusinessError productBusinessError;
		if (exception instanceof CommandExecutionException ex) {
			Optional<Object> details = ex.getDetails();
			if (details.isPresent()) {
				productBusinessError = new ProductBusinessError(
						ex.getClass().getName(), ex.getMessage(), (ProductBusinessErrorCode) details.get());
				return new ProductCommandExecutionException("An exception has occurred during command execution", ex, productBusinessError);
			}
		} else if (exception instanceof ProductNotUniqueException ex) {
			productBusinessError = new ProductBusinessError(
					ex.getClass().getName(), ex.getMessage(), ex.getErrorCode());
			return new ProductCommandExecutionException("An exception has occurred during Product set based validation", ex, productBusinessError);
		} else if ((exception instanceof QueryExecutionException ex)) {
			Optional<Object> details = ex.getDetails();
			if (details.isPresent()) {
				productBusinessError = new ProductBusinessError(
						ex.getClass().getName(), ex.getMessage(), (ProductBusinessErrorCode) details.get());
				return new QueryExecutionException("An exception has occurred during query execution", ex, productBusinessError);
			}
		}
		return exception;
	}
}