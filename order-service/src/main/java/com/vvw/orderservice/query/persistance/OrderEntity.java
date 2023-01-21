package com.vvw.orderservice.query.persistance;

import com.vvw.orderservice.api.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "orders")
public class OrderEntity {

	@Id
	private String orderId;

	private String userId;
	private String addressId;
	private String productId;

	@Enumerated(EnumType.STRING)
	private OrderStatus orderStatus;

	private int quantity;
}
