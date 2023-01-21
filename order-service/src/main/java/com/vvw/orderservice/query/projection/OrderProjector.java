package com.vvw.orderservice.query.projection;

import com.vvw.orderservice.api.dto.OrderSummaryDto;
import com.vvw.orderservice.api.enums.OrderStatus;
import com.vvw.orderservice.api.events.OrderApprovedEvent;
import com.vvw.orderservice.api.events.OrderCreatedEvent;
import com.vvw.orderservice.api.events.OrderRejectedEvent;
import com.vvw.orderservice.api.queries.FindAllOrdersQuery;
import com.vvw.orderservice.api.queries.FindOrderQuery;
import com.vvw.orderservice.query.mapper.OrderMapper;
import com.vvw.orderservice.query.persistance.OrderEntity;
import com.vvw.orderservice.query.persistance.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.queryhandling.QueryHandler;
import org.axonframework.queryhandling.QueryUpdateEmitter;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Slf4j
@Component
@ProcessingGroup("order-group")
public class OrderProjector {

	private final OrderMapper orderMapper;
	private final OrderRepository orderRepository;
	private final QueryUpdateEmitter queryUpdateEmitter;

	public OrderProjector(OrderMapper orderMapper,
	                      OrderRepository orderRepository,
	                      QueryUpdateEmitter queryUpdateEmitter) {
		this.orderMapper = orderMapper;
		this.orderRepository = orderRepository;
		this.queryUpdateEmitter = queryUpdateEmitter;
	}

	@EventHandler
	public void on(OrderCreatedEvent event) {
		OrderEntity orderEntity = orderMapper.map(event);
		log.info("Saving entity: {} from event: {}", orderEntity, event);
		orderRepository.save(orderEntity);

		queryUpdateEmitter.emit(FindAllOrdersQuery.class, query -> true, new OrderSummaryDto(
				event.orderId(),
				event.orderStatus(),
				"Pending order"
		));
	}

	@EventHandler
	public void on(OrderApprovedEvent event) {
		orderRepository.findByOrderId(event.orderId())
				.map(changeOrderStatus(event.orderStatus()))
				.map(orderRepository::save)
				.orElseThrow( () ->
						new IllegalArgumentException("Order with id %s does not exist".formatted(event.orderId())
						));

		queryUpdateEmitter.emit(FindAllOrdersQuery.class, query -> true, new OrderSummaryDto(
				event.orderId(),
				event.orderStatus(),
				"Successfully approve order"
		));
	}

	@EventHandler
	public void on(OrderRejectedEvent event) {
		orderRepository.findByOrderId(event.orderId())
				.map(changeOrderStatus(event.orderStatus()))
				.map(orderRepository::save)
				.orElseThrow(() ->
						new IllegalArgumentException("Order with id %s does not exist".formatted(event.orderId())
						));

		queryUpdateEmitter.emit(FindAllOrdersQuery.class, query -> true, new OrderSummaryDto(
				event.orderId(),
				event.orderStatus(),
				event.reason()
		));
	}

	@QueryHandler
	public Optional<OrderSummaryDto> findOrder(FindOrderQuery query) {
		return orderRepository.findByOrderId(query.orderId())
				.map(orderMapper::map);
	}

	@QueryHandler
	public List<OrderSummaryDto> findAllOrders(FindAllOrdersQuery query) {
		return orderRepository.findAll().stream()
				.map(orderMapper::map)
				.toList();
	}


	private static Function<OrderEntity, OrderEntity> changeOrderStatus(OrderStatus orderStatus) {
		return order -> {
			order.setOrderStatus(orderStatus);
			return order;
		};
	}
}
