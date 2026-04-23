package com.sanskriti.service;

import com.sanskriti.dto.OrderRequest;
import com.sanskriti.model.Order;
import com.sanskriti.model.OrderItem;
import com.sanskriti.model.ProductVariant;
import com.sanskriti.repository.OrderRepository;
import com.sanskriti.repository.ProductVariantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductVariantRepository variantRepository;
    private final InventoryService inventoryService;

    @Transactional
    public Order createOrder(OrderRequest request) {
        Order order = Order.builder()
                .customerName(request.getCustomerName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .address(request.getAddress())
                .build();

        List<OrderItem> items = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;

        for (OrderRequest.OrderItemDto itemDto : request.getItems()) {
            ProductVariant variant = variantRepository.findById(itemDto.getVariantId())
                    .orElseThrow(() -> new RuntimeException("Variant not found"));

            if (variant.getStock() < itemDto.getQuantity()) {
                throw new RuntimeException("Insufficient stock for " + variant.getProduct().getName());
            }

            // Deduct stock natively
            variant.setStock(variant.getStock() - itemDto.getQuantity());
            variantRepository.save(variant);

            OrderItem orderItem = OrderItem.builder()
                    .order(order)
                    .variantId(variant.getId())
                    .productName(variant.getProduct().getName())
                    .size(variant.getSize())
                    .quantity(itemDto.getQuantity())
                    .build();

            items.add(orderItem);
            
            BigDecimal lineTotal = variant.getProduct().getPrice().multiply(BigDecimal.valueOf(itemDto.getQuantity()));
            total = total.add(lineTotal);
        }

        order.setItems(items);
        order.setTotalAmount(total);

        return orderRepository.save(order);
    }
    
    public List<Order> getAllOrders() {
        return orderRepository.findAllByOrderByCreatedAtDesc();
    }

    public Order updateOrderStatus(Long id, String status) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new RuntimeException("Order not found"));
        order.setStatus(status);
        return orderRepository.save(order);
    }
}
