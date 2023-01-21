package com.vvw.orderservice.api.dto;

import com.vvw.orderservice.api.enums.OrderStatus;

public record OrderSummaryDto (
		String orderId,
		OrderStatus orderStatus,
		String message) {

}