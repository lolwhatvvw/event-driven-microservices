package com.vvw.core.dto;

public record CardDetails(
		String name,
		String cardNumber,
		String cvv,
		int validUntilMonth,
		int validUntilYear) {

	public static Builder builder() {
		return new Builder();
	}

	public static final class Builder {
		private String name;
		private String cardNumber;
		private String cvv;
		private int validUntilMonth;
		private int validUntilYear;

		private Builder() {
		}

		public Builder name(String val) {
			name = val;
			return this;
		}

		public Builder cardNumber(String val) {
			cardNumber = val;
			return this;
		}

		public Builder cvv(String val) {
			cvv = val;
			return this;
		}

		public Builder validUntilMonth(int val) {
			validUntilMonth = val;
			return this;
		}

		public Builder validUntilYear(int val) {
			validUntilYear = val;
			return this;
		}

		public CardDetails build() {
			return new CardDetails(
					name,
					cardNumber,
					cvv,
					validUntilMonth,
					validUntilYear
			);
		}
	}
}
