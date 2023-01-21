package com.vvw.productservice.query.persistance;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "product")
@Table(name = "product")
public class ProductEntity implements Serializable {
	@Serial
	private static final long serialVersionUID = 4078825844279601718L;

	@Id
	@Column(unique = true)
	private String productId;

	private String title;

	private BigDecimal price;
	private Integer quantity;
	private ZonedDateTime createdAt;
}