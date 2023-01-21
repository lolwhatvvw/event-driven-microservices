package com.vvw.productservice.command.projection;

import com.vvw.productservice.api.events.ProductCreatedEvent;
import com.vvw.productservice.command.persistence.ProductUniquenessCheck;
import com.vvw.productservice.command.persistence.ProductUniquenessCheckRepository;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.eventhandling.ResetHandler;
import org.axonframework.eventhandling.Timestamp;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.ZoneId;

@Component
@Slf4j
@ProcessingGroup(value = "product-group")
public class ProductUniquenessCheckProjector {

	private final ProductUniquenessCheckRepository repository;

	public ProductUniquenessCheckProjector(ProductUniquenessCheckRepository repository) {
		this.repository = repository;
	}

	@EventHandler
	public void on(ProductCreatedEvent event,
	               @Timestamp Instant publishedAt) {
		log.info("Event: {} was published at: {}"
				, event, publishedAt.atZone((ZoneId.of("UTC"))));
		var entity = new ProductUniquenessCheck(event.productId(), event.title());
		repository.save(entity);
	}

	@ResetHandler
	public void reset() {
		repository.deleteAll();
	}
}
