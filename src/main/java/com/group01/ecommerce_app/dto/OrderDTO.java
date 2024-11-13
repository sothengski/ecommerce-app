package com.group01.ecommerce_app.dto;

import java.time.LocalDateTime;
import java.util.List;

public class OrderDTO {

    private Long userId;
    private String orderNumber;
    private String orderStatus;
    private LocalDateTime orderDate;
    private int totalQuantity;
    private double totalPrice;
    private String currency;
    private String paymentStatus;
    private String paymentMethod;
    private String shippingAddress;
    private String shippingCity;
    private String shippingState;
    private String shippingPostalCode;
    private String shippingCountry;
    private double shippingCost;
//    private String billingAddress;
//    private String billingCity;
//    private String billingState;
//    private String billingPostalCode;
//    private String billingCountry;
    private List<OrderItemDTO> items;

    // Getters and Setters
    // You can use Lombok annotations like @Data or @Getter and @Setter here as well
}

