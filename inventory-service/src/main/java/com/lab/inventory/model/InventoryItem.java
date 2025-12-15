package com.lab.inventory.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Document(collection = "inventory")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InventoryItem {

    @Id
    private String id;

    private Long orderId;
    private String productName;
    private Integer quantityReserved;
    private String status; // RESERVED, CONFIRMED, RELEASED
    private LocalDateTime createdAt;

    public InventoryItem(Long orderId, String productName, Integer quantityReserved) {
        this.orderId = orderId;
        this.productName = productName;
        this.quantityReserved = quantityReserved;
        this.status = "RESERVED";
        this.createdAt = LocalDateTime. now();
    }
}