package com.group01.ecommerce_app.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.group01.ecommerce_app.dto.CartDTO;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data // @Getter @Setter @RequiredArgsConstructor @ToString @EqualsAndHashCode
@Builder
@Entity
@Table(name = "carts")
public class Cart implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "totalPrice")
    private Double totalPrice;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Item> items = new ArrayList<>();

    // @ManyToOne
    // @JoinColumn(name = "product_id", referencedColumnName = "id", insertable =
    // false, updatable = false)
    // private Product product;

    // @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval =
    // true)
    // private List<Order> orders = new ArrayList<>(); // One-to-Many relationship
    // with Order

    // public Cart(CartDTO cartDTO, Product product, int userId) {
    // this.userId = userId;
    // this.productId = cartDTO.getProductId();
    // this.quantity = cartDTO.getQuantity();
    // this.product = product;
    // this.createdDate = new Date();
    // }

    // public Cart(Integer userId, Long productId, int quantity) {
    // this.userId = userId;
    // this.productId = productId;
    // this.createdDate = new Date();
    // this.quantity = quantity;
    // }

    // public Cart(CartDTO cartDTO, Product product) {
    // this.productId = cartDTO.getProductId();
    // this.quantity = cartDTO.getQuantity();
    // this.product = product;
    // this.createdDate = new Date();
    // }
}
