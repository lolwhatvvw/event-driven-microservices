package com.vvw.paymentservice.query.projection;

import com.vvw.core.events.PaymentProcessedEvent;
import com.vvw.paymentservice.query.mapper.PaymentMapper;
import com.vvw.paymentservice.query.persistance.PaymentRepository;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PaymentProjector {

	private final PaymentMapper paymentMapper;
	private final PaymentRepository paymentRepository;

	public PaymentProjector(PaymentMapper paymentMapper,
	                        PaymentRepository paymentRepository) {
		this.paymentMapper = paymentMapper;
		this.paymentRepository = paymentRepository;
	}

	@EventHandler
	public void on(PaymentProcessedEvent event) {

		log.info("Handling event: {}", event);
		paymentRepository.save(paymentMapper.map(event));
	}
}
