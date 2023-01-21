package com.vvw.productservice.api.dto;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record ProductResponseDto(
		String productId,
		String title,
		BigDecimal price,
		int quantity) {
}