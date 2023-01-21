package com.vvw.paymentservice.command;

import com.vvw.core.commands.ProcessPaymentCommand;
import com.vvw.core.dto.CardDetails;
import com.vvw.core.events.PaymentProcessedEvent;
import lombok.ToString;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateCreationPolicy;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.CreationPolicy;
import org.axonframework.spring.stereotype.Aggregate;

import static org.axonframework.modelling.command.AggregateLifecycle.apply;

@ToString
@Aggregate
public class Payment {

	@AggregateIdentifier
	private String paymentId;

	private String orderId;


	@CommandHandler
	@CreationPolicy(AggregateCreationPolicy.ALWAYS)
	public void handle(ProcessPaymentCommand processPaymentCommand) {
		validatePaymentProcessCommand(processPaymentCommand);
		apply(new PaymentProcessedEvent(
				processPaymentCommand.paymentId(),
				processPaymentCommand.orderId())
		);
	}

	@EventSourcingHandler
	public void on(PaymentProcessedEvent paymentProcessedEvent) {
		this.paymentId = paymentProcessedEvent.paymentId();
		this.orderId = paymentProcessedEvent.orderId();
	}

	public Payment() {

	}


	private static void validatePaymentProcessCommand(ProcessPaymentCommand processPaymentCommand) {
		validateOrderId(processPaymentCommand.orderId());
		validatePaymentId(processPaymentCommand.paymentId());
		validateCardDetails(processPaymentCommand.cardDetails());
	}

	private static void validateOrderId(String orderId) {
		if (orderId == null) {
			throw new IllegalArgumentException("Missing orderId");
		}
	}

	private static void validatePaymentId(String paymentId) {
		if (paymentId == null) {
			throw new IllegalArgumentException("Missing paymentId");
		}
	}

	private static void validateCardDetails(CardDetails cardDetails) {
		if (cardDetails == null) {
			throw new IllegalArgumentException("Missing payment details");
		}
	}
}