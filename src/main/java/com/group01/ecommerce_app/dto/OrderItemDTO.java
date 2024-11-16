package com.group01.ecommerce_app.dto;

import com.group01.ecommerce_app.model.OrderItem;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
// @AllArgsConstructor
@Data
public class OrderItemDTO {

    private ProductDTO product;
    private int quantity;
    private double unitPrice;
    private double totalPrice;

    // Getters and Setters
    // You can use Lombok annotations like @Data or @Getter and @Setter here as well

    public Double getTotalPrice() {
        return unitPrice * quantity;
        // return unitPrice.multiply(BigDecimal.valueOf(quantity));
    }

    // Converts OrderItem to OrderItemDTO
    public static OrderItemDTO toOrderItemDTO(OrderItem orderItem) {
        // Product product = orderItem.getProduct();
        ProductDTO productDTO = new ProductDTO(orderItem.getProduct());
        // productDTO.setId(product.getId());
        // productDTO.setName(product.getName());
        // productDTO.setDescription(product.getDescription());
        // productDTO.setBrand(product.getBrand());
        // productDTO.setCategory(product.getCategory());
        // productDTO.setStock(product.getStock());
        // productDTO.setImages(product.getImages());
        // productDTO.setPrice(product.getPrice());
        // productDTO.setColor(product.getColor());
        // productDTO.setSize(product.getSize());
        // productDTO.setUser(product.getUser());

        OrderItemDTO orderItemDTO = new OrderItemDTO();
        // orderItemDTO.setId(orderItem.getId());
        orderItemDTO.setQuantity(orderItem.getQuantity());
        orderItemDTO.setUnitPrice(orderItem.getUnitPrice());
        orderItemDTO.setTotalPrice(orderItem.getTotalPrice());
        orderItemDTO.setProduct(productDTO);

        return orderItemDTO;
    }

    // Converts OrderItemDTO to OrderItem
    public static OrderItem toOrderItemEntity(OrderItemDTO orderItemDTO) {
        OrderItem item = new OrderItem();
        // item.setProductId(dto.getProductId());
        // item.setProductName(dto.getProductName());
        // item.setSku(dto.getSku());
        // item.setQuantity(dto.getQuantity());
        // item.setUnitPrice(dto.getUnitPrice());
        // item.setTotalPrice(dto.getTotalPrice());
        return item;
    }
}
