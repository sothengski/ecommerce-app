package com.group01.ecommerce_app.dto;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.group01.ecommerce_app.model.Cart;
import com.group01.ecommerce_app.model.Item;

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

        private int quantity;
        private Double totalPrice;
        // private Long productId;
        private List<ItemDTO> items;

        public static CartDTO convertToCartDTO(final Cart cart) {
                CartDTO cartDTO = new CartDTO();
                cartDTO.setId(cart.getId());
                cartDTO.setQuantity(cart.getQuantity());
                cartDTO.setTotalPrice(cart.getTotalPrice());
                cartDTO.setUserId(cart.getUser().getId());
                cartDTO.setItems(cart.getItems().stream()
                                .map(ItemDTO::convertToItemDTO)
                                .collect(Collectors.toList()));
                return cartDTO;
        }

        public static ItemDTO convertToItemDTO(Item item) {
                ItemDTO itemDTO = new ItemDTO();
                itemDTO.setId(item.getId());
                itemDTO.setQuantity(item.getQuantity());
                itemDTO.setUnitPrice(item.getUnitPrice());
                itemDTO.setTotalPrice(item.getTotalPrice());
                return itemDTO;
        }
}