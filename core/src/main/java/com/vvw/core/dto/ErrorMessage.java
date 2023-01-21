package com.vvw.core.dto;

import java.time.LocalDateTime;

public record ErrorMessage(LocalDateTime timestamp,
                           String message) {

	public static Builder builder() {
		return new Builder();
	}

	public static final class Builder {
		private LocalDateTime timestamp;
		private String message;

		private Builder() {
		}

		public Builder timestamp(LocalDateTime val) {
			timestamp = val;
			return this;
		}

		public Builder message(String val) {
			message = val;
			return this;
		}

		public ErrorMessage build() {
			return new ErrorMessage(
					timestamp,
					message
			);
		}
	}
}

