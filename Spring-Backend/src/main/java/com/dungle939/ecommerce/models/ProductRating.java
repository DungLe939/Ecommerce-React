package com.dungle939.ecommerce.models;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Data
@Embeddable
public class ProductRating {
    private double stars;
    private int count;
}
