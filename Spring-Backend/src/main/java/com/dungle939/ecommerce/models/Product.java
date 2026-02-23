package com.dungle939.ecommerce.models;

import java.util.Date;
import java.util.List;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Product {
    
    @Id
    private String id;
    private String image;
    private String name;

    @Column(name = "price_cents")
    private int priceCents;

    @Embedded
    private ProductRating rating;

    @ElementCollection
    @CollectionTable(name = "product_keywords",
        joinColumns = @JoinColumn(name = "product_id"))
    List<String> keywords;

    private Date createdAt;
    private Date updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = new Date();
        updatedAt = new Date();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = new Date();
    }
}
