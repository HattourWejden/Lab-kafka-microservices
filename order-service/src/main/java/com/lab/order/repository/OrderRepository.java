package com.lab.order.repository;

import com.lab.order.model.Order;
import org.springframework.data.jpa. repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
}