package com.group01.ecommerce_app;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.Transactional;

import com.group01.ecommerce_app.model.Category;
import com.group01.ecommerce_app.model.CategoryRepository;
import com.group01.ecommerce_app.model.Product;
import com.group01.ecommerce_app.model.ProductRepository;
import com.group01.ecommerce_app.model.Role;
import com.group01.ecommerce_app.model.RoleRepository;
import com.group01.ecommerce_app.model.User;
import com.group01.ecommerce_app.model.UserRepository;

@SpringBootApplication
public class EcommerceAppApplication {

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private CategoryRepository categoryRepository;
	
	@Autowired
	private ProductRepository productRepository;

	public static void main(String[] args) {
		SpringApplication.run(EcommerceAppApplication.class, args);
		System.out.println("Program Start");
	}

	@Bean
	@Transactional
	public CommandLineRunner initializeDatabase() {
		return args -> {
			// Initialize roles
			addRoleIfNotFound("admin");
			addRoleIfNotFound("seller");
			addRoleIfNotFound("buyer");

			// Initialize Categories
			categoryRepository.save(new Category("T-shirt", true));
			categoryRepository.save(new Category("Pant", true));
			categoryRepository.save(new Category("Jacket", true));
			categoryRepository.save(new Category("Shoe", true));

			// Initialize sample users
			addUserIfNotFound("admin@example.com", "admin123", "admin");
			addUserIfNotFound("seller@example.com", "seller123", "seller");
			addUserIfNotFound("buyer@example.com", "buyer123", "buyer");
			System.out.println("finished initializeDatabase");
			
			//Initialize Products
			productRepository.save(new Product("T-Shirt",
				    "Comfortable cotton t-shirt",
				    "Zara",
				    19.99,
				    100,
				    List.of("S", "M", "L", "XL"),
				    List.of("White", "Black", "Blue"),
				    true));
			productRepository.save(new Product("Jeans",
				    "Classic denim jeans with a slim fit",
				    "Levi's",
				    49.99,
				    50,
				    List.of("28", "30", "32", "34", "36"),
				    List.of("Dark Blue", "Black", "Gray"),
				    true));
			productRepository.save(new Product(
				    "Sweater",
				    "Cozy wool sweater for winter",
				    "Aritzia",
				    89.99,
				    19,
				    List.of("M", "L", "XL"),
				    List.of("Gray", "White", "Beige"),
				    false
				));
		};
	}

	private void addRoleIfNotFound(String roleName) {
		roleRepository.findByName(roleName).orElseGet(() -> roleRepository.save(new Role(roleName)));
	}

	private void addUserIfNotFound(String email, String password, String roleName) {
		Role role = roleRepository.findByName(roleName)
				.orElseThrow(() -> new RuntimeException("Role not found"));
		userRepository.findByEmail(email).orElseGet(() -> userRepository.save(new User(email, password, role)));
	}

}
