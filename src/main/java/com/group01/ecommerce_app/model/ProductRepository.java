
package com.group01.ecommerce_app.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByName(String name);
    
    List<Product> findByNameAndBrandAndSizeAndColorAndCategoryId(
            String name, String brand, String size, String color, Long categoryId);

    List<Product> findByPriceBetween(double minPrice, double maxPrice);

}