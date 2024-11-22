package com.group01.ecommerce_app.model;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {

    Optional<Cart> findByUserId(Long userId);

    // List<Cart> findAllByUserIdOrderByCreatedDateDesc(Long userId);
}
