package com.group01.ecommerce_app;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.type.TypeReference;
import com.group01.ecommerce_app.loader.JsonLoader;
import com.group01.ecommerce_app.model.Cart;
import com.group01.ecommerce_app.model.CartRepository;
import com.group01.ecommerce_app.model.Category;
import com.group01.ecommerce_app.model.CategoryRepository;
import com.group01.ecommerce_app.model.Item;
import com.group01.ecommerce_app.model.ItemRepository;
import com.group01.ecommerce_app.model.Order;
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
	private CartRepository cartRepository;

	@Autowired
	private ItemRepository itemRepository;

	public static void main(String[] args) {
		SpringApplication.run(EcommerceAppApplication.class, args);
		System.out.println("Program Start");
	}

	@Bean
	@Transactional
	public CommandLineRunner initializeDatabase() {
		return args -> {

			// Initialize roles
			loadRoles();
			// addRoleIfNotFound("admin");
			// addRoleIfNotFound("seller");
			// addRoleIfNotFound("buyer");

			// Initialize sample users
			User admin = addUserIfNotFound("admin@example.com", "admin123", "admin");
			User seller = addUserIfNotFound("seller@example.com", "seller123", "seller");
			User buyer = addUserIfNotFound("buyer@example.com", "buyer123", "buyer");
			List<User> users = loadUsers();
			System.out.println("Users loaded: " + users.size());

			// Initialize Categories

			Category tShirtCategory = categoryRepository.save(new Category("T-shirt", true));
			Category pantsCategory = categoryRepository.save(new Category("Pants", true));
			Category jacketCategory = categoryRepository.save(new Category("Jacket", true));
			Category shoesCategory = categoryRepository.save(new Category("Shoes", true));

			List<Category> categories = loadCategories();
			System.out.println("Categories loaded: " + categories.size());

			// Initialize Products
			Product pro1 = new Product("Long Sleeve T-Shirt",
					"Comfortable cotton t-shirt",
					"Zara",
					19.99,
					50,
					List.of("S", "M", "L"),
					List.of("White", "Black", "Blue"),
					List.of("https://example.com/images/product1.jpg",
							"https://example.com/images/product2.jpg"),
					admin, tShirtCategory, true);
			Product pro3 = new Product("Short Sleeve T-Shirt",
					"Comfortable linen t-shirt",
					"H&M",
					19.99,
					20,
					List.of("S", "L"),
					List.of("Purple", "Yellow"),
					List.of("https://example.com/images/product12.jpg",
							"https://example.com/images/product22.jpg"),
					seller, tShirtCategory, true);
			productRepository.save(pro1);
			productRepository.save(pro3);
			Product pro2 = new Product("High Waist Pants",
					"Pants with front pockets and black welt pockets",
					"Levi's",
					49.99,
					10,
					List.of("28", "30", "34"),
					List.of("Dark Blue", "Gray"),
					List.of("https://example.com/images/product1.jpg",
							"https://example.com/images/product2.jpg"),
					buyer, pantsCategory, true);
			productRepository.save(pro2);

			List<Product> productsLoader = loadProducts();
			System.out.println("productsLoader loaded: " + productsLoader.size());

			// Initialize Orders
			// User admin = userRepository.findByEmail("admin@example.com").orElseThrow();
			// User buyer = userRepository.findByEmail("buyer@example.com").orElseThrow();

			// Example of creating an order with items

			// Create a cart
			Cart cart = cartRepository.findById(buyer.getId()).get();
			// cart.setUser(buyer); // Assuming user is already created
			cart.setTotalPrice(0.0);
			// cartRepository.save(cart);

			// Sample Orders
			Order order1 = new Order();
			order1.setUser(buyer);
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
			order2.setUser(buyer);
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
			// order2.setCart(cart);

			// Save Orders
			order1 = orderRepository.save(order1);
			order2 = orderRepository.save(order2);

			// Order Items
			Item item1 = new Item();
			item1.setOrder(order1);
			item1.setProduct(pro1);
			item1.setQuantity(2);
			item1.setUnitPrice(pro1.getPrice());
			item1.setTotalPrice(item1.getQuantity() * item1.getUnitPrice());
			item1.setCart(cart);

			Item item2 = new Item();
			item2.setOrder(order1);
			item2.setProduct(pro2);
			item2.setQuantity(1);
			item1.setUnitPrice(pro1.getPrice());
			item1.setTotalPrice(item1.getQuantity() * item1.getUnitPrice());

			Item item3 = new Item();
			item3.setOrder(order2);
			item3.setProduct(pro3);
			item3.setQuantity(1);
			item1.setUnitPrice(pro1.getPrice());
			item1.setTotalPrice(item1.getQuantity() * item1.getUnitPrice());

			// Save Order Items
			itemRepository.saveAll(List.of(item1, item2, item3));

		};
	}

	// Function to load roles from roles.json
	public void loadRoles() {
		try {
			List<String> roles = JsonLoader.loadFromJson("data/roles.json", new TypeReference<List<String>>() {
			});
			System.out.println("Loaded Roles:");
			roles.forEach(role -> {
				addRoleIfNotFound(role);
				// System.out.println("Role added: " + role);
			});
			System.out.println("All roles loaded successfully.");

		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}

	// Function to load users from users.json
	public List<User> loadUsers() {
		try {
			List<User> users = JsonLoader.loadFromJson("data/users.json", new TypeReference<List<User>>() {
			});
			System.out.println("Loaded Users:");
			users.forEach(user -> {
				addUserIfNotFound(user.getEmail(), user.getPassword(), user.getRole().getName());
				// System.out.println("User added: " + user.getEmail());
			});
			System.out.println("All users loaded successfully.");
			return users;

		} catch (Exception e) {
			System.err.println(e.getMessage());
			return List.of(); // Return an empty list in case of an error
		}
	}

	// Function to load categories from categories.json
	public List<Category> loadCategories() {
		try {
			List<Category> categoriesLoader = JsonLoader.loadFromJson("data/categories.json",
					new TypeReference<List<Category>>() {
					});
			// Save each category to the database
			categoriesLoader.forEach(category -> {
				categoryRepository.save(category);
				// System.out.println("Category saved: " + category.getName());
			});

			System.out.println("All categories loaded successfully.");
			return categoriesLoader;

		} catch (Exception e) {
			System.err.println(e.getMessage());
			return List.of(); // Return an empty list in case of an error
		}
	}

	// Function to load products from products.json
	public List<Product> loadProducts() {
		try {
			// Deserialize JSON into a List<Map<String, Object>>
			List<Map<String, Object>> rawProducts = JsonLoader.loadFromJson("data/products.json",
					new TypeReference<>() {
					});
			// Convert raw products into Product entities
			List<Product> products = rawProducts.stream().map(raw -> {
				Product product = new Product();
				product.setName((String) raw.get("name"));
				product.setDescription((String) raw.get("description"));
				product.setBrand((String) raw.get("brand"));
				product.setPrice((Double) raw.get("price"));
				product.setStock((Integer) raw.get("stock"));
				product.setSize((List<String>) raw.get("size"));
				product.setColor((List<String>) raw.get("color"));
				product.setImages((List<String>) raw.get("images"));
				product.setActive((Boolean) raw.get("active"));

				// Resolve user and category
				Long userId = Long.valueOf((Integer) raw.get("user"));
				product.setUser(userRepository.findById(userId)
						.orElseThrow(() -> new RuntimeException("User not found with ID: " + userId)));

				Long categoryId = Long.valueOf((Integer) raw.get("category"));
				product.setCategory(categoryRepository.findById(categoryId)
						.orElseThrow(() -> new RuntimeException("Category not found with ID: " + categoryId)));

				return product;
			}).toList();

			// Save products to the database
			products.forEach(productRepository::save);
			System.out.println("All products loaded successfully.");
			return products;
		} catch (Exception e) {
			System.err.println(e.getMessage());
			return List.of(); // Return an empty list in case of an error
		}
	}

	private void addRoleIfNotFound(String roleName) {
		roleRepository.findByName(roleName).orElseGet(() -> roleRepository.save(new Role(roleName)));
	}

	private User addUserIfNotFound(String email, String password, String roleName) {
		Role role = roleRepository.findByName(roleName)
				.orElseThrow(() -> new RuntimeException("Role not found"));

		User newUser = new User(email, password, role);

		// Create a cart for the user
		Cart cart = new Cart();
		cart.setUser(newUser);
		cart.setTotalPrice(0.0);
		// Set the cart in the user (bidirectional relationship)
		newUser.setCart(cart);

		return userRepository.findByEmail(email).orElseGet(() -> userRepository.save(newUser));
	}

}
