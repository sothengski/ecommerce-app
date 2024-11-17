package com.group01.ecommerce_app.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.group01.ecommerce_app.dto.ApiResponse;
import com.group01.ecommerce_app.dto.CartDTO;
import com.group01.ecommerce_app.dto.ItemDTO;
import com.group01.ecommerce_app.model.Cart;
import com.group01.ecommerce_app.model.CartRepository;
import com.group01.ecommerce_app.model.Item;
import com.group01.ecommerce_app.model.Product;
import com.group01.ecommerce_app.model.ProductRepository;
import com.group01.ecommerce_app.model.User;
import com.group01.ecommerce_app.model.UserRepository;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/user/{userId}")
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

    @PostMapping("/user/{userId}")
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
            cart.setItems(new ArrayList<>());
            cart.setTotalPrice(BigDecimal.ZERO);

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

    @PutMapping("/{cartId}/add-item")
    public ResponseEntity<ApiResponse<CartDTO>> addItemToCart(@PathVariable("cartId") Long cartId,
            @RequestBody ItemDTO cartItemDTO) {
        try {
            Cart cart = new Cart();
            // .orElseThrow(() -> new RuntimeException("Cart not found"));
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
            Product product = new Product();
            // productRepository.findById(cartItemDTO.getProductId())
            // .orElseThrow(() -> new RuntimeException("Product not found"));

            if (cartItemDTO.getProductId() != null) {
                Optional<Product> productTemp = productRepository.findById(cartItemDTO.getProductId());
                if (productTemp.isEmpty()) {
                    return new ResponseEntity<>(new ApiResponse<>(false,
                            "Product not found with id "
                                    + cartItemDTO
                                            .getProductId(),
                            "Product not found"),
                            HttpStatus.BAD_REQUEST);
                }
                product = productTemp.get();

            }

            // Find existing cart item for the same product
            Optional<Item> existingItem = Optional.ofNullable(cart.getItems())
                    .orElse(new ArrayList<>()) // Handle null items list
                    .stream()
                    .filter(item -> item.getProduct().getId().equals(cartItemDTO.getProductId()))
                    .findFirst();

            if (existingItem.isPresent()) {
                // Update the existing item's quantity and total price
                Item item = existingItem.get();
                item.setQuantity(item.getQuantity() + cartItemDTO.getQuantity());
                item.setTotalPrice(
                        BigDecimal.valueOf(product.getPrice()).multiply(BigDecimal.valueOf(item.getQuantity())));
            } else {
                // Add a new item to the cart
                Item newItem = new Item();
                newItem.setCart(cart);
                newItem.setProduct(product);
                newItem.setQuantity(cartItemDTO.getQuantity());
                newItem.setUnitPrice(BigDecimal.valueOf(product.getPrice()));
                newItem.setTotalPrice(
                        BigDecimal.valueOf(product.getPrice()).multiply(BigDecimal.valueOf(cartItemDTO.getQuantity())));
                cart.getItems().add(newItem);
            }

            // Update cart total price
            // BigDecimal totalPrice = cart.getItems().stream()
            // .map(Item::getTotalPrice)
            // .reduce(BigDecimal.ZERO, BigDecimal::add);

            BigDecimal totalPrice = BigDecimal.valueOf(100.0);
            cart.setTotalPrice(totalPrice);

            Cart updatedCart = cartRepository.save(cart);

            return new ResponseEntity<>(new ApiResponse<>(true, "Items added successfully",
                    CartDTO.convertToCartDTO(updatedCart)),
                    HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Error adding items into cart", e.getMessage()));
        }
    }

}