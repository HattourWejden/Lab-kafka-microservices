package com.lab.notification.dto;

public class OrderEventDTO {
    private Long orderId;
    private String productName;
    private Integer quantity;
    private String customerEmail;
    private String eventType;

    public OrderEventDTO() {}

    public OrderEventDTO(Long orderId, String productName, Integer quantity, String customerEmail, String eventType) {
        this.orderId = orderId;
        this.productName = productName;
        this. quantity = quantity;
        this. customerEmail = customerEmail;
        this.eventType = eventType;
    }

    public Long getOrderId() { return orderId; }
    public void setOrderId(Long orderId) { this.orderId = orderId; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public String getCustomerEmail() { return customerEmail; }
    public void setCustomerEmail(String customerEmail) { this.customerEmail = customerEmail; }

    public String getEventType() { return eventType; }
    public void setEventType(String eventType) { this.eventType = eventType; }

    @Override
    public String toString() {
        return "OrderEventDTO{" +
                "orderId=" + orderId +
                ", productName='" + productName + '\'' +
                ", quantity=" + quantity +
                ", customerEmail='" + customerEmail + '\'' +
                ", eventType='" + eventType + '\'' +
                '}';
    }
}