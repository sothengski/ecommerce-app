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
import com.group01.ecommerce_app.model.Order;
import com.group01.ecommerce_app.model.OrderItem;
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

			// Initialize sample users
			addUserIfNotFound("admin@example.com", "admin123", "admin");
			addUserIfNotFound("seller@example.com", "seller123", "seller");
			addUserIfNotFound("buyer@example.com", "buyer123", "buyer");

			// Initialize Categories
			Category tShirtCategory = categoryRepository.save(new Category("T-shirt", true));
			Category pantsCategory = categoryRepository.save(new Category("Pants", true));
			Category jacketCategory = categoryRepository.save(new Category("Jacket", true));
			Category shoesCategory = categoryRepository.save(new Category("Shoes", true));

			// Initialize Products
			Product pro1 = new Product("Long Sleeve T-Shirt",
					"Comfortable cotton t-shirt",
					"Zara",
					19.99,
					100,
					List.of("S", "M", "L", "XL"),
					List.of("White", "Black", "Blue"),

					List.of("https://example.com/images/product1.jpg",
							"https://example.com/images/product2.jpg"),
					tShirtCategory, true);
			Product pro4 = new Product("Short Sleeve T-Shirt",
					"Comfortable linen t-shirt",
					"H&M",
					19.99,
					100,
					List.of("S", "L", "XL"),
					List.of("Purple", "Yellow"),

					List.of("https://example.com/images/product1.jpg",
							"https://example.com/images/product2.jpg"),
					tShirtCategory, true);
			productRepository.save(pro1);
			productRepository.save(pro4);
			Product pro2 = new Product("High Waist Pants",
					"Pants with front pockets and black welt pockets",
					"Levi's",
					49.99,
					50,
					List.of("28", "30", "32", "34", "36"),
					List.of("Dark Blue", "Black", "Gray"),
					List.of("https://example.com/images/product1.jpg",
							"https://example.com/images/product2.jpg"),
					pantsCategory, true);
			productRepository.save(pro2);
			Product pro3 = new Product(
					"Faux Fur Jacket",
					"Jacket with lapel collar and long sleeves",
					"Aritzia",
					189.99,
					19,
					List.of("M", "L", "XL"),
					List.of("Gray", "White", "Beige"),
					List.of("https://example.com/images/product1.jpg",
							"https://example.com/images/product2.jpg"),
					jacketCategory, true);
			productRepository.save(pro3);

			// Initialize Orders
			// User admin = userRepository.findByEmail("admin@example.com").orElseThrow();
			// User buyer = userRepository.findByEmail("buyer@example.com").orElseThrow();
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
