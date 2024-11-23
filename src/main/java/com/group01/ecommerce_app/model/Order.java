package com.group01.ecommerce_app.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entity
@Table(name = "orders")
public class Order implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // @Column(name = "user_id", nullable = false)
    // private Long userId;

    @Column(name = "order_number", nullable = false, unique = true)
    private String orderNumber;

    @Column(name = "order_status", nullable = false)
    private String orderStatus;

    @Column(name = "order_date", nullable = false)
    private LocalDateTime orderDate;

    @Column(name = "total_quantity", nullable = false)
    private int totalQuantity;

    @Column(name = "total_price", nullable = false)
    private double totalPrice;

    @Column(name = "currency", nullable = false)
    private String currency;

    @Column(name = "payment_status", nullable = false)
    private String paymentStatus;

    @Column(name = "payment_method", nullable = false)
    private String paymentMethod;

    @Column(name = "shipping_address", nullable = false)
    private String shippingAddress;

    // @Column(name = "shipping_city", nullable = false)
    // private String shippingCity;

    // @Column(name = "shipping_state", nullable = false)
    // private String shippingState;

    // @Column(name = "shipping_postal_code", nullable = false)
    // private String shippingPostalCode;

    // @Column(name = "shipping_country", nullable = false)
    // private String shippingCountry;

    @Column(name = "shipping_cost", nullable = false)
    private double shippingCost;

    // @Column(name = "billing_address", nullable = false)
    // private String billingAddress;
    //
    // @Column(name = "billing_city", nullable = false)
    // private String billingCity;
    //
    // @Column(name = "billing_state", nullable = false)
    // private String billingState;
    //
    // @Column(name = "billing_postal_code", nullable = false)
    // private String billingPostalCode;
    //
    // @Column(name = "billing_country", nullable = false)
    // private String billingCountry;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "order")
    private List<Item> items = new ArrayList<>();

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // @ManyToOne(fetch = FetchType.LAZY) // or FetchType.EAGER
    // @JoinColumn(name = "cart_id", nullable = true)
    // private Cart cart; // Many-to-One relationship with Cart

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public static String generateOrderNumber(Long userId, boolean increment) {
        // Get current date and time
        LocalDateTime now = LocalDateTime.now();

        // Add seconds if increment is true
        if (increment) {
            now = now.plusSeconds(1);
        }

        // Format order number
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

        // Format order number
        return "ORD" + userId + now.format(formatter);
    }
}
