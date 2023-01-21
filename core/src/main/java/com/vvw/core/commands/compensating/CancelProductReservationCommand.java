package com.vvw.core.commands.compensating;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

public record CancelProductReservationCommand(

		@TargetAggregateIdentifier
		String productId,
		String orderId,
		String userId,
		String reason,
		int quantity) {

	public static Builder builder() {
		return new Builder();
	}

	public static final class Builder {
		private @TargetAggregateIdentifier String productId;
		private String orderId;
		private String userId;
		private String reason;
		private int quantity;

		private Builder() {
		}

		public Builder productId(String val) {
			productId = val;
			return this;
		}

		public Builder orderId(String val) {
			orderId = val;
			return this;
		}

		public Builder userId(String val) {
			userId = val;
			return this;
		}

		public Builder reason(String val) {
			reason = val;
			return this;
		}

		public Builder quantity(int val) {
			quantity = val;
			return this;
		}

		public CancelProductReservationCommand build() {
			return new CancelProductReservationCommand(
					productId,
					orderId,
					userId,
					reason,
					quantity
			);
		}
	}
}