package com.dungle939.ecommerce.dtos;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class CreateOrderRequestDTO {

    @NotNull(message = "orderTimeMs is required")
    private Long orderTimeMs;

    @NotNull(message = "totalCostCents is required")
    @Positive(message = "totalCostCents must be positive")
    private Integer totalCostCents;

    // JSON gửi lên là "product", nhưng trong Java ta đặt tên biến là "products"
    @JsonProperty("product")
    @NotEmpty(message = "product list cannot be empty")
    @Valid
    private List<CreateOrderItemDTO> products;
}
