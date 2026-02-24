package com.dungle939.ecommerce.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dungle939.ecommerce.dtos.CartItemDTO;
import com.dungle939.ecommerce.models.CartItem;
import com.dungle939.ecommerce.repos.CartItemRepo;
import com.dungle939.ecommerce.repos.ProductRepo;

@Service
public class CartItemService {

    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private CartItemRepo cartItemRepo;

    // Get all cart Items
    public List<CartItemDTO> getCartItems(boolean isExpandProduct) {
        List<CartItem> cartItems = cartItemRepo.findAll();
        return cartItems.stream().map((item) -> {
            CartItemDTO dto = new CartItemDTO();
            dto.setProductId(item.getProductId());
            dto.setQuantity(item.getQuantity());
            dto.setDeliveryOptionId(item.getDeliveryOptionId());

            if (isExpandProduct) {
                productRepo.findById(item.getProductId())
                        .ifPresent(product -> dto.setProduct(product));
            }
            return dto;
        }).collect(Collectors.toList());
    }

    // Add a Product to cart
    public CartItem addCartItem(CartItem cartItem) {
        return cartItemRepo.save(cartItem);
    }

    // Update item to Cart
    public CartItem updateCartItem(CartItem cartItem) {
        return cartItemRepo.save(cartItem);
    }
}
