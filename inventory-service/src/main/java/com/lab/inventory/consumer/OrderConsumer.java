package com.lab.inventory.consumer;

import com.lab.inventory.dto.OrderEventDTO;
import com.lab.inventory.model. InventoryItem;
import com. lab.inventory.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j. Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework. stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderConsumer {

    private final InventoryRepository inventoryRepository;

    @KafkaListener(topics = "${kafka.topic.order-events}", groupId = "${spring.kafka.consumer.group-id}")
    public void consumeOrderEvent(OrderEventDTO event) {
        log.info("Received order event: {}", event);

        if ("ORDER_CREATED".equals(event.getEventType())) {
            // Reserve inventory
            InventoryItem item = new InventoryItem(
                    event.getOrderId(),
                    event.getProductName(),
                    event.getQuantity()
            );

            InventoryItem saved = inventoryRepository.save(item);
            log.info("Inventory reserved: {}", saved);
        }
    }
}