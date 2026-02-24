package com.dungle939.ecommerce.dtos;

import lombok.Data;

@Data
public class DeliveryOptionDTO {
    private String id;
    private Integer deliveryDays;
    private Integer priceCents;
    private Long estimatedDeliveryTimeMs;
}
