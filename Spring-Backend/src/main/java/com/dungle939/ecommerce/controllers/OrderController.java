package com.dungle939.ecommerce.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.dungle939.ecommerce.dtos.PaymentSummaryDTO;
import com.dungle939.ecommerce.models.Product;
import com.dungle939.ecommerce.services.OrderService;


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
}
