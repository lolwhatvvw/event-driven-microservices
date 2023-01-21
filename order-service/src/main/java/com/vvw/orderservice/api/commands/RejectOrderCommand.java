package com.vvw.orderservice.api.commands;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

public record RejectOrderCommand (
		@TargetAggregateIdentifier
		String orderId,
		String reason) {

}