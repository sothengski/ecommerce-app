
package com.group01.ecommerce_app.model;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
    // Optional<Order> findByName(String name);
}