package com.dungle939.ecommerce.dtos;

import lombok.Data;

@Data
public class UpdateCartItemDTO {
    private Integer quantity;
    private String deliveryOptionId;
}
