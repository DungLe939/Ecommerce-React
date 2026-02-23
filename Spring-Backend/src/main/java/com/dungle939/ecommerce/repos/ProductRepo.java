package com.dungle939.ecommerce.repos;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dungle939.ecommerce.models.Product;

public interface ProductRepo extends JpaRepository<Product, String> {
    
}
