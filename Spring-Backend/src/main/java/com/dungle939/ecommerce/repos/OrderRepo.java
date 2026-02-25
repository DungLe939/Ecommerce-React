package com.dungle939.ecommerce.repos;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.dungle939.ecommerce.models.Order;

@Repository
public interface OrderRepo extends JpaRepository<Order, String> {
    
}
