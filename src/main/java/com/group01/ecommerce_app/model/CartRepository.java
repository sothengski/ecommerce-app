package com.group01.ecommerce_app.model;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> getCartByUserId(Long id);
}
