package com.group01.ecommerce_app.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.group01.ecommerce_app.dto.ApiResponse;
import com.group01.ecommerce_app.dto.OrderDTO;
import com.group01.ecommerce_app.dto.OrderRequestDTO;
import com.group01.ecommerce_app.model.Item;
import com.group01.ecommerce_app.model.Order;
import com.group01.ecommerce_app.model.OrderRepository;
import com.group01.ecommerce_app.model.Product;
import com.group01.ecommerce_app.model.ProductRepository;
import com.group01.ecommerce_app.model.User;
import com.group01.ecommerce_app.model.UserRepository;

@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api")
public class OrderController {

    // @Autowired
    private final OrderRepository orderRepository;

    // @Autowired
    private final UserRepository userRepository;

    // @Autowired
    private final ProductRepository productRepository;

    public OrderController(
            UserRepository userRepository, OrderRepository orderRepository, ProductRepository productRepository) {
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }

    @GetMapping("/orders")
    public ResponseEntity<ApiResponse<List<OrderDTO>>> getAllOrders(@RequestParam(required = false) String name) {
        try {
            List<Order> orders = orderRepository.findAll();
            List<OrderDTO> orderDTOs = new ArrayList<>();
            for (Order order : orders) {
                orderDTOs.add(OrderDTO.convertToOrderDTO(order));
            }

            // List of products is Empty
            if (orders.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } // List of products have the data
            return new ResponseEntity<>(new ApiResponse<>(true, "Order retrieved successfully",
                    orderDTOs), HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Error getting all order data", e.getMessage()));
        }
    }

    @GetMapping("/orders/{orderId}")
    public ResponseEntity<ApiResponse<OrderDTO>> getOrderById(@PathVariable("orderId") Long orderId) {
        try {
            Optional<Order> orderData = orderRepository.findById(orderId);
            if (orderData.isPresent()) {
                return new ResponseEntity<>(new ApiResponse<>(true, "Order retrieved successfully",
                        OrderDTO.convertToOrderDTO(orderData.get())), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(
                        new ApiResponse<>(false, "Order with id " + orderId + " does not exist", "Order is not found"),
                        HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Error getting an order data", e.getMessage()));
        }
    }

    @GetMapping("/users/{userId}/orders")
    public ResponseEntity<ApiResponse<List<OrderDTO>>> getOrdersByUserId(@PathVariable("userId") Long userId) {
        try {
            // Fetch orders by user ID
            List<Order> orders = orderRepository.findByUser_Id(userId);

            // Convert orders to DTOs
            List<OrderDTO> orderDTOs = orders.stream()
                    .map(OrderDTO::convertToOrderDTO)
                    .toList();

            // Check if the list is empty
            if (orders.isEmpty()) {
                return new ResponseEntity<>(new ApiResponse<>(true, "Orders retrieved successfully",
                        orderDTOs, "No orders found"), HttpStatus.OK);
            }

            return new ResponseEntity<>(
                    new ApiResponse<>(true, "Orders retrieved successfully", orderDTOs),
                    HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Error getting orders for user", e.getMessage()));
        }
    }

    @PostMapping("/orders")
    public ResponseEntity<ApiResponse<OrderDTO>> createOrder(@RequestBody OrderRequestDTO orderRequesDTO) {
        try {
            // Convert DTO to Order entity
            Order orderTemp = new Order();

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
                        .orElseThrow(() -> new RuntimeException("Product not found with id " + itemDTO.getProductId()));

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

            orderTemp.setOrderNumber(Order.generateOrderNumber(orderRequesDTO.getUserId(), false));
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
            // orderTemp.setShippingCity(orderRequesDTO.getShippingCity());
            // orderTemp.setShippingState(orderRequesDTO.getShippingState());
            // orderTemp.setShippingPostalCode(orderRequesDTO.getShippingPostalCode());
            // orderTemp.setShippingCountry(orderRequesDTO.getShippingCountry());
            orderTemp.setShippingCost(orderRequesDTO.getShippingCost());

            Order savedOrder = orderRepository.save(orderTemp);

            // Convert to DTO for response
            OrderDTO responseDto = OrderDTO.convertToOrderDTO(savedOrder);

            return new ResponseEntity<>(new ApiResponse<>(true, "Product created successfully",
                    responseDto), HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Error creating order", e.getMessage()));
        }
    }

    // @PostMapping("/orders")
    // public ResponseEntity<OrderDTO> createOrder(@RequestBody OrderDTO orderDTO) {
    // Order order = OrderDTO.toOrderEntity(orderDTO);
    // Order savedOrder = orderRepository.save(order);
    // return new ResponseEntity<>(OrderDTO.convertToOrderDTO(savedOrder),
    // HttpStatus.CREATED);
    // }

    @PutMapping("/orders/{id}")
    public ResponseEntity<ApiResponse<OrderDTO>> updateOrder(
            @PathVariable("id") long id,
            @RequestBody OrderRequestDTO orderRequestDTO) {
        try {
            // Find the existing order by ID
            Optional<Order> orderData = orderRepository.findById(id);
            if (orderData.isPresent()) {
                Order existingOrder = orderData.get();

                // Update user ID if provided
                if (orderRequestDTO.getUserId() != null) {
                    Optional<User> user = userRepository.findById(orderRequestDTO.getUserId());
                    if (user.isEmpty()) {
                        return new ResponseEntity<>(new ApiResponse<>(false,
                                "User not found with id " + orderRequestDTO.getUserId(),
                                "User not found"),
                                HttpStatus.BAD_REQUEST);
                    }
                    existingOrder.setUser(user.get());
                }

                // Update items
                if (orderRequestDTO.getItems() != null) {
                    List<Item> items = orderRequestDTO.getItems().stream().map(itemDTO -> {
                        Product product = productRepository.findById(itemDTO.getProductId())
                                .orElseThrow(() -> new RuntimeException(
                                        "Product not found with id " + itemDTO.getProductId()));

                        Item item = new Item();
                        item.setOrder(existingOrder);
                        item.setProduct(product);
                        item.setQuantity(itemDTO.getQuantity());
                        item.setUnitPrice(product.getPrice());
                        item.setTotalPrice(product.getPrice() * itemDTO.getQuantity());
                        return item;
                    }).collect(Collectors.toList());
                    existingOrder.setItems(items);
                }

                // Update basic order details
                existingOrder.setOrderNumber(orderRequestDTO.getOrderNumber());
                existingOrder.setOrderStatus(orderRequestDTO.getOrderStatus());
                existingOrder.setOrderDate(orderRequestDTO.getOrderDate());
                existingOrder.setTotalQuantity(orderRequestDTO.getTotalQuantity());

                // Calculate and update total price
                if (orderRequestDTO.getItems() != null) {
                    Double totalPrice = existingOrder.getItems().stream()
                            .map(Item::getTotalPrice)
                            .reduce(0.0, Double::sum);
                    existingOrder.setTotalPrice(totalPrice);
                }

                existingOrder.setCurrency(orderRequestDTO.getCurrency());
                existingOrder.setPaymentStatus(orderRequestDTO.getPaymentStatus());
                existingOrder.setPaymentMethod(orderRequestDTO.getPaymentMethod());
                existingOrder.setShippingAddress(orderRequestDTO.getShippingAddress());
                // existingOrder.setShippingCity(orderRequestDTO.getShippingCity());
                // existingOrder.setShippingState(orderRequestDTO.getShippingState());
                // existingOrder.setShippingPostalCode(orderRequestDTO.getShippingPostalCode());
                // existingOrder.setShippingCountry(orderRequestDTO.getShippingCountry());
                existingOrder.setShippingCost(orderRequestDTO.getShippingCost());

                // Save the updated order to the database
                Order updatedOrder = orderRepository.save(existingOrder);

                // Convert to DTO for response
                OrderDTO responseDto = OrderDTO.convertToOrderDTO(updatedOrder);

                return new ResponseEntity<>(new ApiResponse<>(true, "Order updated successfully",
                        responseDto), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(
                        new ApiResponse<>(false, "Order with id " + id + " does not exist", "Order not found"),
                        HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Error updating order", e.getMessage()));
        }
    }

    // @PutMapping("/orders/{orderId}")
    // public ResponseEntity<OrderDTO> updateOrder(@PathVariable Long orderId,
    // @RequestBody OrderDTO orderDTO) {
    // return orderRepository.findById(orderId).map(order -> {
    // Order updatedOrder = orderRepository.save(OrderDTO.toOrderEntity(orderDTO));
    // return ResponseEntity.ok(OrderDTO.convertToOrderDTO(updatedOrder));
    // }).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    // }

    @DeleteMapping("/orders/{orderId}")
    public ResponseEntity<HttpStatus> deleteOrder(@PathVariable Long orderId) {
        try {
            orderRepository.deleteById(orderId);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

/*
 * 
 * ==================================================================
 * 
 * Get Order Summary (Cart View)
 * 
 * Endpoint: GET /api/orders/{orderId}
 * 
 * Description: Retrieves the order summary, including items in the cart, total
 * price, tax, and estimated shipping costs.
 * ==================================================================
 * 
 * Add Item to Cart
 * 
 * Endpoint: POST /api/orders/{orderId}/items
 * 
 * Description: Adds an item to the cart within an order by orderId.
 * ==================================================================
 * 
 * Update Item Quantity in Order
 * 
 * Endpoint: PUT /api/orders/{orderId}/items/{item_id}
 * 
 * Description: Updates the quantity of a specific item in the order.
 * ==================================================================
 * 
 * Remove Item from Order
 * 
 * Endpoint: DELETE /api/order/{orderId}/items/{item_id}
 * 
 * Description: Removes a specific item from the order.
 * ==================================================================
 * 
 * Clear Order (Empty Cart)
 * 
 * Endpoint: DELETE /api/order/{orderId}
 * 
 * Description: Clears all items from the order.
 * ==================================================================
 * 
 * Update Shipping Option
 * 
 * Endpoint: PUT /api/orders/{orderId}/shipping
 * 
 * Description: Allows the user to select or update a shipping option for the
 * order.
 * ==================================================================
 * 
 * Update Payment Method
 * 
 * Endpoint: PUT /api/orders/{orderId}/payment
 * 
 * Description: Allows the user to select or update a payment method for the
 * order.
 * ==================================================================
 * 
 * Submit Order
 * 
 * Endpoint: PUT /api/orders/{orderId}/submit
 * 
 * Description: Submits the order for processing and finalizes the purchase.
 * ==================================================================
 */
