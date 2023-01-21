package com.vvw.productservice.query.controller;

import com.vvw.productservice.api.dto.ProductResponseDto;
import com.vvw.productservice.api.queries.FindProductsQuery;
import org.axonframework.extensions.reactor.queryhandling.gateway.ReactorQueryGateway;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api/v1/product")
public class ProductQueryController {

	private final ReactorQueryGateway reactorQueryGateway;

	@Autowired
	public ProductQueryController(ReactorQueryGateway reactorQueryGateway) {
		this.reactorQueryGateway = reactorQueryGateway;
	}

	@GetMapping
	public Mono<List<ProductResponseDto>> retrieveAllProducts() {
		return reactorQueryGateway.query(new FindProductsQuery(), ResponseTypes.multipleInstancesOf(ProductResponseDto.class));
	}
}