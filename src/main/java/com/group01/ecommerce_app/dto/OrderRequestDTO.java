package com.group01.ecommerce_app.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class OrderRequestDTO {
    private Long userId;
    private String orderNumber;
    private String orderStatus;
    private LocalDateTime orderDate;

    // private int totalQuantity;
    // private double totalPrice;

    private String currency;
    private String paymentStatus;
    private String paymentMethod;

    private String shippingAddress;
    private String shippingCity;
    private String shippingState;
    private String shippingPostalCode;
    private String shippingCountry;
    private double shippingCost;

    // private String billingAddress;
    // private String billingCity;
    // private String billingState;
    // private String billingPostalCode;
    // private String billingCountry;

    private List<ItemRequestDTO> items;

    // public Double getTotalPrice() {
    // if (items == null || items.isEmpty()) {
    // return shippingCost;
    // }
    // Double itemsTotal = items.stream()
    // .mapToDouble(
    // OrderItemRequestDTO::getTotalPrice)
    // .reduce(0.0, Double::sum); // Sum all the TotalPrice values
    // return itemsTotal + shippingCost;
    // }
    public Double getTotalPrice() {

        return items.stream()
                .mapToDouble(
                        ItemRequestDTO::getTotalPrice)
                .reduce(0.0, Double::sum); // Sum all the TotalPrice values
    }

    public Integer getTotalQuantity() {
        if (items == null || items.isEmpty()) {
            return 0;
        }
        return items.stream()
                .mapToInt(
                        ItemRequestDTO::getQuantity)
                .sum();
    }
}
