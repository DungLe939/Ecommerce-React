package com.dungle939.ecommerce.dtos;

import com.dungle939.ecommerce.models.Product;

import lombok.Data;

@Data
public class OrderProductDTO {
    private String productId;
    private Integer quantity;
    private Long estimatedDeliveryTimeMs;

    private Product product;
}
