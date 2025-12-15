package com.lab.order.service;

import com.lab.order.dto.OrderDTO;
import com. lab.order.dto.OrderEventDTO;
import com.lab. order.model.Order;
import com.lab.order.repository.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype. Service;
import org.springframework. transaction.annotation.Transactional;

@Service
public class OrderService {

    private static final Logger log = LoggerFactory.getLogger(OrderService.class);
    private final OrderRepository orderRepository;
    private final KafkaTemplate<String, OrderEventDTO> kafkaTemplate;

    @Value("${kafka.topic.order-events}")
    private String orderEventsTopic;

    public OrderService(OrderRepository orderRepository,
                        KafkaTemplate<String, OrderEventDTO> kafkaTemplate) {
        this.orderRepository = orderRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Transactional
    public Order createOrder(OrderDTO orderDTO) {
        // Save order to database
        Order order = new Order();
        order.setProductName(orderDTO.getProductName());
        order.setQuantity(orderDTO.getQuantity());
        order.setPrice(orderDTO.getPrice());
        order.setCustomerEmail(orderDTO.getCustomerEmail());

        Order savedOrder = orderRepository.save(order);
        log.info("Order saved to database:  {}", savedOrder.getId());

        // Publish event to Kafka with partition key
        OrderEventDTO event = new OrderEventDTO(
                savedOrder.getId(),
                savedOrder.getProductName(),
                savedOrder.getQuantity(),
                savedOrder.getCustomerEmail(),
                "ORDER_CREATED"
        );

        // Use customer email as partition key to ensure order for same customer
        String partitionKey = savedOrder.getCustomerEmail();
        kafkaTemplate.send(orderEventsTopic, partitionKey, event);
        log.info("Order event published to Kafka with partition key: {}", partitionKey);

        return savedOrder;
    }
}