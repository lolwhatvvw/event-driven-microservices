package com.vvw.orderservice.api.dto;

import lombok.Builder;

@Builder
public record CreateOrderDto(
		String userId,
		String addressId,
		String productId,
		int quantity) {
}