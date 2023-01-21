package com.vvw.productservice.config;

import com.vvw.productservice.util.ExceptionMapper;
import com.vvw.productservice.command.handler.ProductServiceEventErrorHandler;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.config.EventProcessingConfigurer;
import org.axonframework.eventsourcing.EventCountSnapshotTriggerDefinition;
import org.axonframework.eventsourcing.SnapshotTriggerDefinition;
import org.axonframework.eventsourcing.Snapshotter;
import org.axonframework.extensions.reactor.commandhandling.gateway.ReactorCommandGateway;
import org.axonframework.extensions.reactor.queryhandling.gateway.ReactorQueryGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class AxonConfiguration {

	@Autowired
	public void processingConfigurer(EventProcessingConfigurer config) {
		config.registerListenerInvocationErrorHandler("product-group",
				conf -> new ProductServiceEventErrorHandler());
		config.registerErrorHandler("product-group",
				conf -> new ProductServiceEventErrorHandler());
	}

	@Bean(name = "productSnapshot")
	public SnapshotTriggerDefinition snapshotTriggerDefinition(Snapshotter snapshotter) {
		return new EventCountSnapshotTriggerDefinition(snapshotter, 5);
	}

	@Autowired
	public void configureResultHandlerInterceptors(ReactorCommandGateway commandGateway,
	                                               ReactorQueryGateway queryGateway) {
		commandGateway.registerResultHandlerInterceptor(
				(cmd, result) -> result.onErrorMap(ExceptionMapper::mapRemoteException)
		);

		queryGateway.registerResultHandlerInterceptor(
				(query, result) -> result.onErrorMap(ExceptionMapper::mapRemoteException)
		);
	}
}
