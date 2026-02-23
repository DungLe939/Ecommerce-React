package com.dungle939.ecommerce.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dungle939.ecommerce.dtos.CartItemDTO;
import com.dungle939.ecommerce.models.CartItem;
import com.dungle939.ecommerce.models.Product;
import com.dungle939.ecommerce.repos.CartItemRepo;
import com.dungle939.ecommerce.repos.ProductRepo;

@Service
public class ProductService {

    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private CartItemRepo cartItemRepo;

    // Get all Product
    public List<Product> getAllProducts() {
        return productRepo.findAll();
    }

    // Add a Product
    public Product addProduct(Product product) {
        return productRepo.save(product);
    }

    // Add a Batch Product
    public List<Product> addProducts(List<Product> products) {
        return productRepo.saveAll(products);
    }

    // Get all cart Items
    public List<CartItem> getCartItems() {
        return cartItemRepo.findAll();
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
