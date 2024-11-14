package com.group01.ecommerce_app.dto;

import java.util.Arrays;
import java.util.List;

import com.group01.ecommerce_app.model.Category;
import com.group01.ecommerce_app.model.Product;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
// @AllArgsConstructor
@Data
public class ProductDTO {
	private Long id;
	private String name;
	private String description;
	private String brand;
	private double price;
	private int stock;
	private String size;
	private String color;
	private boolean isActive;
	private Category category;
	// private String categoryName; // Display only the name of the category
	private List<String> images; // Assuming URLs are stored for images

	// Constructor
	public ProductDTO(Long id, String name, String description, String brand, double price, int stock,
			List<String> size, List<String> color, List<String> imgList, Category category, boolean isActive) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.brand = brand;
		this.price = price;
		this.stock = stock;
		this.size = String.join(",", size);
		this.color = String.join(",", color);
		this.images = imgList;
		this.category = category;
		this.isActive = isActive;
	}

	// Getter and Setter

	public String getName() {
		return name;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public int getStock() {
		return stock;
	}

	public void setStock(int stock) {
		this.stock = stock;
	}

	public List<String> getSize() {
		return Arrays.asList(size.split(","));
	}

	public void setSize(List<String> size) {
		this.size = String.join(",", size);
	}

	public List<String> getColor() {
		return Arrays.asList(color.split(","));
	}

	public void setColor(List<String> color) {
		this.color = String.join(",", color);
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	// Converts ProductDTO to Product entity
	public static Product convertToProductEntity(ProductDTO productDTO, Category category) {
		Product product = new Product();
		product.setName(productDTO.getName());
		product.setDescription(productDTO.getDescription());
		product.setBrand(productDTO.getBrand());
		product.setPrice(productDTO.getPrice());
		product.setStock(productDTO.getStock());
		product.setSize(productDTO.getSize());
		product.setColor(productDTO.getColor());
		product.setCategory(category); // Set the category based on the category ID
		product.setImages(productDTO.getImages());
		product.setActive(productDTO.isActive());
		return product;
	}

	// Converts Product entity to ProductDTO
	public static ProductDTO convertToProductDTO(Product product) {
		return new ProductDTO(
				product.getId(),
				product.getName(),
				product.getDescription(),
				product.getBrand(),
				product.getPrice(),
				product.getStock(),
				product.getSize(),
				product.getColor(),
				product.getImages(),
				product.getCategory(), // != null ? product.getCategory().getName() : null,
				product.isActive());
	}
}
