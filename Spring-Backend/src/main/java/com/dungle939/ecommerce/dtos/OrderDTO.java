package com.dungle939.ecommerce.dtos;

import java.util.List;

import lombok.Data;

@Data
public class OrderDTO {
    private String id;
    private Long orderTimeMs;
    private Integer totalCostCents;
    private List<OrderProductDTO> products;
}