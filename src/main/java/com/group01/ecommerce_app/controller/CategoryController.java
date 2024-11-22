package com.group01.ecommerce_app.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.group01.ecommerce_app.dto.ApiResponse;
import com.group01.ecommerce_app.model.Category;
import com.group01.ecommerce_app.model.CategoryRepository;

@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api")
public class CategoryController {

    @Autowired
    private CategoryRepository categoryRepository;

    // Get all categories
    @GetMapping("/categories")
    public ResponseEntity<ApiResponse<List<Category>>> getAllCategories() {
        try {
            List<Category> categories = categoryRepository.findAll(); // new ArrayList<>();

            // List of roles is Empty
            if (categories.isEmpty()) {
                return new ResponseEntity<>(new ApiResponse<>(true, "Categories retrieved successfully",
                        categories, "No categories found"), HttpStatus.OK);
                // return new ResponseEntity<>(HttpStatus.NO_CONTENT);
                // return new ResponseEntity<>(new ApiResponse<>(true, "No Categories data",
                // Collections.emptyList()), HttpStatus.NO_CONTENT);
            }

            // List of roles have the data
            // return ResponseEntity.ok(categories);
            return new ResponseEntity<>(new ApiResponse<>(true, "Categories retrieved successfully",
                    categories), HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Error getting all categories data", e.getMessage()));
        }
    }

    // Get category by ID
    @GetMapping("/categories/{id}")
    public ResponseEntity<ApiResponse<Category>> getCategoriesById(@PathVariable("id") long id) {
        try {
            Optional<Category> categoryData = categoryRepository.findById(id);
            if (categoryData.isPresent()) {
                Category categoryTemp = categoryData.get();
                return new ResponseEntity<>(new ApiResponse<>(true, "Category retrieved successfully",
                        categoryTemp), HttpStatus.OK);
            }
            return new ResponseEntity<>(
                    new ApiResponse<>(false, "Category with id " + id + " does not exist", "Category not found"),
                    HttpStatus.NOT_FOUND);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Error getting a category data", e.getMessage()));
        }
    }

    // Create a new category
    @PostMapping("/categories")
    public ResponseEntity<ApiResponse<Category>> createCategory(@RequestBody Category category) {
        try {
            Category newCategory = categoryRepository
                    .save(new Category(category.getName(), category.isActive()));
            return new ResponseEntity<>(new ApiResponse<>(true, "Category created successfully",
                    newCategory),
                    HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Error creating category", e.getMessage()));
        }
    }

    // Update an existing category
    @PutMapping("/categories/{id}")
    public ResponseEntity<ApiResponse<Category>> updateCategory(@PathVariable("id") long id,
            @RequestBody Category category) {
        try {
            Optional<Category> categoryData = categoryRepository.findById(id);
            if (categoryData.isPresent()) {
                Category categoryTemp = categoryData.get();
                // update fields
                categoryTemp.setName(category.getName());
                categoryTemp.setActive(category.isActive());
                categoryRepository.save(categoryTemp);
                return new ResponseEntity<>(
                        new ApiResponse<>(true, "Category updated successfully",
                                categoryTemp),
                        HttpStatus.OK);
            }
            // return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            return new ResponseEntity<>(
                    new ApiResponse<>(false, "Category with id " + id + " does not exist", "Category not found"),
                    HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Error updating category", e.getMessage()));
        }
    }

    // Delete a category
    @DeleteMapping("/categories/{id}")
    public ResponseEntity<ApiResponse<String>> deleteCategory(@PathVariable("id") long id) {
        try {
            categoryRepository.deleteById(id);
            return ResponseEntity.noContent().build();
            // return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Error deleting category", e.getMessage()));
        }
    }
}
