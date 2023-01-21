package com.vvw.productservice.query.persistance;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductEntityRepository extends JpaRepository<ProductEntity, String> {

	Optional<ProductEntity> findByProductId(String productId);

}