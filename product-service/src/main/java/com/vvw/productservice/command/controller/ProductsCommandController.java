package com.vvw.productservice.command.controller;

import com.vvw.productservice.api.commands.CreateProductCommand;
import com.vvw.productservice.api.dto.CreateProductDto;
import com.vvw.productservice.command.mapper.ProductCommandMapper;
import org.axonframework.extensions.reactor.commandhandling.gateway.ReactorCommandGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/product")
public class ProductsCommandController {

	private final ProductCommandMapper commandMapper;
	private final ReactorCommandGateway reactorCommandGateway;


	@Autowired
	public ProductsCommandController(ProductCommandMapper commandMapper,
	                                 ReactorCommandGateway reactorCommandGateway) {
		this.commandMapper = commandMapper;
		this.reactorCommandGateway = reactorCommandGateway;
	}

	@PostMapping
	public Mono<ResponseEntity<Mono<String>>> createProduct(@RequestBody CreateProductDto createProductDto) {
		CreateProductCommand createProductCommand = commandMapper.map(createProductDto);
		Mono<String> send = reactorCommandGateway.send(createProductCommand);
		return Mono.just(ResponseEntity.ok(send));
	}
}