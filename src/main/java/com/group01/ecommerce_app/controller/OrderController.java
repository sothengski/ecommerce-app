package com.group01.ecommerce_app.controller;

import com.group01.ecommerce_app.dto.ApiResponse;
import com.group01.ecommerce_app.dto.OrderDTO;
import com.group01.ecommerce_app.model.Order;
import com.group01.ecommerce_app.model.OrderRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class OrderController {

    @Autowired
    private OrderRepository orderRepository;

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

    @PostMapping("/orders")
    public ResponseEntity<OrderDTO> createOrder(@RequestBody OrderDTO orderDTO) {
        Order order = OrderDTO.toOrderEntity(orderDTO);
        Order savedOrder = orderRepository.save(order);
        return new ResponseEntity<>(OrderDTO.convertToOrderDTO(savedOrder), HttpStatus.CREATED);
    }

    @PutMapping("/orders/{orderId}")
    public ResponseEntity<OrderDTO> updateOrder(@PathVariable Long orderId, @RequestBody OrderDTO orderDTO) {
        return orderRepository.findById(orderId).map(order -> {
            Order updatedOrder = orderRepository.save(OrderDTO.toOrderEntity(orderDTO));
            return ResponseEntity.ok(OrderDTO.convertToOrderDTO(updatedOrder));
        }).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

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
