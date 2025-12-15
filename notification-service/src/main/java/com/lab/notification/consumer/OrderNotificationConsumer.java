package com.lab.notification.consumer;

import com.lab.notification.dto.OrderEventDTO;
import com.lab.notification.model.Notification;
import com. lab.notification.repository.NotificationRepository;
import org.slf4j.Logger;
import org. slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class OrderNotificationConsumer {

    private static final Logger log = LoggerFactory.getLogger(OrderNotificationConsumer.class);
    private final NotificationRepository notificationRepository;

    public OrderNotificationConsumer(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @KafkaListener(topics = "${kafka.topic.order-events}", groupId = "${spring.kafka.consumer.group-id}")
    public void consumeOrderEvent(OrderEventDTO event) {
        log.info("Received order event for notification: {}", event);

        if ("ORDER_CREATED".equals(event. getEventType())) {
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
            log.info("Notification sent and saved:  {}", saved);
        }
    }
}