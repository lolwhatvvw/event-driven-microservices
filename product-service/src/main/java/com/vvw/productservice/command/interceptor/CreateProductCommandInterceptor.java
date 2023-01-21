package com.vvw.productservice.command.interceptor;

import com.vvw.productservice.api.commands.CreateProductCommand;
import com.vvw.productservice.api.exceptions.ProductNotUniqueException;
import com.vvw.productservice.command.persistence.ProductUniquenessCheck;
import com.vvw.productservice.command.persistence.ProductUniquenessCheckRepository;
import com.vvw.productservice.util.ExceptionMapper;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandMessage;
import org.axonframework.extensions.reactor.messaging.ReactorMessageDispatchInterceptor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.Optional;

@Slf4j
@Component
public class CreateProductCommandInterceptor implements ReactorMessageDispatchInterceptor<CommandMessage<?>> {

	private final ProductUniquenessCheckRepository repository;

	public CreateProductCommandInterceptor(ProductUniquenessCheckRepository repository) {
		this.repository = repository;
	}

	@Override
	public Mono<CommandMessage<?>> intercept(Mono<CommandMessage<?>> monoMessage) {
		return monoMessage.filterWhen(
						message -> {
							log.info("message: [ {} ]", message);
							if (message.getPayloadType().equals(CreateProductCommand.class)) {
								final var payload = (CreateProductCommand) message.getPayload();
								return Mono.fromCallable(() -> internalGetProduct(payload))
										.flatMap(Mono::justOrEmpty)
										.hasElement()
										.map(b -> !b)
										.filter(b -> b)
										.onErrorMap(ExceptionMapper::mapRemoteException)
										.switchIfEmpty(
												Mono
														.error(ExceptionMapper.mapRemoteException(new ProductNotUniqueException(
																payload.productId(),
																payload.title()))
														)
										)

										.subscribeOn(Schedulers.single());
							}
							return monoMessage.hasElement();
						}
				);
	}

	private Optional<ProductUniquenessCheck> internalGetProduct(CreateProductCommand payload) {
		return repository.findProductLookupByProductIdOrTitle(payload.productId(), payload.title());
	}
}
