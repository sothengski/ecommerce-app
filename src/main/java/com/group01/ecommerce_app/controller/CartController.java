package com.group01.ecommerce_app.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.group01.ecommerce_app.dto.ApiResponse;
import com.group01.ecommerce_app.dto.CartDTO;
import com.group01.ecommerce_app.dto.OrderDTO;
import com.group01.ecommerce_app.dto.OrderRequestDTO;
import com.group01.ecommerce_app.model.Cart;
import com.group01.ecommerce_app.model.CartRepository;
import com.group01.ecommerce_app.model.Order;
import com.group01.ecommerce_app.model.OrderItem;
import com.group01.ecommerce_app.model.OrderRepository;
import com.group01.ecommerce_app.model.Product;
import com.group01.ecommerce_app.model.ProductRepository;
import com.group01.ecommerce_app.model.User;
import com.group01.ecommerce_app.model.UserRepository;

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

    @GetMapping("/users/{userId}")
    public ResponseEntity<ApiResponse<CartDTO>> getCartByUser(@PathVariable("userId") Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id " + userId));

        Cart cart = user.getCart();

        if (cart == null) {
            throw new RuntimeException("Cart not found for user with id " + userId);
        }

        return ResponseEntity.ok(new ApiResponse<>(true, "Cart retrieved successfully",
                CartDTO.convertToCartDTO(cart)));
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
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Error creating category", e.getMessage()));
        }
        // CartDTO cartDTO = cartService.createCartForUser(userId);
        // return ResponseEntity.status(HttpStatus.CREATED).body(cartDTO);
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
            orderTemp.setCart(cart);

            if (orderRequesDTO.getUserId() != null) {
                Optional<User> user = userRepository.findById(orderRequesDTO.getUserId());
                if (user.isEmpty()) {
                    return new ResponseEntity<>(new ApiResponse<>(false,
                            "User not found with id "
                                    + orderRequesDTO.getUserId(),
                            "User not found"),
                            HttpStatus.BAD_REQUEST);
                }
                orderTemp.setUserId(user.get().getId());
            }
            List<OrderItem> items = orderRequesDTO.getItems().stream().map(itemDTO -> {
                Product product = productRepository.findById(itemDTO.getProductId())
                        .orElseThrow(() -> new RuntimeException("Product not found with id " + itemDTO.getProductId()));

                OrderItem item = new OrderItem();
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
                    .map(OrderItem::getTotalPrice)
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