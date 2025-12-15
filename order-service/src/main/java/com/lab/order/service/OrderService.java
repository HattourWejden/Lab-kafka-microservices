package com.lab.order.service;

import com.lab.order.dto.OrderDTO;
import com. lab.order.dto.OrderEventDTO;
import com.lab. order.model.Order;
import com.lab.order.repository. OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j. Slf4j;
import org. springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;
    private final KafkaTemplate<String, OrderEventDTO> kafkaTemplate;

    @Value("${kafka.topic.order-events}")
    private String orderEventsTopic;

    @Transactional
    public Order createOrder(OrderDTO orderDTO) {
        Order order = new Order();
        order.setProductName(orderDTO. getProductName());
        order.setQuantity(orderDTO.getQuantity());
        order.setPrice(orderDTO.getPrice());
        order.setCustomerEmail(orderDTO.getCustomerEmail());

        Order savedOrder = orderRepository. save(order);
        log.info("Order saved to database: {}", savedOrder.getId());

        OrderEventDTO event = new OrderEventDTO(
                savedOrder.getId(),
                savedOrder.getProductName(),
                savedOrder.getQuantity(),
                savedOrder.getCustomerEmail(),
                "ORDER_CREATED"
        );

        kafkaTemplate.send(orderEventsTopic, event);
        log.info("Order event published to Kafka: {}", event);

        return savedOrder;
    }
}