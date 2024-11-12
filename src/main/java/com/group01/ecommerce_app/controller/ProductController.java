package com.group01.ecommerce_app.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.group01.ecommerce_app.model.ProductRepository;
import com.group01.ecommerce_app.model.Product;

@RestController
@RequestMapping("/api")
public class ProductController {
	@Autowired 
	ProductRepository productRepository;
	
	//1. Get a list of all Product records
	@GetMapping ("/products")
	public ResponseEntity<List<Product>> getAllProducts(@RequestParam(required =
	false) String name) {
		try {
			List<Product> products = new ArrayList<Product>();
			if (name == null) {
				productRepository.findAll().forEach(products::add);
			} else {
				productRepository.findByName(name).forEach(products::add);
			}
			if (products.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}
				return new ResponseEntity<>(products, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	//2. Get a Product record by its id
	@GetMapping("/products/{id}")
	public ResponseEntity<Product> getProductById(@PathVariable("id") long id) {
		try {
			Optional<Product> productData = productRepository.findById(id);
			if (productData.isPresent()) {
				return new ResponseEntity<>(productData.get(), HttpStatus.OK);
			} else {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
		}catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}	
	}
	
	//3. Create a new Product record
	@PostMapping("/products")
	public ResponseEntity<Product> createProduct(@RequestBody Product product) {
		try {
			Product _product = productRepository.save(new
			Product(product.getName(), product.getDescription(), product.getBrand(),
					product.getPrice(), product.getStock(), product.getSize(),
					product.getColor(), product.isActive()));
			return new ResponseEntity<>(_product, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	//4. Update an existing Product record with its id
	@PutMapping("/products/{id}")
	public ResponseEntity<Product> updateProduct(@PathVariable("id") long id, @RequestBody Product product) {
		try {
			Optional<Product> productData = productRepository.findById(id);
			if (productData.isPresent()) {
				Product _product = productData.get();
				_product.setName(product.getName());
				_product.setDescription(product.getDescription());
				_product.setBrand(product.getBrand());
				_product.setPrice(product.getPrice());
				_product.setStock(product.getStock());
				_product.setSize(product.getSize());
				_product.setColor(product.getColor());
				_product.setActive(product.isActive());
				return new ResponseEntity<>(productRepository.save(_product), HttpStatus.OK);
				} else {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
				}
		}catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	//5. Delete an existing Product record with its id
	@DeleteMapping("/products/{id}")
	public ResponseEntity<HttpStatus> deleteProduct(@PathVariable("id") long id) {
		try {
			productRepository.deleteById(id);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
