package com.dungle939.ecommerce.services;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dungle939.ecommerce.dtos.CartItemDTO;
import com.dungle939.ecommerce.dtos.CreateOrderItemDTO;
import com.dungle939.ecommerce.dtos.CreateOrderRequestDTO;
import com.dungle939.ecommerce.dtos.OrderDTO;
import com.dungle939.ecommerce.dtos.OrderProductDTO;
import com.dungle939.ecommerce.dtos.PaymentSummaryDTO;
import com.dungle939.ecommerce.models.DeliveryOption;
import com.dungle939.ecommerce.models.Order;
import com.dungle939.ecommerce.models.OrderItem;
import com.dungle939.ecommerce.models.Product;
import com.dungle939.ecommerce.repos.DeliveryOptionsRepo;
import com.dungle939.ecommerce.repos.OrderRepo;
import com.dungle939.ecommerce.repos.ProductRepo;

@Service
public class OrderService {

    @Autowired
    private CartItemService cartItemService;

    @Autowired
    private DeliveryOptionsRepo deliveryOptionsRepo;

    @Autowired
    private OrderRepo orderRepo;

    @Autowired
    private ProductRepo productRepo;

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

    public List<OrderDTO> getOrders(boolean expandProducts) {
    
        List<Order> orders = orderRepo.findAll();

        return orders.stream().map(order -> {
            OrderDTO dto = new OrderDTO();
            dto.setId(order.getId());
            dto.setOrderTimeMs(order.getOrderTimeMs());
            dto.setTotalCostCents(order.getTotalCostCents());

            List<OrderProductDTO> productDTOs = order.getOrderItems().stream().map(item -> {
                OrderProductDTO itemDto = new OrderProductDTO();
                itemDto.setProductId(item.getProductId());
                itemDto.setQuantity(item.getQuantity());
                itemDto.setEstimatedDeliveryTimeMs(item.getEstimatedDeliveryTimeMs());

                if (expandProducts) {
                    productRepo.findById(item.getProductId())
                            .ifPresent((product) -> itemDto.setProduct(product));
                }

                return itemDto;
            }).collect(Collectors.toList());

            dto.setProducts(productDTOs);
            return dto;
        }).collect(Collectors.toList());
    }

    public Order placeOrder(CreateOrderRequestDTO request) {
        // 1. Validate if product exist in the Database
        for (CreateOrderItemDTO item : request.getProducts()) {
            if (!productRepo.existsById(item.getProductId())) {
                throw new RuntimeException("Product not found!");
            }
        }

        // 2. Create order
        Order order = new Order();
        order.setOrderTimeMs(request.getOrderTimeMs());
        order.setTotalCostCents(request.getTotalCostCents());

        // 3. Create order item
        List<OrderItem> orderItems = new ArrayList<>();
        for (CreateOrderItemDTO itemRequest : request.getProducts()) {
            OrderItem newItem = new OrderItem();
            newItem.setProductId(itemRequest.getProductId());
            newItem.setQuantity(itemRequest.getQuantity());
            newItem.setEstimatedDeliveryTimeMs(itemRequest.getEstimatedDeliveryTimeMs());
            newItem.setOrder(order);
            orderItems.add(newItem);
        }

        // 4. Set list
        order.setOrderItems(orderItems);
        return orderRepo.save(order);
    }

    public OrderDTO getOrderById(boolean expandProducts, String orderId) {
        Order order = orderRepo.findById(orderId).orElse(null);

        OrderDTO dto = new OrderDTO();
        dto.setId(order.getId());
        dto.setOrderTimeMs(order.getOrderTimeMs());
        dto.setTotalCostCents(order.getTotalCostCents());

        List<OrderProductDTO> productDTOs = order.getOrderItems().stream().map(item -> {
            OrderProductDTO itemDto = new OrderProductDTO();
            itemDto.setProductId(item.getProductId());
            itemDto.setQuantity(item.getQuantity());
            itemDto.setEstimatedDeliveryTimeMs(item.getEstimatedDeliveryTimeMs());

            if (expandProducts) {
                productRepo.findById(item.getProductId())
                        .ifPresent((product) -> itemDto.setProduct(product));
            }

            return itemDto;
        }).collect(Collectors.toList());

        dto.setProducts(productDTOs);
        return dto;
    }

}
