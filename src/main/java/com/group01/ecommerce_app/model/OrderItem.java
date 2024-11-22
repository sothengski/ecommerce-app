// package com.group01.ecommerce_app.model;

// import java.io.Serializable;
// import jakarta.persistence.*;
// import lombok.AllArgsConstructor;
// import lombok.Builder;
// import lombok.Data;
// import lombok.NoArgsConstructor;

// @NoArgsConstructor
// @AllArgsConstructor
// @Data
// @Builder
// @Entity
// @Table(name = "order_items")
// public class OrderItem implements Serializable {

// @Id
// @GeneratedValue(strategy = GenerationType.IDENTITY)
// private Long id;

// @ManyToOne
// @JoinColumn(name = "order_id", nullable = false)
// private Order order;

// @ManyToOne(fetch = FetchType.LAZY)
// @JoinColumn(name = "product_id", nullable = false)
// private Product product;

// @Column(name = "quantity", nullable = false)
// private int quantity;

// @Column(name = "unit_price", nullable = false)
// private double unitPrice;

// @Column(name = "total_price", nullable = false)
// private double totalPrice;
// }
