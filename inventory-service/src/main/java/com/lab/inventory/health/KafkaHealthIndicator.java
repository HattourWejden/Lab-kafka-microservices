package com.lab.inventory.health;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import java.util.Properties;
import java.util.concurrent.TimeUnit;

@Component
public class KafkaHealthIndicator implements HealthIndicator {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Override
    public Health health() {
        try {
            Properties props = new Properties();
            props.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
            props.put(AdminClientConfig.REQUEST_TIMEOUT_MS_CONFIG, 3000);

            try (AdminClient adminClient = AdminClient.create(props)) {
                adminClient.listTopics().names().get(3, TimeUnit.SECONDS);
                return Health.up()
                        .withDetail("kafka", "Available")
                        .withDetail("bootstrap-servers", bootstrapServers)
                        .build();
            }
        } catch (Exception e) {
            return Health.down()
                    .withDetail("kafka", "Unavailable")
                    .withDetail("error", e.getMessage())
                    .build();
        }
    }
}