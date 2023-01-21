package com.vvw.paymentservice.query.persistance;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serial;
import java.io.Serializable;

@Data
@Entity
@Table(name = "payment")
public class PaymentEntity implements Serializable {

	@Serial
	private static final long serialVersionUID = -87231053028452669L;

	@Id
	private String paymentId;

	@Column
	public String orderId;
}