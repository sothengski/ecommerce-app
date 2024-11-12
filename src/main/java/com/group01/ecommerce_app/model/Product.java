package com.group01.ecommerce_app.model;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data // @Getter @Setter @RequiredArgsConstructor @ToString @EqualsAndHashCode
@Builder
@Entity
@Table(name = "products")
public class Product implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column (name = "name")
    private String name;
    @Column (name = "description")
    private String description;
    @Column (name = "brand")
    private String brand;
    @Column (name = "price")
    private double price;
    @Column (name = "stock")
    private int stock;
    @Column (name = "size")
    private String size;
    @Column (name = "color")
    private String color;
    @Column(name = "active")
    private boolean isActive;
    
    public Product() {
    	
    }

	public Product(String name, String description, String brand, double price, int stock, 
			List<String> size, List<String> color, boolean isActive) {
		this.name = name;
		this.description = description;
		this.brand = brand;
		this.price = price;
		this.stock = stock;
		this.size = String.join(",", size);
		this.color = String.join(",", color);
		this.isActive = isActive;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
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
		return Arrays.asList(size.split(",")) ;
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
    
    
    
}