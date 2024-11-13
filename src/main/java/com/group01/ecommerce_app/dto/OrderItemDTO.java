package com.group01.ecommerce_app.dto;

public class OrderItemDTO {

    private Long productId;
    private String productName;
    private String sku;
    private int quantity;
    private double unitPrice;
    private double totalPrice;

    // Getters and Setters
    // You can use Lombok annotations like @Data or @Getter and @Setter here as well
}
