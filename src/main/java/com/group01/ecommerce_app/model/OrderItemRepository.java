package com.group01.ecommerce_app.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    // List<OrderItem> findByOrderOrderId(Long orderId);
}