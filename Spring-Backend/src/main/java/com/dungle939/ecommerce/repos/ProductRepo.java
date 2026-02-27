package com.dungle939.ecommerce.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.dungle939.ecommerce.models.Product;

@Repository
public interface ProductRepo extends JpaRepository<Product, String> {
    
    @Query("SELECT DISTINCT p FROM Product p LEFT JOIN p.keywords k " +
           "WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :search, '%')) " +
           "OR LOWER(k) LIKE LOWER(CONCAT('%', :search, '%'))")
    List<Product> searchByNameOrKeyword(@Param("search") String search);
}
