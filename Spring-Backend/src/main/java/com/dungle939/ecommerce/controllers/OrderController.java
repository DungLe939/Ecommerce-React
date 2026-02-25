package com.dungle939.ecommerce.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.dungle939.ecommerce.dtos.CreateOrderRequestDTO;
import com.dungle939.ecommerce.dtos.OrderDTO;
import com.dungle939.ecommerce.dtos.PaymentSummaryDTO;
import com.dungle939.ecommerce.models.Order;
import com.dungle939.ecommerce.models.Product;
import com.dungle939.ecommerce.services.OrderService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class OrderController {

    @Autowired
    private OrderService orderService;

    // Get payment summary
    @GetMapping("/payment-summary")
    public ResponseEntity<PaymentSummaryDTO> getPaymentSummary() {
        PaymentSummaryDTO summary = orderService.getPaymentSummary();
        return new ResponseEntity<>(summary, HttpStatus.OK);
    }

    // Get orders
    @GetMapping("/orders")
    public ResponseEntity<List<OrderDTO>> getOrders(
            @RequestParam(required = false, defaultValue = "false") String expand) {
        boolean expandProducts = "products".equals(expand);

        List<OrderDTO> orders = orderService.getOrders(expandProducts);
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    // Post Order
    @PostMapping("/orders")
    public ResponseEntity<Order> placeOrders(@Valid @RequestBody CreateOrderRequestDTO request) {
        Order newOrder = orderService.placeOrder(request);
        return new ResponseEntity<>(newOrder, HttpStatus.CREATED);
    }

    // Get Tracking of product
    @GetMapping("/orders/{orderId}")
    public ResponseEntity<OrderDTO> getOrderById(
        @PathVariable String orderId,
        @RequestParam(required = false, defaultValue = "false") String expand
    ) {
        boolean expandProducts = "products".equals(expand);

        OrderDTO orders = orderService.getOrderById(expandProducts, orderId);
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

}
