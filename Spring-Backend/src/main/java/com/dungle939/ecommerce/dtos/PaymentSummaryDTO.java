package com.dungle939.ecommerce.dtos;

import lombok.Data;

@Data
public class PaymentSummaryDTO {
    private Integer totalItems;
    private Integer productCostCents;
    private Integer shippingCostCents;
    private Integer totalCostBeforeTaxCents;
    private Integer taxCents;
    private Integer totalCostCents;
}
