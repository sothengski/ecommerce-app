
package com.group01.ecommerce_app.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductRepository extends JpaRepository<Product, Long> {

	// Custom method to find products by user ID
	// @Query("SELECT p FROM Product p WHERE p.user.id = :userId")
	// List<Product> findByUserId(@Param("userId") Long userId);

	// Find products by user ID using derived query
	List<Product> findByUser_Id(Long userId);

	// Existing method to find by name (if needed)
	List<Product> findByName(String name);
	// List<Product> findByBrand(String brand);
	// @Query("SELECT p FROM Product p WHERE :size IS NULL OR p.size LIKE %:size%")
	// List<Product> findBySize(@Param("size") String size);
	// @Query("SELECT p FROM Product p WHERE :color IS NULL OR p.color LIKE
	// %:color%")
	// List<Product> findByColor(@Param("color") String color);
	// List<Product> findByCategoryId(Long categoryId);
	// List<Product> findByUserId(Long userId);

	@Query("SELECT p FROM Product p WHERE "
			+ "(:name IS NULL OR p.name LIKE %:name%) AND "
			+ "(:brand IS NULL OR p.brand LIKE %:brand%) AND "
			+ "(:size IS NULL OR p.size LIKE %:size%) AND "
			+ "(:color IS NULL OR p.color LIKE %:color%) AND "
			+ "(:userId IS NULL OR p.user.id = :userId) AND "
			+ "(:categoryId IS NULL OR p.category.id = :categoryId)")
	List<Product> findProducts(
			@Param("name") String name,
			@Param("brand") String brand,
			@Param("size") String size,
			@Param("color") String color,
			@Param("userId") Long userId,
			@Param("categoryId") Long categoryId);

}