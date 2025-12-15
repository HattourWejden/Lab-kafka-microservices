package com.lab.inventory.config;

import org. apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org. springframework.kafka.listener.CommonErrorHandler;
import org.springframework.kafka.listener.MessageListenerContainer;
import org. springframework.stereotype.Component;

@Component
public class KafkaErrorHandler implements CommonErrorHandler {

    private static final Logger log = LoggerFactory.getLogger(KafkaErrorHandler.class);
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${kafka.topic.order-events-dlq}")
    private String dlqTopic;

    public KafkaErrorHandler(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void handleRecord(Exception thrownException, ConsumerRecord<?, ?> record,
                             Consumer<?, ?> consumer, MessageListenerContainer container) {
        log.error("Error processing record: {}", record, thrownException);

        // Send to DLQ
        kafkaTemplate.send(dlqTopic, record.key(), record.value());
        log.info("Message sent to DLQ: {}", record);
    }
}