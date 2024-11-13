package com.group01.ecommerce_app.mapper;

import com.group01.ecommerce_app.dto.OrderDTO;
import com.group01.ecommerce_app.dto.OrderItemDTO;
import com.group01.ecommerce_app.model.Order;
import com.group01.ecommerce_app.model.OrderItem;

import java.util.List;
import java.util.stream.Collectors;

public class OrderMapper {

    // Converts Order to OrderDTO
    public static OrderDTO toOrderDTO(Order order) {
        OrderDTO dto = new OrderDTO();
        dto.setUserId(order.getUserId());
        dto.setOrderNumber(order.getOrderNumber());
        dto.setOrderStatus(order.getOrderStatus());
        dto.setOrderDate(order.getOrderDate());
        dto.setTotalQuantity(order.getTotalQuantity());
        dto.setTotalPrice(order.getTotalPrice());
        dto.setCurrency(order.getCurrency());
        dto.setPaymentStatus(order.getPaymentStatus());
        dto.setPaymentMethod(order.getPaymentMethod());
        dto.setShippingAddress(order.getShippingAddress());
        dto.setShippingCity(order.getShippingCity());
        dto.setShippingState(order.getShippingState());
        dto.setShippingPostalCode(order.getShippingPostalCode());
        dto.setShippingCountry(order.getShippingCountry());
        dto.setShippingCost(order.getShippingCost());
        // dto.setBillingAddress(order.getBillingAddress());
        // dto.setBillingCity(order.getBillingCity());
        // dto.setBillingState(order.getBillingState());
        // dto.setBillingPostalCode(order.getBillingPostalCode());
        // dto.setBillingCountry(order.getBillingCountry());
        dto.setItems(order.getItems().stream().map(OrderMapper::toOrderItemDTO).collect(Collectors.toList()));
        return dto;
    }

    // Converts OrderDTO to Order
    public static Order toOrderEntity(OrderDTO dto) {
        Order order = new Order();
        order.setUserId(dto.getUserId());
        order.setOrderNumber(dto.getOrderNumber());
        order.setOrderStatus(dto.getOrderStatus());
        order.setOrderDate(dto.getOrderDate());
        order.setTotalQuantity(dto.getTotalQuantity());
        order.setTotalPrice(dto.getTotalPrice());
        order.setCurrency(dto.getCurrency());
        order.setPaymentStatus(dto.getPaymentStatus());
        order.setPaymentMethod(dto.getPaymentMethod());
        order.setShippingAddress(dto.getShippingAddress());
        order.setShippingCity(dto.getShippingCity());
        order.setShippingState(dto.getShippingState());
        order.setShippingPostalCode(dto.getShippingPostalCode());
        order.setShippingCountry(dto.getShippingCountry());
        order.setShippingCost(dto.getShippingCost());
        // order.setBillingAddress(dto.getBillingAddress());
        // order.setBillingCity(dto.getBillingCity());
        // order.setBillingState(dto.getBillingState());
        // order.setBillingPostalCode(dto.getBillingPostalCode());
        // order.setBillingCountry(dto.getBillingCountry());
        order.setItems(dto.getItems().stream().map(OrderMapper::toOrderItemEntity).collect(Collectors.toList()));
        return order;
    }

    // Converts OrderItem to OrderItemDTO
    public static OrderItemDTO toOrderItemDTO(OrderItem orderItem) {
        OrderItemDTO dto = new OrderItemDTO();
        // dto.setProductId(orderItem.getProductId());
        // dto.setProductName(orderItem.getProductName());
        // dto.setSku(orderItem.getSku());
        // dto.setQuantity(orderItem.getQuantity());
        // dto.setUnitPrice(orderItem.getUnitPrice());
        // dto.setTotalPrice(orderItem.getTotalPrice());
        return dto;
    }

    // Converts OrderItemDTO to OrderItem
    public static OrderItem toOrderItemEntity(OrderItemDTO dto) {
        OrderItem item = new OrderItem();
        // item.setProductId(dto.getProductId());
        // item.setProductName(dto.getProductName());
        // item.setSku(dto.getSku());
        // item.setQuantity(dto.getQuantity());
        // item.setUnitPrice(dto.getUnitPrice());
        // item.setTotalPrice(dto.getTotalPrice());
        return item;
    }
}
