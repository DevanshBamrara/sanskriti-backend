package com.sanskriti.service;

import com.sanskriti.dto.DashboardResponse;
import com.sanskriti.model.Order;
import com.sanskriti.repository.OrderRepository;
import com.sanskriti.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    public DashboardResponse getDashboardStats() {
        long totalProducts = productRepository.count();
        long totalOrders = orderRepository.count();
        
        List<Order> orders = orderRepository.findAll();
        BigDecimal totalRevenue = orders.stream()
                .filter(o -> !o.getStatus().equals("CANCELLED"))
                .map(Order::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return DashboardResponse.builder()
                .totalProducts(totalProducts)
                .totalOrders(totalOrders)
                .totalRevenue(totalRevenue)
                .build();
    }
}
