package com.group01.ecommerce_app;

import java.math.BigDecimal;
import java.sql.Array;
import java.time.LocalDateTime;
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
import com.group01.ecommerce_app.model.OrderItemRepository;
import com.group01.ecommerce_app.model.OrderRepository;
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

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private OrderItemRepository orderItemRepository;

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
			User admin = addUserIfNotFound("admin@example.com", "admin123", "admin");
			User seller = addUserIfNotFound("seller@example.com", "seller123", "seller");
			User buyer = addUserIfNotFound("buyer@example.com", "buyer123", "buyer");

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
					admin, tShirtCategory, true);
			Product pro4 = new Product("Short Sleeve T-Shirt",
					"Comfortable linen t-shirt",
					"H&M",
					19.99,
					100,
					List.of("S", "L", "XL"),
					List.of("Purple", "Yellow"),

					List.of("https://example.com/images/product1.jpg",
							"https://example.com/images/product2.jpg"),
					seller, tShirtCategory, true);
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
					admin, pantsCategory, true);
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
					buyer, jacketCategory, true);
			productRepository.save(pro3);

			// Initialize Orders
			// User admin = userRepository.findByEmail("admin@example.com").orElseThrow();
			// User buyer = userRepository.findByEmail("buyer@example.com").orElseThrow();

			// Example of creating an order with items

			// Sample Orders
			Order order1 = new Order();
			order1.setUserId(1L);
			order1.setOrderNumber("ORD123456");
			order1.setOrderStatus("Processing");
			order1.setOrderDate(LocalDateTime.now());
			order1.setTotalQuantity(3);
			order1.setTotalPrice(150.00);
			order1.setCurrency("USD");
			order1.setPaymentStatus("Pending");
			order1.setPaymentMethod("Credit Card");
			order1.setShippingAddress("123 Main St");
			order1.setShippingCity("New York");
			order1.setShippingState("NY");
			order1.setShippingPostalCode("10001");
			order1.setShippingCountry("USA");
			order1.setShippingCost(5.99);

			Order order2 = new Order();
			order2.setUserId(2L);
			order2.setOrderNumber("ORD123457");
			order2.setOrderStatus("Shipped");
			order2.setOrderDate(LocalDateTime.now().minusDays(1));
			order2.setTotalQuantity(2);
			order2.setTotalPrice(75.00);
			order2.setCurrency("USD");
			order2.setPaymentStatus("Paid");
			order2.setPaymentMethod("PayPal");
			order2.setShippingAddress("456 Elm St");
			order2.setShippingCity("Los Angeles");
			order2.setShippingState("CA");
			order2.setShippingPostalCode("90001");
			order2.setShippingCountry("USA");
			order2.setShippingCost(4.99);

			// Save Orders
			order1 = orderRepository.save(order1);
			order2 = orderRepository.save(order2);

			// Order Items
			OrderItem item1 = new OrderItem();
			item1.setOrder(order1);
			item1.setProduct(pro1);
			item1.setQuantity(2);
			item1.setUnitPrice(50.00);
			item1.setTotalPrice(100.00);

			OrderItem item2 = new OrderItem();
			item2.setOrder(order1);
			item2.setProduct(pro2);
			item2.setQuantity(1);
			item2.setUnitPrice(25.00);
			item2.setTotalPrice(25.00);

			OrderItem item3 = new OrderItem();
			item3.setOrder(order2);
			item3.setProduct(pro3);
			item3.setQuantity(1);
			item3.setUnitPrice(50.00);
			item3.setTotalPrice(50.00);

			// Save Order Items
			orderItemRepository.saveAll(List.of(item1, item2, item3));

		};

	}

	private void addRoleIfNotFound(String roleName) {
		roleRepository.findByName(roleName).orElseGet(() -> roleRepository.save(new Role(roleName)));
	}

	private User addUserIfNotFound(String email, String password, String roleName) {
		Role role = roleRepository.findByName(roleName)
				.orElseThrow(() -> new RuntimeException("Role not found"));
		return userRepository.findByEmail(email).orElseGet(() -> userRepository.save(new User(email, password, role)));
	}

}
