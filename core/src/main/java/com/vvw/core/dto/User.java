package com.vvw.core.dto;

public record User (
		String userId,
		String firstName,
		String lastName,
		CardDetails cardDetails) {

	public static Builder builder() {
		return new Builder();
	}

	public static final class Builder {
		private String userId;
		private String firstName;
		private String lastName;
		private CardDetails cardDetails;

		private Builder() {
		}

		public Builder userId(String val) {
			userId = val;
			return this;
		}

		public Builder firstName(String val) {
			firstName = val;
			return this;
		}

		public Builder lastName(String val) {
			lastName = val;
			return this;
		}

		public Builder cardDetails(CardDetails val) {
			cardDetails = val;
			return this;
		}

		public User build() {
			return new User(
					userId,
					firstName,
					lastName,
					cardDetails
			);
		}
	}
}
