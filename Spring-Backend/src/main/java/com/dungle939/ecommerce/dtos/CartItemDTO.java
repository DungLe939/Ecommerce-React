package com.dungle939.ecommerce.dtos;

import com.dungle939.ecommerce.models.Product;

import lombok.Data;

@Data
public class CartItemDTO {
    private String productId;
    private Integer quantity;
    private String deliveryOptionId;

    private Product product;
}
