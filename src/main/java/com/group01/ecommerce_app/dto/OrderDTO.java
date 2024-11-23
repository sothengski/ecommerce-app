package com.group01.ecommerce_app.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.group01.ecommerce_app.model.Order;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
// @AllArgsConstructor
@Data
// @Builder
public class OrderDTO {

    private Long userId;
    private Long orderId;
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

    // private String billingAddress;
    // private String billingCity;
    // private String billingState;
    // private String billingPostalCode;
    // private String billingCountry;

    private List<ItemDTO> items;

    // private Long cartId;

    // Getters and Setters
    // You can use Lombok annotations like @Data or @Getter and @Setter here as well

    // Converts Order to OrderDTO
    public static OrderDTO convertToOrderDTO(Order order) {
        OrderDTO dto = new OrderDTO();
        dto.setOrderId(order.getId());
        dto.setUserId(order.getUser().getId());
        // dto.setCartId(order.getCart() == null ? null : order.getCart().getId());
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
        dto.setItems(order.getItems().stream()
                .map(ItemDTO::convertToItemDTO) // Explicitly map each Item
                .collect(Collectors.toList()));
        // dto.setItems(order.getItems().stream().map(OrderItemDTO::toOrderItemDTO).collect(Collectors.toList()));
        return dto;
    }

    // Converts OrderDTO to Order
    // public static Order toOrderEntity(OrderDTO dto) {
    // Order order = new Order();
    // order.setUserId(dto.getUserId());
    // order.setOrderNumber(dto.getOrderNumber());
    // order.setOrderStatus(dto.getOrderStatus());
    // order.setOrderDate(dto.getOrderDate());
    // order.setTotalQuantity(dto.getTotalQuantity());
    // order.setTotalPrice(dto.getTotalPrice());
    // order.setCurrency(dto.getCurrency());
    // order.setPaymentStatus(dto.getPaymentStatus());
    // order.setPaymentMethod(dto.getPaymentMethod());
    // order.setShippingAddress(dto.getShippingAddress());
    // order.setShippingCity(dto.getShippingCity());
    // order.setShippingState(dto.getShippingState());
    // order.setShippingPostalCode(dto.getShippingPostalCode());
    // order.setShippingCountry(dto.getShippingCountry());
    // order.setShippingCost(dto.getShippingCost());
    // // order.setBillingAddress(dto.getBillingAddress());
    // // order.setBillingCity(dto.getBillingCity());
    // // order.setBillingState(dto.getBillingState());
    // // order.setBillingPostalCode(dto.getBillingPostalCode());
    // // order.setBillingCountry(dto.getBillingCountry());
    // order.setItems(dto.getItems().stream().map(ItemDTO::toOrderItemEntity).collect(Collectors.toList()));
    // return order;
    // }
}
