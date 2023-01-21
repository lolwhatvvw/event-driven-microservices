package com.vvw.core.events;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

public record ProductCreatedEvent(
		String productId,
		ZonedDateTime createdAt,
		String title,
		BigDecimal price,
		int quantity) {
}