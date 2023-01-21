package com.vvw.orderservice.command.handler;

import com.vvw.core.dto.ErrorMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Slf4j
@RestControllerAdvice
public class OrderServiceExceptionHandler {

	@ExceptionHandler(value = {Exception.class})
	public Mono<ResponseEntity<?>> handleAll(Exception ex) {
		logException(ex);
		var errorMessage = ErrorMessage.builder()
				.timestamp(LocalDateTime.now())
				.message(ex.getMessage())
				.build();
		return Mono.defer(() -> Mono.just(ResponseEntity.badRequest().body(errorMessage)));
	}

	@ExceptionHandler({WebExchangeBindException.class})
	public Mono<ResponseEntity<?>> handleValidationError(WebExchangeBindException ex) {
		logException(ex);
		var errors = ex.getBindingResult()
				.getAllErrors()
				.stream()
				.map(DefaultMessageSourceResolvable::getDefaultMessage)
				.peek(log::info)
				.toList();
		var errorMessage = ErrorMessage.builder()
				.timestamp(LocalDateTime.now())
				.message(errors.toString())
				.build();
		return Mono.defer(() -> Mono.just(ResponseEntity.badRequest().body(errorMessage)));
	}


	private void logException(Throwable throwable) {
		String causeString =
				throwable.getCause() != null
						? " and cause is " + throwable.getCause().getClass().getSimpleName()
						: "";
		log.error("exception is " + throwable.getClass().getSimpleName() + causeString);
	}

//	private String getErrorResponseMessage(Throwable throwable) {
//		if (throwable instanceof CommandExecutionException cee) {
//			return cee.getDetails()
//					.map(it -> {
//						ProductBusinessError productBusinessError = (ProductBusinessError) it;
//						log.error("Unable to create Product due to validation constrains. Reason: " + productBusinessError);
//						return productBusinessError.code() + " : " + productBusinessError.message();
//					})
//					.orElseGet(() -> {
//						log.error("Unable to create Product due to " + throwable);
//						return cee.getMessage();
//					});
//		} else {
//			log.error("Unable to create Product due to unknown generic exception " + throwable);
//			return throwable.getMessage();
//		}
//	}
}
