package com.dungle939.ecommerce.repos;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dungle939.ecommerce.models.CartItem;

public interface CartItemRepo extends JpaRepository<CartItem, String> {
}
