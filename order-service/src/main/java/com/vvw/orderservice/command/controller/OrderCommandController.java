package com.vvw.orderservice.command.controller;

import com.vvw.orderservice.api.commands.CreateOrderCommand;
import com.vvw.orderservice.api.dto.CreateOrderDto;
import com.vvw.orderservice.command.mapper.OrderCommandMapper;
import org.axonframework.extensions.reactor.commandhandling.gateway.ReactorCommandGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/order")
public class OrderCommandController {

	private final OrderCommandMapper commandMapper;
	private final ReactorCommandGateway reactorCommandGateway;

	@Autowired
	public OrderCommandController(OrderCommandMapper commandMapper,
	                              ReactorCommandGateway reactorCommandGateway) {
		this.commandMapper = commandMapper;
		this.reactorCommandGateway = reactorCommandGateway;
	}

	@PostMapping
	public Mono<?> createOrder(@RequestBody CreateOrderDto createOrderDto) {
		CreateOrderCommand createOrderCommand = commandMapper.map(createOrderDto);
		return reactorCommandGateway.send(createOrderCommand);
	}
}