package com.dungle939.ecommerce.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dungle939.ecommerce.dtos.CartItemDTO;
import com.dungle939.ecommerce.dtos.UpdateCartItemDTO;
import com.dungle939.ecommerce.models.CartItem;
import com.dungle939.ecommerce.services.CartItemService;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class CartItemController {

    @Autowired
    private CartItemService cartItemService;

    // Get all cart Products
    @GetMapping("/cart-items")
    public ResponseEntity<List<CartItemDTO>> getCartItems(
            @RequestParam(required = false, defaultValue = "false") String expand) {
        boolean isExpandProduct = expand.equals("product");
        List<CartItemDTO> products = cartItemService.getCartItems(isExpandProduct);
        if (products.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // 204 No Content
        }
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    // Add a Product to the Cart
    @PostMapping("/cart-item")
    public ResponseEntity<CartItem> addCartItem(@RequestBody CartItem cartItem) {
        CartItem savedItem = cartItemService.addCartItem(cartItem);
        return new ResponseEntity<>(savedItem, HttpStatus.CREATED);
    }

    // Updtae a Product in the cart
    @PutMapping("/cart-items/{productId}")
    public ResponseEntity<CartItem> updateCartItem(@PathVariable String productId,
            @RequestBody UpdateCartItemDTO updateCartItemDTO) {
        CartItem updatedItem = cartItemService.updateCartItem(productId, updateCartItemDTO);
        return new ResponseEntity<>(updatedItem, HttpStatus.CREATED);
    }

    // Delete the product by Id
    @DeleteMapping("/cart-items/{productId}")
    public ResponseEntity<Void> deleteCartItem(@PathVariable String productId) {
        cartItemService.deleteCartItem(productId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
