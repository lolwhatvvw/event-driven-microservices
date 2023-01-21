package com.vvw.userservice.query.projection;

import com.vvw.core.dto.CardDetails;
import com.vvw.core.dto.User;
import com.vvw.core.queries.FetchUserPaymentDetailsQuery;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Slf4j
@Component
public class UserProjector {

	// return dummy
	@QueryHandler
	public User fetchUserPaymentDetails(FetchUserPaymentDetailsQuery query) {

		log.info("fetchUserPaymentDetails query is called");

		return User.builder()
				.userId(query.userId())
				.firstName("firstName")
				.lastName("lastName")
				.cardDetails(CardDetails.builder()
						.name("name")
						.cvv("123")
						.cardNumber("12345")
						.validUntilYear(2023)
						.validUntilMonth(1)
						.build())
				.build();
// 		return dbCall(query.userId());
//		return reactiveDbCall(query.userId());
	}

	private Optional<User> rdbCall(String id) {
		return Optional.of(User.builder()
				.userId("romka demka")
				.firstName("firstName")
				.lastName("lastName")
				.cardDetails(CardDetails.builder()
						.name("name")
						.cvv("123")
						.cardNumber("12345")
						.validUntilYear(2023)
						.validUntilMonth(1)
						.build())
				.build());
	}
	private Mono<User> reactiveDbCall(String id) {
		return Mono.just(User.builder()
				.userId("romka demka")
				.firstName("firstName")
				.lastName("lastName")
				.cardDetails(CardDetails.builder()
						.name("name")
						.cvv("123")
						.cardNumber("12345")
						.validUntilYear(2023)
						.validUntilMonth(1)
						.build())
				.build());
	}
}
