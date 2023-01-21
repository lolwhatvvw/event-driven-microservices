package com.vvw.productservice.command.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductUniquenessCheckRepository extends JpaRepository<ProductUniquenessCheck, String> {

	Optional<ProductUniquenessCheck> findProductLookupByProductIdOrTitle(String productId, String title);
}


//import org.springframework.data.r2dbc.repository.R2dbcRepository;
//import reactor.core.publisher.Mono;
//
//public interface ProductUniquenessCheckRepository extends R2dbcRepository<ProductUniquenessCheck, String> {
//	Mono<ProductUniquenessCheck> findProductLookupByProductIdOrTitle(String id, String title);
//}