
package com.group01.ecommerce_app.model;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findByName(String name);
    List<Category> findByUserId(Long userId);
}