package com.vvw.orderservice.command.mapper;

import com.vvw.orderservice.api.commands.CreateOrderCommand;
import com.vvw.orderservice.api.dto.CreateOrderDto;
import com.vvw.orderservice.api.enums.OrderStatus;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.UUID;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
		imports = {ZonedDateTime.class, UUID.class, ZoneId.class, OrderStatus.class})
public interface OrderCommandMapper {

	 String UTC = "UTC";

	@Mapping(target = "orderId", expression = "java(UUID.randomUUID().toString())")
	@Mapping(target = "createdAt", expression = "java(ZonedDateTime.now(ZoneId.of(UTC)))")
	@Mapping(target = "orderStatus", expression = "java(OrderStatus.CREATED)")
	CreateOrderCommand map(CreateOrderDto dto);
}
