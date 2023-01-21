package com.vvw.productservice.query.mapper;

import com.vvw.productservice.api.dto.ProductResponseDto;
import com.vvw.productservice.api.events.ProductCreatedEvent;
import com.vvw.productservice.query.persistance.ProductEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)

public interface ProductMapper {

	ProductEntity map(ProductCreatedEvent event);

	ProductResponseDto map(ProductEntity query);
}
