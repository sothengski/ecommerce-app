package com.group01.ecommerce_app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.Transactional;

import com.group01.ecommerce_app.model.Category;
import com.group01.ecommerce_app.model.CategoryRepository;
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
			categoryRepository.save(new Category("T-shirt", 1331, true));
			categoryRepository.save(new Category("Pant", 1332, true));
			categoryRepository.save(new Category("Jacket", 1333, true));
			categoryRepository.save(new Category("Shoe", 1334, true));

			// Initialize sample users
			addUserIfNotFound("admin@example.com", "admin123", "admin");
			addUserIfNotFound("seller@example.com", "seller123", "seller");
			addUserIfNotFound("buyer@example.com", "buyer123", "buyer");
			System.out.println("finished initializeDatabase");
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
