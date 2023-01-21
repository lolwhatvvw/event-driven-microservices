package com.vvw.paymentservice.query.mapper;

import com.vvw.core.events.PaymentProcessedEvent;
import com.vvw.paymentservice.query.persistance.PaymentEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface PaymentMapper {
	PaymentEntity map(PaymentProcessedEvent event);
}
