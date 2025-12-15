# Kafka Microservices Lab

A complete implementation of event-driven microservices using Apache Kafka, demonstrating Service-Oriented Architecture (SOA) principles with multiple database technologies.

## Architecture Overview

This project consists of three microservices communicating through Apache Kafka:

- **Order Service** - Creates orders and publishes events (PostgreSQL)
- **Inventory Service** - Consumes order events and manages inventory (MongoDB)
- **Notification Service** - Consumes order events and sends notifications (MariaDB)

```
Order Service (PostgreSQL) → Kafka → Inventory Service (MongoDB)
                                  → Notification Service (MariaDB)
```

## Technologies Used

- Java 17
- Spring Boot 3.2.0
- Apache Kafka 3.7.0
- PostgreSQL 15
- MongoDB 7.0
- MariaDB 11.1
- Docker & Docker Compose
- Maven

## Project Structure

```
kafka-microservices/
├── docker-compose-infra.yml
├── order-service/
│   ├── src/main/java/com/lab/order/
│   │   ├── OrderServiceApplication.java
│   │   ├── config/KafkaTopicConfig.java
│   │   ├── controller/OrderController.java
│   │   ├── dto/OrderDTO.java, OrderEventDTO.java
│   │   ├── model/Order.java
│   │   ├── repository/OrderRepository.java
│   │   ├── service/OrderService.java
│   │   └── health/KafkaHealthIndicator.java
│   └── src/main/resources/application.yml
├── inventory-service/
│   ├── src/main/java/com/lab/inventory/
│   │   ├── InventoryServiceApplication.java
│   │   ├── config/KafkaErrorHandler.java, KafkaConsumerConfig.java
│   │   ├── consumer/OrderConsumer.java
│   │   ├── dto/OrderEventDTO.java
│   │   ├── model/InventoryItem.java
│   │   └── repository/InventoryRepository. java
│   └── src/main/resources/application.yml
└── notification-service/
    ├── src/main/java/com/lab/notification/
    │   ├── NotificationServiceApplication.java
    │   ├── consumer/OrderNotificationConsumer.java
    │   ├── dto/OrderEventDTO.java
    │   ├── model/Notification.java
    │   └── repository/NotificationRepository.java
    └── src/main/resources/application.yml
```

## Prerequisites

- JDK 17+
- Maven 3.8+
- Docker & Docker Compose
- Git

## Getting Started

### 1. Start Infrastructure

```bash
cd kafka-microservices
docker-compose -f docker-compose-infra. yml up -d
```

Verify containers are running:
```bash
docker ps
```

### 2. Build Services

```bash
# Order Service
cd order-service
mvn clean package

# Inventory Service
cd ../inventory-service
mvn clean package

# Notification Service
cd ../notification-service
mvn clean package
```

### 3. Run Services

Open three separate terminals:

**Terminal 1:**
```bash
cd order-service
mvn spring-boot:run
```

**Terminal 2:**
```bash
cd inventory-service
mvn spring-boot:run
```

**Terminal 3:**
```bash
cd notification-service
mvn spring-boot:run
```

Services will start on:
- Order Service: http://localhost:8081
- Inventory Service: http://localhost:8082
- Notification Service: http://localhost:8083
- Kafka UI: http://localhost:8080

## Testing

### Create an Order

```bash
curl -X POST http://localhost:8081/api/orders \
  -H "Content-Type: application/json" \
  -d '{
    "productName": "Laptop",
    "quantity": 2,
    "price": 999.99,
    "customerEmail": "user@example.com"
  }'
```

### Verify Results

**PostgreSQL (Orders):**
```bash
docker exec -it postgres-order psql -U postgres -d orderdb -c "SELECT * FROM orders;"
```

**MongoDB (Inventory):**
```bash
docker exec -it mongodb-inventory mongosh -u admin -p admin --authenticationDatabase admin --eval "use inventorydb; db.inventory.find().pretty()"
```

**MariaDB (Notifications):**
```bash
docker exec -it mariadb-notification mysql -u notifuser -pnotifpass notificationdb -e "SELECT * FROM notifications;"
```

### Test Error Handling

Create an invalid order to trigger DLQ:
```bash
curl -X POST http://localhost:8081/api/orders \
  -H "Content-Type: application/json" \
  -d '{
    "productName": "Invalid",
    "quantity": -5,
    "price": 99.99,
    "customerEmail":  "error@example.com"
  }'
```

Check DLQ in Kafka UI:  http://localhost:8080 → Topics → order-events-dlq

### Health Checks

```bash
curl http://localhost:8081/actuator/health
curl http://localhost:8082/actuator/health
curl http://localhost:8083/actuator/health
```

## Key Features

### Part 1: Basic Producer-Consumer Pattern
- Order Service produces events to Kafka
- Inventory Service consumes events
- Basic database integration

### Part 2: Multiple Consumers
- Added Notification Service as second consumer
- Independent consumer groups
- Polyglot persistence (PostgreSQL, MongoDB, MariaDB)

### Part 3: Advanced Features
- **Partitioning**: Messages with same customer email go to same partition
- **Dead Letter Queue**: Failed messages captured for analysis
- **Manual Acknowledgment**: Better control over message processing
- **Error Handling**: Custom error handler with DLQ forwarding
- **Monitoring**: Kafka UI and health endpoints

## Configuration

### Kafka Topics
- `order-events`: Main topic for order events (3 partitions)
- `order-events-dlq`: Dead letter queue for failed messages (3 partitions)

### Consumer Groups
- `inventory-service-group`: Inventory Service consumer group
- `notification-service-group`: Notification Service consumer group

### Ports
- 8081: Order Service
- 8082: Inventory Service
- 8083: Notification Service
- 8080: Kafka UI
- 9092: Kafka
- 5432: PostgreSQL
- 27017: MongoDB
- 3306: MariaDB

## Stopping Services

```bash
# Stop Spring Boot services (Ctrl+C in each terminal)

# Stop infrastructure
docker-compose -f docker-compose-infra.yml down

# Remove volumes (optional)
docker-compose -f docker-compose-infra.yml down -v
```

## Troubleshooting

**Kafka Connection Issues:**
```bash
docker logs kafka
```

**Database Connection Issues:**
```bash
docker ps | grep -E "postgres|mongo|mariadb"
```

**Consumer Not Receiving Messages:**
- Check consumer group IDs are unique per service
- Verify topic names match in producer and consumer
- Check Kafka UI for messages in topics

#