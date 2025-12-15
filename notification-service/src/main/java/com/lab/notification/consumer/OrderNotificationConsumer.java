package com.lab.notification.consumer;

import com.lab.notification.dto.OrderEventDTO;
import com.lab.notification.model.Notification;
import com.lab.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderNotificationConsumer {

    private final NotificationRepository notificationRepository;

    @KafkaListener(topics = "${kafka.topic.order-events}", groupId = "${spring.kafka.consumer.group-id}")
    public void consumeOrderEvent(OrderEventDTO event) {
        log.info("Received order event for notification: {}", event);

        if ("ORDER_CREATED".equals(event.getEventType())) {
            String message = String.format(
                    "Your order #%d for %d x %s has been received and is being processed.",
                    event.getOrderId(),
                    event.getQuantity(),
                    event.getProductName()
            );

            Notification notification = new Notification();
            notification.setOrderId(event.getOrderId());
            notification.setRecipientEmail(event.getCustomerEmail());
            notification.setMessage(message);

            Notification saved = notificationRepository.save(notification);
            log.info("Notification sent (simulated): {}", saved);
        }
    }
}