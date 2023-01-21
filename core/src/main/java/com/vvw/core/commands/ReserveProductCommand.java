package com.vvw.core.commands;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

public record ReserveProductCommand(
		@TargetAggregateIdentifier
		String productId,

		int quantity,
		String userId,
		String orderId) {

	public static Builder builder() {
		return new Builder();
	}

	public static final class Builder {
		private String productId;
		private int quantity;
		private String userId;
		private String orderId;

		private Builder() {
		}

		public Builder productId(String val) {
			productId = val;
			return this;
		}

		public Builder quantity(int val) {
			quantity = val;
			return this;
		}

		public Builder userId(String val) {
			userId = val;
			return this;
		}

		public Builder orderId(String val) {
			orderId = val;
			return this;
		}

		public ReserveProductCommand build() {
			return new ReserveProductCommand(
					productId,
					quantity,
					userId,
					orderId
			);
		}
	}
}
