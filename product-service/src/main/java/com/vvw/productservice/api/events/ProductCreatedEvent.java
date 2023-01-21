package com.vvw.productservice.api.events;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Builder
public record ProductCreatedEvent(
		String productId,
		ZonedDateTime createdAt,
		String title,
		BigDecimal price,
		int quantity) {
}