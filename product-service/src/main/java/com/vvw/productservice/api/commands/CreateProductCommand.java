package com.vvw.productservice.api.commands;

import lombok.Builder;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Builder
public record CreateProductCommand(
		@TargetAggregateIdentifier
		String productId,
		ZonedDateTime createdAt,
		String title,
		BigDecimal price,
		int quantity) {
}