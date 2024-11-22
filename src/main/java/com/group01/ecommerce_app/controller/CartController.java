package com.group01.ecommerce_app.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.group01.ecommerce_app.dto.ApiResponse;
import com.group01.ecommerce_app.dto.CartDTO;
import com.group01.ecommerce_app.dto.ItemRequestDTO;
import com.group01.ecommerce_app.dto.OrderDTO;
import com.group01.ecommerce_app.dto.OrderRequestDTO;
import com.group01.ecommerce_app.model.Cart;
import com.group01.ecommerce_app.model.CartRepository;
import com.group01.ecommerce_app.model.Item;
import com.group01.ecommerce_app.model.ItemRepository;
import com.group01.ecommerce_app.model.Order;
import com.group01.ecommerce_app.model.OrderRepository;
import com.group01.ecommerce_app.model.Product;
import com.group01.ecommerce_app.model.ProductRepository;
import com.group01.ecommerce_app.model.User;
import com.group01.ecommerce_app.model.UserRepository;

@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api/carts")
public class CartController {

        @Autowired
        private CartRepository cartRepository;

        @Autowired
        private ProductRepository productRepository;

        @Autowired
        private UserRepository userRepository;

        @Autowired
        private OrderRepository orderRepository;

        @Autowired
        private ItemRepository itemRepository;

        @GetMapping("/{cartId}")
        public ResponseEntity<ApiResponse<CartDTO>> getCartById(@PathVariable("cartId") Long cartId) {

                // User user = userRepository.findById(userId)
                // .orElseThrow(() -> new RuntimeException("User not found with id " + userId));

                Optional<Cart> cartData = cartRepository.findById(cartId);

                if (cartData.isPresent()) {
                        Cart cartTemp = cartData.get();
                        return new ResponseEntity<>(new ApiResponse<>(true, "Cart retrieved successfully",
                                        CartDTO.convertToCartDTO(
                                                        cartTemp)),
                                        HttpStatus.OK);
                }
                return new ResponseEntity<>(
                                new ApiResponse<>(false, "Cart with id " + cartId + " does not exist",
                                                "Cart not found"),
                                HttpStatus.NOT_FOUND);
        }

        @GetMapping("/users/{userId}")
        public ResponseEntity<ApiResponse<CartDTO>> getCartByUserId(@PathVariable("userId") Long userId) {

                Optional<Cart> cartData = cartRepository.findByUserId(userId);

                if (cartData.isPresent()) {
                        Cart cartTemp = cartData.get();
                        return new ResponseEntity<>(
                                        new ApiResponse<>(true, "Cart retrieved successfully",
                                                        CartDTO.convertToCartDTO(cartTemp)),
                                        HttpStatus.OK);
                }
                return new ResponseEntity<>(
                                new ApiResponse<>(false, "Cart for user with id " + userId + " does not exist",
                                                "Cart not found"),
                                HttpStatus.NOT_FOUND);
        }

        @PostMapping("/users/{userId}")
        public ResponseEntity<ApiResponse<CartDTO>> createCartForUser(@PathVariable Long userId) {
                try {
                        // Check if user exists
                        User user = userRepository.findById(userId)
                                        .orElseThrow(() -> new RuntimeException("User not found with id " + userId));

                        // Check if cart already exists
                        if (user.getCart() != null) {
                                throw new RuntimeException("Cart already exists for user id " + userId);
                        }

                        // Create new cart
                        Cart cart = new Cart();
                        cart.setUser(user);
                        // cart.setItems(new ArrayList<>());
                        // cart.setTotalPrice(BigDecimal.ZERO);

                        // Save cart (cascades to user)
                        cartRepository.save(cart);

                        return new ResponseEntity<>(new ApiResponse<>(true, "Product created successfully",
                                        CartDTO.convertToCartDTO(cart)), HttpStatus.CREATED);
                } catch (RuntimeException e) {
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                        .body(new ApiResponse<>(false, "Error creating category", e.getMessage()));
                }
                // CartDTO cartDTO = cartService.createCartForUser(userId);
                // return ResponseEntity.status(HttpStatus.CREATED).body(cartDTO);
        }

