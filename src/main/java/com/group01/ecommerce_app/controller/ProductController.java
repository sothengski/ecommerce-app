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

import com.group01.ecommerce_app.dto.ApiResponse;
import com.group01.ecommerce_app.dto.ProductCreateRequestDTO;
import com.group01.ecommerce_app.dto.ProductDTO;
import com.group01.ecommerce_app.model.Category;
import com.group01.ecommerce_app.model.CategoryRepository;
import com.group01.ecommerce_app.model.Product;
import com.group01.ecommerce_app.model.ProductRepository;
import com.group01.ecommerce_app.model.User;
import com.group01.ecommerce_app.model.UserRepository;

@RestController
@RequestMapping("/api")
public class ProductController {

	@Autowired
	ProductRepository productRepository;

	@Autowired
	CategoryRepository categoryRepository;

	@Autowired
	UserRepository userRepository;

	// 1. Get a list of all Product records
	@GetMapping("/products")
	public ResponseEntity<ApiResponse<List<ProductDTO>>> getAllProducts(@RequestParam(required = false) String name) {
		try {
			List<Product> products = productRepository.findAll();
			List<ProductDTO> productDTOs = new ArrayList<>();
			for (Product product : products) {
				productDTOs.add(ProductDTO.convertToProductDTO(product));
			}
			if (name == null) {
				productRepository.findAll().forEach(products::add);
			} else {
				productRepository.findByName(name).forEach(products::add);
			}
			// List of products is Empty
			if (products.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			} // List of products have the data
			return new ResponseEntity<>(new ApiResponse<>(true, "Product retrieved successfully",
					productDTOs), HttpStatus.OK);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ApiResponse<>(false, "Error getting all products data", e.getMessage()));
		}
	}

	// 2. Get a Product record by its id
	@GetMapping("/products/{id}")
	public ResponseEntity<ApiResponse<ProductDTO>> getProductById(@PathVariable("id") long id) {
		try {
			Optional<Product> productData = productRepository.findById(id);
			if (productData.isPresent()) {
				return new ResponseEntity<>(new ApiResponse<>(true, "Product retrieved successfully",
						ProductDTO.convertToProductDTO(productData.get())), HttpStatus.OK);
			} else {
				return new ResponseEntity<>(
						new ApiResponse<>(false, "Product with id " + id + " does not exist", "Product is not found"),
						HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ApiResponse<>(false, "Error getting a product data", e.getMessage()));
		}
	}

	// 3. Search product by name, brand, color, size, userId and categoryId
	@GetMapping("/products/search")
	public ResponseEntity<ApiResponse<List<ProductDTO>>> searchProducts(
			@RequestParam(required = false, defaultValue = "") String name,
			@RequestParam(required = false, defaultValue = "") String brand,
			@RequestParam(required = false, defaultValue = "") String size,
			@RequestParam(required = false, defaultValue = "") String color,
			@RequestParam(required = false) Long userId,
			@RequestParam(required = false) Long categoryId) {

		try {
			List<Product> products = productRepository.findProducts(
					name, brand, size, color, userId, categoryId);

			List<ProductDTO> productDTOs = products.stream()
					.map(ProductDTO::convertToProductDTO)
					.toList();

			if (productDTOs.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}

			return new ResponseEntity<>(new ApiResponse<>(true, "Products retrieved successfully", productDTOs),
					HttpStatus.OK);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ApiResponse<>(false, "Error retrieving products", e.getMessage()));
		}
	}

	// 4. Create a new Product record
	@PostMapping("/products")
	public ResponseEntity<ApiResponse<ProductDTO>> createProduct(@RequestBody ProductCreateRequestDTO productRequest) {
		try {
			// Convert DTO to Product entity
			Product productTemp = new Product();
			if (productRequest.getCategoryId() != null) {
				Optional<Category> category = categoryRepository.findById(productRequest.getCategoryId());
				if (category.isEmpty()) {
					return new ResponseEntity<>(new ApiResponse<>(false,
							"Category not found with id "
									+ productRequest.getCategoryId(),
							"Category not found"),
							HttpStatus.BAD_REQUEST);
				}
				productTemp.setCategory(category.get());
			}
			if (productRequest.getUserId() != null) {
				Optional<User> user = userRepository.findById(productRequest.getUserId());
				if (user.isEmpty()) {
					return new ResponseEntity<>(new ApiResponse<>(false,
							"User not found with id "
									+ productRequest.getUserId(),
							"User not found"),
							HttpStatus.BAD_REQUEST);
				}
				productTemp.setUser(user.get());
			}

			productTemp.setName(productRequest.getName());
			productTemp.setBrand(productRequest.getBrand());
			productTemp.setDescription(productRequest.getDescription());
			productTemp.setPrice(productRequest.getPrice());
			productTemp.setStock(productRequest.getStock());
			productTemp.setSize(productRequest.getSize());
			productTemp.setColor(productRequest.getColor());
			productTemp.setImages(productRequest.getImages());
			// productTemp.setCategory(category);
			productTemp.setActive(productRequest.isActive());

			// Save product to the database
			Product savedProduct = productRepository.save(productTemp);

			// Convert to DTO for response
			ProductDTO responseDto = ProductDTO.convertToProductDTO(savedProduct);

			return new ResponseEntity<>(new ApiResponse<>(true, "Product created successfully",
					responseDto), HttpStatus.CREATED);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ApiResponse<>(false, "Error creating product", e.getMessage()));
		}
	}

	// 5. Update an existing Product record with its id
	@PutMapping("/products/{id}")
	public ResponseEntity<ApiResponse<ProductDTO>> updateProduct(@PathVariable("id") long id,
			@RequestBody ProductCreateRequestDTO productToBeUpdated) {
		try {
			Optional<Product> productData = productRepository.findById(id);
			if (productData.isPresent()) {
				Product updatedProduct = productData.get();

				if (productToBeUpdated.getCategoryId() != null) {
					Optional<Category> category = categoryRepository.findById(productToBeUpdated.getCategoryId());
					if (category.isEmpty()) {
						return new ResponseEntity<>(new ApiResponse<>(false,
								"Category not found with id "
										+ productToBeUpdated.getCategoryId(),
								"Category not found"),
								HttpStatus.BAD_REQUEST);
					}
					updatedProduct.setCategory(category.get());
				}
				if (productToBeUpdated.getUserId() != null) {
					Optional<User> user = userRepository.findById(productToBeUpdated.getUserId());
					if (user.isEmpty()) {
						return new ResponseEntity<>(new ApiResponse<>(false,
								"User not found with id "
										+ productToBeUpdated.getUserId(),
								"user not found"),
								HttpStatus.BAD_REQUEST);
					}
					updatedProduct.setUser(user.get());
				}
				updatedProduct.setName(productToBeUpdated.getName());
				updatedProduct.setDescription(productToBeUpdated.getDescription());
				updatedProduct.setBrand(productToBeUpdated.getBrand());
				updatedProduct.setPrice(productToBeUpdated.getPrice());
				updatedProduct.setStock(productToBeUpdated.getStock());
				updatedProduct.setSize(productToBeUpdated.getSize());
				updatedProduct.setColor(productToBeUpdated.getColor());
				updatedProduct.setImages(productToBeUpdated.getImages());
				// updatedProduct.setCategory(category);
				updatedProduct.setActive(productToBeUpdated.isActive());
				productRepository.save(updatedProduct);

				return new ResponseEntity<>(
						new ApiResponse<>(true, "Product updated successfully",
								ProductDTO.convertToProductDTO(updatedProduct)),
						HttpStatus.OK);
			} else {
				return new ResponseEntity<>(
						new ApiResponse<>(false, "Product with id " + id + " does not exist", "Product not found"),
						HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ApiResponse<>(false, "Error updating product", e.getMessage()));
		}
	}

	// 6. Delete an existing Product record with its id
	@DeleteMapping("/products/{id}")
	public ResponseEntity<ApiResponse<HttpStatus>> deleteProduct(@PathVariable("id") long id) {
		try {
			productRepository.deleteById(id);
			return ResponseEntity.noContent().build();
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ApiResponse<>(false, "Error deleting product", e.getMessage()));
		}
	}
}
