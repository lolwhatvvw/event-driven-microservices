package com.vvw.productservice.command.mapper;

import com.vvw.productservice.api.commands.CreateProductCommand;
import com.vvw.productservice.api.dto.CreateProductDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.UUID;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, imports = {ZonedDateTime.class, UUID.class, ZoneId.class})
public interface ProductCommandMapper {

	String UTC = "UTC";

	@Mapping(target = "productId", expression = "java(UUID.randomUUID().toString())")
	@Mapping(target = "createdAt", expression = "java(ZonedDateTime.now(ZoneId.of(UTC)))")
	CreateProductCommand map(CreateProductDto dto);
}
