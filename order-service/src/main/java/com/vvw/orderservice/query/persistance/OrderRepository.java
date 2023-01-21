package com.vvw.orderservice.query.persistance;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<OrderEntity, String> {
	Optional<OrderEntity> findByOrderId(String orderId);
}