        @PostMapping("/{cartId}/add-item")
        public ResponseEntity<ApiResponse<CartDTO>> addItemToCart(
                        @PathVariable("cartId") Long cartId,
                        // @RequestParam("productId") Long productId,
                        // @RequestParam("quantity") int quantity
                        @RequestBody ItemRequestDTO itemRequesDTO) {
                try {
                        // CartDTO updatedCart = cartService.addProductToCart(cartId, productId,
                        // quantity);

                        // Fetch the cart by ID
                        Cart cart = cartRepository.findById(cartId)
                                        .orElseThrow(() -> new RuntimeException(
                                                        "Cart with ID " + cartId + " not found"));

                        // Fetch the product by ID
                        Product product = productRepository.findById(
                                        itemRequesDTO.getProductId())
                                        .orElseThrow(() -> new RuntimeException(
                                                        "Product with ID " + itemRequesDTO.getProductId()
                                                                        + " not found"));

                        // Check if the product already exists in the cart
                        Optional<Item> existingItem = cart.getItems().stream()
                                        .filter(item -> item.getProduct().getId().equals(itemRequesDTO
                                                        .getProductId()))
                                        .findFirst();

                        if (existingItem.isPresent()) {
                                // Update the existing item
                                Item item = existingItem.get();
                                item.setUnitPrice(product.getPrice()); // Assuming `Product` has a `price` field
                                item.setQuantity(item.getQuantity() + itemRequesDTO.getQuantity());
                                item.setTotalPrice(item.getQuantity() * item.getUnitPrice());
                        } else {
                                // Add a new item to the cart
                                Item newItem = new Item();
                                newItem.setProduct(product);
                                newItem.setQuantity(itemRequesDTO.getQuantity());
                                newItem.setUnitPrice(product.getPrice()); // Assuming `Product` has a `price` field
                                newItem.setTotalPrice(itemRequesDTO.getQuantity() * product.getPrice());
                                newItem.setCart(cart);

                                cart.getItems().add(newItem);
                        }

                        // Update the cart's total quantity and price
                        int totalQuantity = cart.getItems().stream().mapToInt(Item::getQuantity).sum();
                        double totalPrice = cart.getItems().stream().mapToDouble(Item::getTotalPrice).sum();

                        cart.setQuantity(totalQuantity);
                        cart.setTotalPrice(totalPrice);

                        // Save the updated cart
                        Cart updatedCart = cartRepository.save(cart);

                        return new ResponseEntity<>(
                                        new ApiResponse<>(true, "Product added to cart successfully",
                                                        CartDTO.convertToCartDTO(updatedCart)),
                                        HttpStatus.OK);
                } catch (RuntimeException e) {
                        return new ResponseEntity<>(
                                        new ApiResponse<>(false, e.getMessage(), null),
                                        HttpStatus.BAD_REQUEST);
                }
        }

        @PutMapping("/{cartId}/update-item")
        public ResponseEntity<ApiResponse<CartDTO>> updateItemInCart(
                        @PathVariable("cartId") Long cartId,
                        // @PathVariable("itemId") Long itemId,
                        @RequestParam("itemId") Long itemId,
                        @RequestParam("newQuantity") int newQuantity
        // @RequestBody ItemRequestDTO itemRequesDTO
        ) {
                try {
                        // CartDTO updatedCart = cartService.updateItemInCart(cartId, itemId,
                        // newQuantity);

                        if (newQuantity <= 0) {
                                throw new RuntimeException("Quantity must be greater than zero");
                        }

                        // Fetch the cart by ID
                        Cart cart = cartRepository.findById(cartId)
                                        .orElseThrow(() -> new RuntimeException(
                                                        "Cart with ID " + cartId + " not found"));

                        // Find the item to be updated
                        Item itemToUpdate = cart.getItems().stream()
                                        .filter(item -> item.getId().equals(
                                                        itemId))
                                        .findFirst()
                                        .orElseThrow(() -> new RuntimeException(
                                                        "Item with ID " + itemId
                                                                        + " not found in cart"));

                        // Update the item's quantity and total price
                        itemToUpdate.setQuantity(newQuantity);
                        itemToUpdate.setTotalPrice(newQuantity * itemToUpdate.getUnitPrice());

                        // Recalculate the cart's total quantity and price
                        int totalQuantity = cart.getItems().stream().mapToInt(Item::getQuantity).sum();
                        double totalPrice = cart.getItems().stream().mapToDouble(Item::getTotalPrice).sum();

                        cart.setQuantity(totalQuantity);
                        cart.setTotalPrice(totalPrice);

                        // Save the updated cart
                        Cart updatedCart = cartRepository.save(cart);

                        // Convert to DTO and return
                        return new ResponseEntity<>(
                                        new ApiResponse<>(true, "Item updated in cart successfully",
                                                        CartDTO.convertToCartDTO(
                                                                        updatedCart)),
                                        HttpStatus.OK);
                } catch (RuntimeException e) {
                        return new ResponseEntity<>(
                                        new ApiResponse<>(false, e.getMessage(), null),
                                        HttpStatus.BAD_REQUEST);
                }
        }

