package com.vvw.orderservice.api.commands;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

public record ApproveOrderCommand (
		@TargetAggregateIdentifier
		String orderId) {
}
