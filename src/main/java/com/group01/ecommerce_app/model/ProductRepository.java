
package com.group01.ecommerce_app.model;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByName(String name);
    List<Product> findByBrand(String brand);
    List<Product> findByPriceBetween(double minPrice, double maxPrice);
    List<Product> findByColorContaining(String color);
    List<Product> findBySizeContaining(String size);
}