        @DeleteMapping("/{cartId}/remove-item")
        public ResponseEntity<ApiResponse<CartDTO>> removeItemFromCart(
                        @PathVariable("cartId") Long cartId,
                        @RequestParam("itemId") Long itemId) {
                try {
                        // CartDTO updatedCart = cartService.removeItemFromCart(cartId, itemId);

                        // Fetch the cart by ID
                        Cart cart = cartRepository.findById(cartId)
                                        .orElseThrow(() -> new RuntimeException(
                                                        "Cart with ID " + cartId + " not found"));

                        // Find the item to be removed
                        Item itemToRemove = cart.getItems().stream()
                                        .filter(item -> item.getId().equals(itemId))
                                        .findFirst()
                                        .orElseThrow(() -> new RuntimeException(
                                                        "Item with ID " + itemId + " not found in cart"));

                        // Remove the item from the cart
                        cart.getItems().remove(itemToRemove);

                        // Recalculate the cart's total quantity and price
                        int totalQuantity = cart.getItems().stream().mapToInt(Item::getQuantity).sum();
                        double totalPrice = cart.getItems().stream().mapToDouble(Item::getTotalPrice).sum();

                        cart.setQuantity(totalQuantity);
                        cart.setTotalPrice(totalPrice);

                        // Save the updated cart and delete the item
                        cartRepository.save(cart);
                        itemRepository.delete(itemToRemove);

                        return new ResponseEntity<>(
                                        new ApiResponse<>(true, "Item removed from cart successfully",
                                                        CartDTO.convertToCartDTO(
                                                                        cart)),
                                        HttpStatus.OK);
                } catch (RuntimeException e) {
                        return new ResponseEntity<>(
                                        new ApiResponse<>(false, e.getMessage(), null),
                                        HttpStatus.BAD_REQUEST);
                }
        }

        @PostMapping("/{cartId}/checkout")
        public ResponseEntity<ApiResponse<OrderDTO>> checkoutCart(@PathVariable("cartId") Long cartId) {
                try {
                        // OrderDTO order = cartService.checkoutCart(cartId);

                        // Fetch the cart by ID
                        Cart cart = cartRepository.findById(cartId)
                                        .orElseThrow(() -> new RuntimeException(
                                                        "Cart with ID " + cartId + " not found"));

                        // Ensure the cart has items
                        if (cart.getItems().isEmpty()) {
                                throw new RuntimeException("Cart is empty. Add items to the cart before checkout.");
                        }

                        // Create a new order
                        Order order = new Order();
                        order.setUser(cart.getUser());
                        order.setOrderDate(order.getCreatedAt());
                        order.setTotalPrice(cart.getTotalPrice());
                        order.setItems(new ArrayList<>());

                        // Transfer cart items to order items
                        for (Item cartItem : cart.getItems()) {
                                Item orderItem = new Item();
                                orderItem.setProduct(cartItem.getProduct());
                                orderItem.setQuantity(cartItem.getQuantity());
                                orderItem.setUnitPrice(cartItem.getUnitPrice());
                                orderItem.setTotalPrice(cartItem.getTotalPrice());
                                orderItem.setOrder(order);

                                // Add the order item to the order
                                order.getItems().add(orderItem);
                        }

                        // Save the order
                        Order savedOrder = orderRepository.save(order);

                        // Clear the cart
                        cart.getItems().clear();
                        cart.setQuantity(0);
                        cart.setTotalPrice(0.0);
                        cartRepository.save(cart);

                        // Convert to DTO and return
                        return new ResponseEntity<>(
                                        new ApiResponse<>(true, "Cart checked out successfully",
                                                        OrderDTO.convertToOrderDTO(
                                                                        savedOrder)),
                                        HttpStatus.OK);
                } catch (RuntimeException e) {
                        return new ResponseEntity<>(
                                        new ApiResponse<>(false, e.getMessage(), null),
                                        HttpStatus.BAD_REQUEST);
                }
        }

