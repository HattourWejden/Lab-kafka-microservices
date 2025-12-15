package com.lab.notification.repository;

import com.lab.notification.model.Notification;
import org.springframework.data. jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
}