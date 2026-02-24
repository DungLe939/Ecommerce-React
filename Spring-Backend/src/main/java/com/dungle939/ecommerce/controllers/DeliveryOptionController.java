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
import com.dungle939.ecommerce.dtos.DeliveryOptionDTO;
import com.dungle939.ecommerce.models.CartItem;
import com.dungle939.ecommerce.models.DeliveryOption;
import com.dungle939.ecommerce.models.Product;
import com.dungle939.ecommerce.services.DeliveryOptionService;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class DeliveryOptionController {

    @Autowired
    private DeliveryOptionService deliveryOptionService;

    // Get All Delivery Options
    @GetMapping("/delivery-options")
    public ResponseEntity<List<DeliveryOptionDTO>> getDeliveryOptions(
            @RequestParam(required = false, defaultValue = "false") String expand) {

        boolean isExpand = expand.equals("estimatedDeliveryTimeMs");

        List<DeliveryOptionDTO> deliveryOptions = deliveryOptionService.getDeliveryOptions(isExpand);

        if (deliveryOptions.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // 204 No Content
        }
        
        return new ResponseEntity<>(deliveryOptions, HttpStatus.OK);
    }

    // Add a new option
    @PostMapping("/delivery-option")
    public ResponseEntity<DeliveryOption> addDeliveryOption(@RequestBody DeliveryOption deliveryOption) {
        DeliveryOption option = deliveryOptionService.addDeliveryOption(deliveryOption);
        return new ResponseEntity<>(option, HttpStatus.CREATED);
    }

}
