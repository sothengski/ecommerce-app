package com.group01.ecommerce_app.controller;

import com.group01.ecommerce_app.dto.OrderDTO;
import com.group01.ecommerce_app.mapper.OrderMapper;
import com.group01.ecommerce_app.model.Order;
import com.group01.ecommerce_app.model.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class OrderController {

    @Autowired
    private OrderRepository orderRepository;

    @GetMapping("/orders")
    public ResponseEntity<List<OrderDTO>> getAllOrders() {
        List<Order> orders = orderRepository.findAll();
        List<OrderDTO> orderDTOs = orders.stream().map(OrderMapper::toOrderDTO).collect(Collectors.toList());
        return orderDTOs.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(orderDTOs);
    }

    @GetMapping("/orders/{orderId}")
    public ResponseEntity<OrderDTO> getOrderById(@PathVariable Long orderId) {
        return orderRepository.findById(orderId)
                .map(order -> ResponseEntity.ok(OrderMapper.toOrderDTO(order)))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/orders")
    public ResponseEntity<OrderDTO> createOrder(@RequestBody OrderDTO orderDTO) {
        Order order = OrderMapper.toOrderEntity(orderDTO);
        Order savedOrder = orderRepository.save(order);
        return new ResponseEntity<>(OrderMapper.toOrderDTO(savedOrder), HttpStatus.CREATED);
    }

    @PutMapping("/orders/{orderId}")
    public ResponseEntity<OrderDTO> updateOrder(@PathVariable Long orderId, @RequestBody OrderDTO orderDTO) {
        return orderRepository.findById(orderId).map(order -> {
            Order updatedOrder = orderRepository.save(OrderMapper.toOrderEntity(orderDTO));
            return ResponseEntity.ok(OrderMapper.toOrderDTO(updatedOrder));
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
