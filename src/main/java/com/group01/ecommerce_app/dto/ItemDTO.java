package com.group01.ecommerce_app.dto;

import java.math.BigDecimal;
import java.util.List;

import com.group01.ecommerce_app.model.Item;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class ItemDTO {
    private Long productId;
    private Integer quantity;

    // Getters and Setters

    // Convert Item entity to ItemDTO
    public static ItemDTO convertToItemDTO(Item item) {
        ItemDTO cartItemDTO = new ItemDTO();
        cartItemDTO.setProductId(item.getProduct().getId());
        cartItemDTO.setQuantity(item.getQuantity());
        return cartItemDTO;
    }
}
