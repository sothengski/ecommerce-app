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

    // public Double getTotalPrice() {
    // return unitPrice * quantity;
    // // return unitPrice.multiply(BigDecimal.valueOf(quantity));
    // }

    // Convert Item entity to ItemDTO
    public static ItemDTO convertToItemDTO(final Item item) {
        ItemDTO itemDTO = new ItemDTO();
        itemDTO.setId(item.getId());
        itemDTO.setUnitPrice(item.getUnitPrice());
        itemDTO.setQuantity(item.getQuantity());
        itemDTO.setTotalPrice(item.getUnitPrice() * item.getQuantity());
        // Convert Product to ProductDTO
        if (item.getProduct() != null) {
            itemDTO.setProduct(ProductDTO.convertToProductDTO(item.getProduct()));
        }
        return itemDTO;
    }
}
