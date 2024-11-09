
package com.group01.ecommerce_app.model;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
    // Optional<Product> findByName(String name);
}