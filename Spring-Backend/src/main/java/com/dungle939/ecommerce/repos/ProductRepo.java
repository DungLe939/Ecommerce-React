package com.dungle939.ecommerce.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dungle939.ecommerce.models.Product;

@Repository
public interface ProductRepo extends JpaRepository<Product, String> {
    
}
