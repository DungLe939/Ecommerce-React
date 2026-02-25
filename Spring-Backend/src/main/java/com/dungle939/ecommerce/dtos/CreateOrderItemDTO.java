package com.dungle939.ecommerce.dtos;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class CreateOrderItemDTO {

    @NotBlank(message = "productId is required")
    private String productId;

    @NotNull(message = "quantity is required")
    @Positive(message = "quantity must be positive")
    private Integer quantity;

    @NotNull(message = "estimatedDeliveryTimeMs is required")
    private Long estimatedDeliveryTimeMs;
}