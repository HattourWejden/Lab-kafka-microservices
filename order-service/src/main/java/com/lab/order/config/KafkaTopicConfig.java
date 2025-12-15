package com.lab.order.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    @Value("${kafka.topic.order-events}")
    private String orderEventsTopic;

    @Value("${kafka.topic.order-events-dlq}")
    private String orderEventsDlqTopic;

    @Value("${kafka.partitions}")
    private int partitions;

    @Value("${kafka.replication-factor}")
    private int replicationFactor;

    @Bean
    public NewTopic orderEventsTopic() {
        return TopicBuilder.name(orderEventsTopic)
                .partitions(partitions)
                .replicas(replicationFactor)
                .build();
    }

    @Bean
    public NewTopic orderEventsDlqTopic() {
        return TopicBuilder.name(orderEventsDlqTopic)
                .partitions(partitions)
                .replicas(replicationFactor)
                .build();
    }
}