package com.sanskriti.service;

import com.sanskriti.dto.VariantRequest;
import com.sanskriti.model.Product;
import com.sanskriti.model.ProductVariant;
import com.sanskriti.repository.ProductRepository;
import com.sanskriti.repository.ProductVariantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VariantService {

    private final ProductRepository productRepository;
    private final ProductVariantRepository variantRepository;

    public ProductVariant createVariant(VariantRequest request) {
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        ProductVariant variant = ProductVariant.builder()
                .product(product)
                .size(request.getSize())
                .sku("SKU-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase())
                .build();

        return variantRepository.save(variant);
    }
}
