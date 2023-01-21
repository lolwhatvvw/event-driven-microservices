package com.vvw.productservice.query.projection;

import com.vvw.core.events.ProductReservedEvent;
import com.vvw.core.events.compensating.ProductReservationCancelledEvent;
import com.vvw.productservice.api.dto.ProductResponseDto;
import com.vvw.productservice.api.events.ProductCreatedEvent;
import com.vvw.productservice.api.exceptions.ProductNotFound;
import com.vvw.productservice.api.queries.FindProductsQuery;
import com.vvw.productservice.query.mapper.ProductMapper;
import com.vvw.productservice.query.persistance.ProductEntity;
import com.vvw.productservice.query.persistance.ProductEntityRepository;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.eventhandling.ResetHandler;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Function;
import java.util.function.IntBinaryOperator;

@Slf4j
@Service
@ProcessingGroup(value = "product-group")
public class ProductProjector {

	private final ProductMapper productMapper;
	private final ProductEntityRepository productRepository;

	public ProductProjector(ProductEntityRepository productRepository, ProductMapper productMapper) {
		this.productRepository = productRepository;
		this.productMapper = productMapper;
	}

	@EventHandler
	public void on(ProductCreatedEvent event) {
		var product = productMapper.map(event);
		productRepository.save(product);
	}

	@EventHandler
	public void on(ProductReservedEvent event) {
		productRepository.findByProductId(event.productId())
				.map(changeProductQuantity(event.quantity(), (a, b) -> a - b))
//				.map(decreaseProductQuantity(event.quantity()))
				.map(productRepository::save)
				.orElseThrow(() -> new ProductNotFound(
						"Product with id: %s is not found".formatted(event.productId())));
	}

	@EventHandler
	public void on(ProductReservationCancelledEvent event) {
		productRepository.findByProductId(event.productId())
				.map(changeProductQuantity(event.quantity(), Integer::sum))
//				.map(increaseProductQuantity(event.quantity()))
				.map(productRepository::save);
	}

	@QueryHandler
	public List<ProductResponseDto> findAll(FindProductsQuery query) {
		return productRepository.findAll()
				.stream()
				.map(productMapper::map)
				.toList();
	}

	@ResetHandler
	public void reset() {
		productRepository.deleteAll();
	}


	private static Function<ProductEntity, ProductEntity> decreaseProductQuantity(int quantity) {
		return entity -> {
			entity.setQuantity(entity.getQuantity() - quantity);
			return entity;
		};
	}

	private static Function<ProductEntity, ProductEntity> increaseProductQuantity(int quantity) {
		return entity -> {
			entity.setQuantity(entity.getQuantity() + quantity);
			return entity;
		};
	}

	private static Function<ProductEntity, ProductEntity> changeProductQuantity(int quantity, IntBinaryOperator op) {
		return entity -> {
			entity.setQuantity(op.applyAsInt(entity.getQuantity(), quantity));
			return entity;
		};
	}
}
