package com.vvw.orderservice;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.serialization.Serializer;
import org.axonframework.serialization.json.JacksonSerializer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

@Slf4j
@SpringBootApplication
public class OrderServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(OrderServiceApplication.class, args);
	}

	@Bean
	@Primary
	public Serializer buildSerializer() {
		var jacksonSerializer = JacksonSerializer.builder().defaultTyping().build();
		var objectMapper = jacksonSerializer.getObjectMapper();
		objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
		return jacksonSerializer;
	}
}
