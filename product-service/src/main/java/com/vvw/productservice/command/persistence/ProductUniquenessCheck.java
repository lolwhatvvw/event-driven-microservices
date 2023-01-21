package com.vvw.productservice.command.persistence;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serial;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "product_lookup")
public class ProductUniquenessCheck implements Serializable {

	@Serial
	private static final long serialVersionUID = 1999847836768099706L;

	@Id
	private String productId;

	@Column(unique = true)
	private String title;
}