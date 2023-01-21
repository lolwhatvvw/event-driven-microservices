package com.vvw.productservice.command.handler;

import org.axonframework.eventhandling.*;

import javax.annotation.Nonnull;

public class ProductServiceEventErrorHandler implements ListenerInvocationErrorHandler, ErrorHandler {

	@Override
	public void onError(@Nonnull Exception exception, @Nonnull EventMessage<?> event,
	                    @Nonnull EventMessageHandler eventHandler) throws Exception {
		throw exception;
	}

	@Override
	public void handleError(@Nonnull ErrorContext errorContext) throws Exception {
		Throwable error = errorContext.error();
		if (error instanceof Error) {
			throw (Error) error;
		} else if (error instanceof Exception) {
			throw (Exception) error;
		} else {
			throw new EventProcessingException("An error occurred while handling an event", error);
		}
	}
}
