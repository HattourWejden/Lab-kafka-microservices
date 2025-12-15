package com.lab.order. dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {
    private String productName;
    private Integer quantity;
    private Double price;
    private String customerEmail;
}