        @PostMapping("/{cartId}/orders")
        public ResponseEntity<ApiResponse<OrderDTO>> addOrderToCart(@PathVariable Long cartId,
                        @RequestBody OrderRequestDTO orderRequesDTO) {
                try {
                        // Retrieve the cart
                        Cart cart = new Cart();
                        if (cartId != null) {
                                Optional<Cart> cartTemp = cartRepository.findById(cartId);
                                if (cartTemp.isEmpty()) {

                                        return new ResponseEntity<>(new ApiResponse<>(false,
                                                        "Cart not found with id "
                                                                        + cartId,
                                                        "Cart not found"),
                                                        HttpStatus.BAD_REQUEST);
                                }
                                cart = cartTemp.get();
                        }

                        // Convert DTO to Order entity
                        Order orderTemp = new Order();

                        // Associate the order with the cart
                        // orderTemp.setCart(cart);

                        if (orderRequesDTO.getUserId() != null) {
                                Optional<User> user = userRepository.findById(orderRequesDTO.getUserId());
                                if (user.isEmpty()) {
                                        return new ResponseEntity<>(new ApiResponse<>(false,
                                                        "User not found with id "
                                                                        + orderRequesDTO.getUserId(),
                                                        "User not found"),
                                                        HttpStatus.BAD_REQUEST);
                                }
                                orderTemp.setUser(user.get());
                        }
                        List<Item> items = orderRequesDTO.getItems().stream().map(itemDTO -> {
                                Product product = productRepository.findById(itemDTO.getProductId())
                                                .orElseThrow(() -> new RuntimeException(
                                                                "Product not found with id " + itemDTO.getProductId()));

                                Item item = new Item();
                                item.setOrder(orderTemp);
                                item.setProduct(product);
                                item.setQuantity(itemDTO.getQuantity());
                                item.setUnitPrice(product.getPrice());
                                item.setTotalPrice(product.getPrice() * itemDTO.getQuantity());
                                return item;
                        })
                                        .collect(Collectors.toList());

                        orderTemp.setItems(items);

                        orderTemp.setOrderNumber(orderRequesDTO.getOrderNumber());
                        orderTemp.setOrderStatus(orderRequesDTO.getOrderStatus());
                        orderTemp.setOrderDate(orderRequesDTO.getOrderDate());
                        orderTemp.setTotalQuantity(orderRequesDTO.getTotalQuantity());

                        // Calculate and set total price for the order
                        Double totalPrice = items.stream()
                                        .map(Item::getTotalPrice)
                                        .reduce(0.0, Double::sum);
                        orderTemp.setTotalPrice(totalPrice);

                        // orderTemp.setTotalPrice(orderRequesDTO.getTotalPrice());
                        orderTemp.setCurrency(orderRequesDTO.getCurrency());
                        orderTemp.setPaymentStatus(orderRequesDTO.getPaymentStatus());
                        orderTemp.setPaymentMethod(orderRequesDTO.getPaymentMethod());
                        orderTemp.setShippingAddress(orderRequesDTO.getShippingAddress());
                        orderTemp.setShippingCity(orderRequesDTO.getShippingCity());
                        orderTemp.setShippingState(orderRequesDTO.getShippingState());
                        orderTemp.setShippingPostalCode(orderRequesDTO.getShippingPostalCode());
                        orderTemp.setShippingCountry(orderRequesDTO.getShippingCountry());
                        orderTemp.setShippingCost(orderRequesDTO.getShippingCost());

                        // Save the order
                        Order savedOrder = orderRepository.save(orderTemp);
                        return new ResponseEntity<>(new ApiResponse<>(true, "Cart created successfully",
                                        OrderDTO.convertToOrderDTO(savedOrder)), HttpStatus.CREATED);
                } catch (RuntimeException e) {
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                        .body(new ApiResponse<>(false, "Error creating cart", e.getMessage()));
                }
        }
}