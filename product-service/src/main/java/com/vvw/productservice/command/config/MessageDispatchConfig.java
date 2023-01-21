package com.vvw.productservice.command.config;

import com.vvw.productservice.command.interceptor.CreateProductCommandInterceptor;
import org.axonframework.extensions.reactor.commandhandling.gateway.ReactorCommandGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MessageDispatchConfig {

	@Autowired
	public void configureCommandMessageInterceptors(ReactorCommandGateway reactorCommandGateway,
	                                                CreateProductCommandInterceptor interceptor) {
		reactorCommandGateway.registerDispatchInterceptor(interceptor);
	}
}
