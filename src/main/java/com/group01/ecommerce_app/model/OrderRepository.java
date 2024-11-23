package com.group01.ecommerce_app.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
    // Fetch all orders by user ID
    List<Order> findByUser_Id(Long userId);
}
