package com.vvw.orderservice.util;

import com.vvw.orderservice.api.exceptions.OrderCommandExecutionException;
import com.vvw.orderservice.api.exceptions.OrderQueryExecutionException;
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
		if (exception instanceof CommandExecutionException ex) {
			Optional<Object> details = ex.getDetails();
			if (details.isPresent()) {
				return new OrderCommandExecutionException(ex.getMessage(), ex, details.get());
			}
		} else if ((exception instanceof QueryExecutionException ex)) {
			Optional<Object> details = ex.getDetails();
			if (details.isPresent()) {
				return new OrderQueryExecutionException(ex.getMessage(), ex, details.get());
			}
		}
		return exception;
	}
}