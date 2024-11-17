package com.group01.ecommerce_app.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.group01.ecommerce_app.model.Cart;
import com.group01.ecommerce_app.model.Product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class CartDTO {

    private Long id;
    private Long userId;

    @JsonProperty("user")
    @JsonInclude(Include.NON_NULL)
    private UserDTO userDto;

    private List<ItemDTO> items = new ArrayList<>();
    private BigDecimal totalPrice;

    // Getters and Setters

    // Converts Cart entity to CartDTO
    public static CartDTO convertToCartDTO(final Cart cart) {
        // return new CartDTO(
        // cart.getId(),
        // cart.getItems().stream().map(ItemDTO::convertToItemDTO).toList(),
        // cart.getTotalPrice());

        return CartDTO.builder()
                .id(cart.getId())
                .userId(cart.getUser().getId())
                .userDto(
                        UserDTO.builder()
                                .id(cart.getId())
                                .build())
                .items(cart.getItems().stream()
                        .map(ItemDTO::convertToItemDTO)
                        .toList()) // Convert each Item to ItemDTO
                .build();
    }

    // public static CartDto map(final Cart cart) {
    // return CartDto.builder()
    // .cartId(cart.getCartId())
    // .userId(cart.getUserId())
    // .userDto(
    // UserDto.builder()
    // .userId(cart.getUserId())
    // .build())
    // .build();
    // }

    // public static Cart map(final CartDto cartDto) {
    // return Cart.builder()
    // .cartId(cartDto.getCartId())
    // .userId(cartDto.getUserId())
    // .build();
    // }

}