package com.vvw.core.commands;

import com.vvw.core.dto.CardDetails;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

public record ProcessPaymentCommand (

		@TargetAggregateIdentifier
		 String paymentId,

		 String orderId,
		 CardDetails cardDetails) {

	public static Builder builder() {
		return new Builder();
	}

	public static final class Builder {
		private String paymentId;
		private String orderId;
		private CardDetails cardDetails;

		private Builder() {
		}

		public Builder paymentId(String val) {
			paymentId = val;
			return this;
		}

		public Builder orderId(String val) {
			orderId = val;
			return this;
		}

		public Builder cardDetails(CardDetails val) {
			cardDetails = val;
			return this;
		}

		public ProcessPaymentCommand build() {
			return new ProcessPaymentCommand(
					paymentId,
					orderId,
					cardDetails);
		}
	}
}
