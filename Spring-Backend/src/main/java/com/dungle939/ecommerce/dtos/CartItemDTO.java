package com.dungle939.ecommerce.dtos;

import lombok.Data;

@Data
public class CartItemDTO {
    private String productID;
    private Integer quantity;
    private String deliveryOptionId;
}
