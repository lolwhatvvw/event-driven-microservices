package com.vvw.orderservice.query.mapper;

import com.vvw.orderservice.api.dto.OrderSummaryDto;
import com.vvw.orderservice.api.events.OrderCreatedEvent;
import com.vvw.orderservice.query.persistance.OrderEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface OrderMapper {

	OrderEntity map(OrderCreatedEvent event);

	OrderSummaryDto map(OrderEntity order);
}
