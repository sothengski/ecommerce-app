package com.group01.ecommerce_app.dto;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.group01.ecommerce_app.model.Category;
import com.group01.ecommerce_app.model.Product;
import com.group01.ecommerce_app.model.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class ProductDTO implements Serializable {
	private Long id;
	private String name;
	private String description;
	private String brand;
	private double price;
	private int stock;
	private String size;
	private String color;
	private boolean isActive;

	@JsonProperty("user")
	@JsonInclude(Include.NON_NULL)
	private UserDTO userDTO;

	@JsonProperty("category")
	@JsonInclude(Include.NON_NULL)
	private Category category;
	private List<String> images; // Assuming URLs are stored for images

	// Constructor
	public ProductDTO(Long id, String name, String description, String brand, double price, int stock,
			List<String> size, List<String> color, List<String> imgList,
			UserDTO userDTO, Category category, boolean isActive) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.brand = brand;
		this.price = price;
		this.stock = stock;
		this.size = String.join(",", size);
		this.color = String.join(",", color);
		this.images = imgList;
		this.userDTO = userDTO;
		this.category = category;
		this.isActive = isActive;
	}

	public ProductDTO(Product product) {
		this.id = product.getId();
		this.name = product.getName();
		this.description = product.getDescription();
		this.brand = product.getBrand();
		this.category = product.getCategory();
		this.stock = product.getStock();
		this.images = product.getImages();
		this.price = product.getPrice();
		this.userDTO = UserDTO.convertToUserDTO(product.getUser(), "role", "active");
		this.color = String.join(",", product.getColor());
		this.size = String.join(",", product.getSize());
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
		// return Arrays.asList(size.split(","));
		// Check if size is not null before calling split
		return size != null ? Arrays.asList(size.split(",")) : Arrays.asList();
	}

	public void setSize(List<String> size) {
		this.size = String.join(",", size);
	}

	public List<String> getColor() {
		// return Arrays.asList(color.split(","));
		return color != null ? Arrays.asList(color.split(",")) : Arrays.asList();

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

	public List<String> getImages() {
		return images;
	}

	public void setImages(List<String> images) {
		this.images = images;
	}

	// Converts ProductDTO to Product entity
	public static Product convertToProductEntity(ProductDTO productDTO, User user, Category category) {
		Product product = new Product();
		product.setName(productDTO.getName());
		product.setDescription(productDTO.getDescription());
		product.setBrand(productDTO.getBrand());
		product.setPrice(productDTO.getPrice());
		product.setStock(productDTO.getStock());
		product.setSize(productDTO.getSize());
		product.setColor(productDTO.getColor());
		product.setCategory(category);
		product.setUser(user);
		product.setImages(productDTO.getImages());
		product.setActive(productDTO.isActive());
		return product;
	}

	// Converts Product entity to ProductDTO
	public static ProductDTO convertToProductDTO(final Product product) {
		// return ProductDTO.builder()
		// .id(product.getId())
		// .name(product.getName())
		// .description(product.getDescription())
		// .brand(product.getBrand())
		// .price(product.getPrice())
		// .stock(product.getStock())
		// .size(product.getSize().toString())
		// .color(product.getColor().toString())
		// .images(product.getImages())
		// .userDTO(UserDTO.builder()
		// .id(product.getUser().getId())
		// .email(product.getUser().getEmail())
		// .build())
		// .build();

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
				UserDTO.convertToUserDTO(product
						.getUser(),
						"role",
						"phone", "address", "active"),
				product.getCategory(), // != null ? product.getCategory().getName() : null,
				product.isActive());
	}
}
