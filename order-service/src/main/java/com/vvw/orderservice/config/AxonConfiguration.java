package com.vvw.orderservice.config;

import com.vvw.orderservice.util.ExceptionMapper;
import com.vvw.orderservice.command.handler.OrderServiceEventErrorHandler;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.config.ConfigurationScopeAwareProvider;
import org.axonframework.config.EventProcessingConfigurer;
import org.axonframework.deadline.DeadlineManager;
import org.axonframework.deadline.SimpleDeadlineManager;
import org.axonframework.extensions.reactor.commandhandling.gateway.ReactorCommandGateway;
import org.axonframework.extensions.reactor.queryhandling.gateway.ReactorQueryGateway;
import org.axonframework.spring.messaging.unitofwork.SpringTransactionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class AxonConfiguration {

	@Autowired
	public void processingConfigurer(EventProcessingConfigurer config) {
		config.registerListenerInvocationErrorHandler("order-group",
				conf -> new OrderServiceEventErrorHandler());
		config.registerErrorHandler("order-group",
				conf -> new OrderServiceEventErrorHandler());
	}

	@Bean
	DeadlineManager deadlineManager(org.axonframework.config.Configuration configuration, SpringTransactionManager tm) {
		return SimpleDeadlineManager.builder()
				.scopeAwareProvider(new ConfigurationScopeAwareProvider(configuration))
				.transactionManager(tm)
				.build();
	}

	@Autowired
	public void configureResultHandlerInterceptors(ReactorCommandGateway commandGateway,
	                                               ReactorQueryGateway queryGateway) {
		commandGateway.registerResultHandlerInterceptor(
				(cmd, result) -> result
						.onErrorMap(ExceptionMapper::mapRemoteException)
		);

		queryGateway.registerResultHandlerInterceptor(
				(query, result) -> result.onErrorMap(ExceptionMapper::mapRemoteException)
		);
	}
}