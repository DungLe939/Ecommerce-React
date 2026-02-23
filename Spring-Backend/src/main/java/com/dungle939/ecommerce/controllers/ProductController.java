package com.dungle939.ecommerce.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dungle939.ecommerce.dtos.CartItemDTO;
import com.dungle939.ecommerce.models.CartItem;
import com.dungle939.ecommerce.models.Product;
import com.dungle939.ecommerce.services.ProductService;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class ProductController {

    @Autowired
    private ProductService productService;

    // Get all Products
    @GetMapping("/products")
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        if (products.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // 204 No Content
        }
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    // Add a Product
    @PostMapping("/product")
    public ResponseEntity<Product> addProduct(@RequestBody Product product) {
        try {
            Product savedProduct = productService.addProduct(product);
            return new ResponseEntity<>(savedProduct, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Add batch Products
    @PostMapping("/products/batch")
    public ResponseEntity<List<Product>> addProducts(@RequestBody List<Product> products) {
        List<Product> savedProducts = productService.addProducts(products);
        return new ResponseEntity<>(savedProducts, HttpStatus.CREATED);
    }

    // Get all cart Products
    @GetMapping("/cart-items")
    public ResponseEntity<List<CartItemDTO>> getCartItems(
        @RequestParam(required = false, defaultValue = "false") String expand
    ) {
        boolean isExpandProduct = expand.equals("product");
        List<CartItemDTO> products = productService.getCartItems(isExpandProduct);
        if (products.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // 204 No Content
        }
        return new ResponseEntity<>(products, HttpStatus.OK);
    }


    // Add a Product to the Cart
    @PostMapping("/cart-item")
    public ResponseEntity<CartItem> addCartItem(@RequestBody CartItem cartItem) {
        CartItem savedItem = productService.addCartItem(cartItem);
        return new ResponseEntity<>(savedItem, HttpStatus.CREATED);
    }

    //
}
