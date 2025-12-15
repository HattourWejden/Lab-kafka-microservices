package com.lab.notification.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long orderId;

    @Column(nullable = false)
    private String recipientEmail;

    @Column(nullable = false, length = 500)
    private String message;

    @Column(nullable = false)
    private String status;

    @Column(nullable = false)
    private LocalDateTime sentAt;

    public Notification() {}

    public Notification(Long id, Long orderId, String recipientEmail, String message, String status, LocalDateTime sentAt) {
        this.id = id;
        this.orderId = orderId;
        this. recipientEmail = recipientEmail;
        this.message = message;
        this.status = status;
        this.sentAt = sentAt;
    }

    @PrePersist
    protected void onCreate() {
        sentAt = LocalDateTime.now();
        if (status == null) {
            status = "SENT";
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getOrderId() { return orderId; }
    public void setOrderId(Long orderId) { this.orderId = orderId; }

    public String getRecipientEmail() { return recipientEmail; }
    public void setRecipientEmail(String recipientEmail) { this.recipientEmail = recipientEmail; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getSentAt() { return sentAt; }
    public void setSentAt(LocalDateTime sentAt) { this.sentAt = sentAt; }

    @Override
    public String toString() {
        return "Notification{" +
                "id=" + id +
                ", orderId=" + orderId +
                ", recipientEmail='" + recipientEmail + '\'' +
                ", message='" + message + '\'' +
                ", status='" + status + '\'' +
                ", sentAt=" + sentAt +
                '}';
    }
}