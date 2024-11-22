package com.group01.ecommerce_app.dto;

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
    private Long id;

    private Integer quantity;
    private double unitPrice;
    private double totalPrice;
    private ProductDTO product;

    // Getters and Setters

    // Convert Item entity to ItemDTO
    public static ItemDTO convertToItemDTO(final Item item) {
        ItemDTO cartItemDTO = new ItemDTO();
        cartItemDTO.setId(item.getId());
        cartItemDTO.setUnitPrice(item.getUnitPrice());
        cartItemDTO.setQuantity(item.getQuantity());
        cartItemDTO.setTotalPrice(item.getUnitPrice() * item.getQuantity());
        // Convert Product to ProductDTO
        if (item.getProduct() != null) {
            cartItemDTO.setProduct(ProductDTO.convertToProductDTO(item.getProduct()));
        }
        return cartItemDTO;
    }
}
