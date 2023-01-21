package com.vvw.productservice.command.controller;

import org.axonframework.config.EventProcessingConfiguration;
import org.axonframework.eventhandling.TrackingEventProcessor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("replay")
public class EventReplayController {

	private final EventProcessingConfiguration configuration;

	public EventReplayController(EventProcessingConfiguration configuration) {
		this.configuration = configuration;
	}

	@PostMapping("/{processorName}")
	public ResponseEntity<TrackingEventProcessor> replayEvents(@PathVariable String processorName) {
		return ResponseEntity.of(
				configuration.eventProcessor(processorName, TrackingEventProcessor.class)
						.map(processor -> {
							processor.shutDown();
							processor.resetTokens();
							processor.start();
							return processor;
						})
		);
	}
}
