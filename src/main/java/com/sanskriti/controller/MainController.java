package com.sanskriti.controller;

import com.sanskriti.dto.*;
import com.sanskriti.model.*;
import com.sanskriti.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MainController {

    private final ProductService productService;
    private final VariantService variantService;
    private final InventoryService inventoryService;
    private final ImageService imageService;
    private final OrderService orderService;
    private final DashboardService dashboardService;

    // --- PRODUCTS ---
    @GetMapping("/products")
    public ResponseEntity<ApiResponse<List<Product>>> getAllProducts() {
        return ResponseEntity.ok(ApiResponse.success(productService.getAllProducts(), "Products retrieved"));
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<ApiResponse<Product>> getProduct(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(productService.getProductById(id), "Product retrieved"));
    }

    @PostMapping("/products")
    public ResponseEntity<ApiResponse<Product>> createProduct(@RequestBody ProductRequest request) {
        return ResponseEntity.ok(ApiResponse.success(productService.createProduct(request), "Product created"));
    }

    // --- VARIANTS ---
    @PostMapping("/variants")
    public ResponseEntity<ApiResponse<ProductVariant>> createVariant(@RequestBody VariantRequest request) {
        return ResponseEntity.ok(ApiResponse.success(variantService.createVariant(request), "Variant created"));
    }

    // --- IMAGES ---
    @PostMapping("/images")
    public ResponseEntity<ApiResponse<ProductImage>> saveImage(@RequestBody ImageRequest request) {
        return ResponseEntity.ok(ApiResponse.success(imageService.saveImage(request), "Image saved"));
    }

    // --- INVENTORY ---
    @PostMapping("/inventory/stock-in")
    public ResponseEntity<ApiResponse<StockMovement>> stockIn(@RequestBody StockRequest request) {
        return ResponseEntity.ok(ApiResponse.success(inventoryService.recordStockIn(request), "Stock added"));
    }

    @PostMapping("/inventory/stock-out")
    public ResponseEntity<ApiResponse<StockMovement>> stockOut(@RequestBody StockRequest request) {
        return ResponseEntity.ok(ApiResponse.success(inventoryService.recordStockOut(request), "Stock deducted"));
    }

    @GetMapping("/inventory/movements")
    public ResponseEntity<ApiResponse<List<StockMovement>>> getMovements() {
        return ResponseEntity.ok(ApiResponse.success(inventoryService.getMovementHistory(), "Movements retrieved"));
    }

    // --- ORDERS ---
    @GetMapping("/orders")
    public ResponseEntity<ApiResponse<List<Order>>> getOrders() {
        return ResponseEntity.ok(ApiResponse.success(orderService.getAllOrders(), "Orders retrieved"));
    }

    @PostMapping("/orders")
    public ResponseEntity<ApiResponse<Order>> createOrder(@RequestBody OrderRequest request) {
        return ResponseEntity.ok(ApiResponse.success(orderService.createOrder(request), "Order created"));
    }

    @PatchMapping("/orders/{id}/status")
    public ResponseEntity<ApiResponse<Order>> updateOrderStatus(@PathVariable Long id, @RequestBody Map<String, String> payload) {
        return ResponseEntity.ok(ApiResponse.success(orderService.updateOrderStatus(id, payload.get("status")), "Status updated"));
    }

    // --- DASHBOARD ---
    @GetMapping("/dashboard")
    public ResponseEntity<ApiResponse<DashboardResponse>> getStats() {
        return ResponseEntity.ok(ApiResponse.success(dashboardService.getDashboardStats(), "Stats retrieved"));
    }
}
