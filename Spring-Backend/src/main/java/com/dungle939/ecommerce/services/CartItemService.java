package com.dungle939.ecommerce.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dungle939.ecommerce.dtos.CartItemDTO;
import com.dungle939.ecommerce.dtos.UpdateCartItemDTO;
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
        return cartItemRepo
                .findById(cartItem.getProductId())
                .map((existingItem) -> {
                    existingItem.setQuantity(existingItem.getQuantity() + cartItem.getQuantity());
                    return cartItemRepo.save(existingItem);
                })
                .orElseGet(() -> cartItemRepo.save(cartItem));
    }

    // Update the item
    public CartItem updateCartItem(String productId, UpdateCartItemDTO updateCartItemDTO) {
        CartItem existingItem = cartItemRepo.findById(productId)
                .orElseThrow(() -> new RuntimeException("Cart item not found: " + productId));

        existingItem.setQuantity(updateCartItemDTO.getQuantity());
        existingItem.setDeliveryOptionId(updateCartItemDTO.getDeliveryOptionId());
        return cartItemRepo.save(existingItem);
    }
}
