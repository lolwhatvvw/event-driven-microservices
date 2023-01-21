package com.vvw.productservice.api.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;

public record CreateProductDto(
		@NotBlank(message = "Title is a required field")
		String title,

		@Min(value = 1, message = "Price cannot be lower than one")
		BigDecimal price,

		@Min(value = 1, message = "Quantity cannot be lower than one")
		@Max(value = 5, message = "Quantity cannot be larger than five")
		int quantity) {
}