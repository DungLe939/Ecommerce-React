package com.dungle939.ecommerce.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dungle939.ecommerce.dtos.CartItemDTO;
import com.dungle939.ecommerce.dtos.PaymentSummaryDTO;
import com.dungle939.ecommerce.models.CartItem;
import com.dungle939.ecommerce.models.DeliveryOption;
import com.dungle939.ecommerce.models.Product;
import com.dungle939.ecommerce.repos.DeliveryOptionsRepo;

@Service
public class OrderService {

    @Autowired
    private CartItemService cartItemService;

    @Autowired
    private DeliveryOptionsRepo deliveryOptionsRepo;

    public PaymentSummaryDTO getPaymentSummary() {
        PaymentSummaryDTO newPayment = new PaymentSummaryDTO();

        List<CartItemDTO> cartItems = cartItemService.getCartItems(true);
        
        int totalItems = 0;
        int productCostCents = 0;
        int shippingCostCents = 0;

        for (CartItemDTO item : cartItems) {
            totalItems += item.getQuantity();

            Product tmpProduct = item.getProduct();
            productCostCents += tmpProduct.getPriceCents() * item.getQuantity();

            DeliveryOption deliveryOption = deliveryOptionsRepo.findById(item.getDeliveryOptionId()).orElse(null);

            if (deliveryOption != null) {
                shippingCostCents += deliveryOption.getPriceCents();
            }
        }

        int totalCostBeforeTaxCents = shippingCostCents + productCostCents;
        int taxCents = (int) Math.round(totalCostBeforeTaxCents * 0.1);
        int totalCostCents = totalCostBeforeTaxCents + taxCents;

        newPayment.setTotalItems(totalItems);
        newPayment.setProductCostCents(productCostCents);
        newPayment.setShippingCostCents(shippingCostCents);
        newPayment.setTotalCostBeforeTaxCents(totalCostBeforeTaxCents);
        newPayment.setTaxCents(taxCents);
        newPayment.setTotalCostCents(totalCostCents);

        return newPayment;
    }
    
}
