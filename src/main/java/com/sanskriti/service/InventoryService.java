package com.sanskriti.service;

import com.sanskriti.dto.StockRequest;
import com.sanskriti.model.ProductVariant;
import com.sanskriti.model.StockMovement;
import com.sanskriti.repository.ProductVariantRepository;
import com.sanskriti.repository.StockMovementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final ProductVariantRepository variantRepository;
    private final StockMovementRepository movementRepository;

    @Transactional
    public StockMovement recordStockIn(StockRequest request) {
        ProductVariant variant = variantRepository.findById(request.getVariantId())
                .orElseThrow(() -> new RuntimeException("Variant not found"));

        variant.setStock(variant.getStock() + request.getQuantity());
        variantRepository.save(variant);

        StockMovement movement = StockMovement.builder()
                .variant(variant)
                .type("IN")
                .quantity(request.getQuantity())
                .reason(request.getReason())
                .productName(variant.getProduct().getName())
                .size(variant.getSize())
                .build();

        return movementRepository.save(movement);
    }

    @Transactional
    public StockMovement recordStockOut(StockRequest request) {
        ProductVariant variant = variantRepository.findById(request.getVariantId())
                .orElseThrow(() -> new RuntimeException("Variant not found"));

        if (variant.getStock() < request.getQuantity()) {
            throw new RuntimeException("Insufficient stock");
        }

        variant.setStock(variant.getStock() - request.getQuantity());
        variantRepository.save(variant);

        StockMovement movement = StockMovement.builder()
                .variant(variant)
                .type("OUT")
                .quantity(request.getQuantity())
                .reason(request.getReason())
                .productName(variant.getProduct().getName())
                .size(variant.getSize())
                .build();

        return movementRepository.save(movement);
    }

    public List<StockMovement> getMovementHistory() {
        return movementRepository.findAllByOrderByCreatedAtDesc();
    }
}
