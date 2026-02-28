package com.dungle939.ecommerce.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
import com.dungle939.ecommerce.repos.CartItemRepo;
import com.dungle939.ecommerce.repos.DeliveryOptionsRepo;
import com.dungle939.ecommerce.repos.OrderRepo;
import com.dungle939.ecommerce.repos.ProductRepo;

@Service
@Transactional
public class OrderService {

    @Autowired
    private CartItemService cartItemService;

    @Autowired
    private DeliveryOptionsRepo deliveryOptionsRepo;

    @Autowired
    private OrderRepo orderRepo;

    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private CartItemRepo cartItemRepo;

    public PaymentSummaryDTO getPaymentSummary() {
        PaymentSummaryDTO newPayment = new PaymentSummaryDTO();

        List<CartItemDTO> cartItems = cartItemService.getCartItems(true);

        // Batch load all delivery options in 1 query instead of N
        List<String> deliveryOptionIds = cartItems.stream()
                .map(CartItemDTO::getDeliveryOptionId)
                .distinct()
                .collect(Collectors.toList());
        Map<String, DeliveryOption> deliveryOptionMap = deliveryOptionsRepo.findAllById(deliveryOptionIds)
                .stream()
                .collect(Collectors.toMap(DeliveryOption::getId, Function.identity()));

        int totalItems = 0;
        int productCostCents = 0;
        int shippingCostCents = 0;

        for (CartItemDTO item : cartItems) {
            totalItems += item.getQuantity();

            Product tmpProduct = item.getProduct();
            productCostCents += tmpProduct.getPriceCents() * item.getQuantity();

            DeliveryOption deliveryOption = deliveryOptionMap.get(item.getDeliveryOptionId());
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

        // Batch load all products across all orders in 1 query
        Map<String, Product> productMap = Map.of();
        if (expandProducts && !orders.isEmpty()) {
            List<String> allProductIds = orders.stream()
                    .flatMap(order -> order.getOrderItems().stream())
                    .map(OrderItem::getProductId)
                    .distinct()
                    .collect(Collectors.toList());
            productMap = productRepo.findAllById(allProductIds).stream()
                    .collect(Collectors.toMap(Product::getId, Function.identity()));
        }

        final Map<String, Product> finalProductMap = productMap;
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
                    Product product = finalProductMap.get(item.getProductId());
                    if (product != null) {
                        itemDto.setProduct(product);
                    }
                }

                return itemDto;
            }).collect(Collectors.toList());

            dto.setProducts(productDTOs);
            return dto;
        }).collect(Collectors.toList());
    }

    public Order placeOrder(CreateOrderRequestDTO request) {
        // 1. Batch validate all products in 1 query instead of N
        List<String> productIds = request.getProducts().stream()
                .map(CreateOrderItemDTO::getProductId)
                .collect(Collectors.toList());
        List<Product> existingProducts = productRepo.findAllById(productIds);
        if (existingProducts.size() != productIds.size()) {
            throw new RuntimeException("One or more products not found!");
        }

        // 2. Create order
        Order order = new Order();
        order.setOrderTimeMs(request.getOrderTimeMs());
        order.setTotalCostCents(request.getTotalCostCents());

        // 3. Create order items
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
        Order savedOrder = orderRepo.save(order);

        // 5. Clear cart after placing order
        cartItemRepo.deleteAll();

        return savedOrder;
    }

    public OrderDTO getOrderById(boolean expandProducts, String orderId) {
        Order order = orderRepo.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found: " + orderId));

        OrderDTO dto = new OrderDTO();
        dto.setId(order.getId());
        dto.setOrderTimeMs(order.getOrderTimeMs());
        dto.setTotalCostCents(order.getTotalCostCents());

        // Batch load products for this order in 1 query
        Map<String, Product> productMap = Map.of();
        if (expandProducts && !order.getOrderItems().isEmpty()) {
            List<String> productIds = order.getOrderItems().stream()
                    .map(OrderItem::getProductId)
                    .distinct()
                    .collect(Collectors.toList());
            productMap = productRepo.findAllById(productIds).stream()
                    .collect(Collectors.toMap(Product::getId, Function.identity()));
        }

        final Map<String, Product> finalProductMap = productMap;
        List<OrderProductDTO> productDTOs = order.getOrderItems().stream().map(item -> {
            OrderProductDTO itemDto = new OrderProductDTO();
            itemDto.setProductId(item.getProductId());
            itemDto.setQuantity(item.getQuantity());
            itemDto.setEstimatedDeliveryTimeMs(item.getEstimatedDeliveryTimeMs());

            if (expandProducts) {
                Product product = finalProductMap.get(item.getProductId());
                if (product != null) {
                    itemDto.setProduct(product);
                }
            }

            return itemDto;
        }).collect(Collectors.toList());

        dto.setProducts(productDTOs);
        return dto;
    }

    // New Delete Order method
    public void deleteOrder(String orderId) {
        Order order = orderRepo.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found: " + orderId));
        orderRepo.delete(order);
    }

}
