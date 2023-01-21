package com.vvw.orderservice.query.controller;

import com.vvw.orderservice.api.dto.OrderSummaryDto;
import com.vvw.orderservice.api.queries.FindAllOrdersQuery;
import com.vvw.orderservice.api.queries.FindOrderQuery;
import org.axonframework.extensions.reactor.queryhandling.gateway.ReactorQueryGateway;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("api/v1/order")
public class OrderQueryController {
	private final ReactorQueryGateway reactorQueryGateway;

	public OrderQueryController(ReactorQueryGateway reactorQueryGateway) {
		this.reactorQueryGateway = reactorQueryGateway;
	}

	@GetMapping(value = "{orderId}")
	public Mono<OrderSummaryDto> singleOrder(@PathVariable String orderId) {
		return reactorQueryGateway.query(new FindOrderQuery(orderId), OrderSummaryDto.class);
	}

	@GetMapping
	public Mono<List<OrderSummaryDto>> allOrders() {
		return reactorQueryGateway.query(new FindAllOrdersQuery(), ResponseTypes.multipleInstancesOf(OrderSummaryDto.class));
	}

	@GetMapping(value = "stream", produces = "text/event-stream")
	public Flux<OrderSummaryDto> orderSubscription() {
		return reactorQueryGateway.subscriptionQueryMany(new FindAllOrdersQuery(), OrderSummaryDto.class);
	}
}
