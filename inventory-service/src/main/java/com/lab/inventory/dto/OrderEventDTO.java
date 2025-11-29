package com.lab.inventory.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderEventDTO {
    private Long orderId;
    private String productName;
    private Integer quantity;
    private String customerEmail;
    private String eventType;
}