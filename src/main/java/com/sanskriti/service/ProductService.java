package com.sanskriti.service;

import com.sanskriti.dto.ProductRequest;
import com.sanskriti.model.Product;
import com.sanskriti.model.ProductVariant;
import com.sanskriti.repository.ProductRepository;
import com.sanskriti.repository.ProductVariantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductVariantRepository variantRepository;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }

    @Transactional
    public Product createProduct(ProductRequest request) {
        Product product = Product.builder()
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .category(request.getCategory())
                .build();
        
        Product savedProduct = productRepository.save(product);

        // Auto-generate standard variant for Sarees
        if ("SAREE".equalsIgnoreCase(request.getCategory())) {
            try {
                ProductVariant variant = ProductVariant.builder()
                        .product(savedProduct)
                        .size("Standard")
                        .sku("SKU-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase())
                        .build();
                variantRepository.save(variant);
                if (savedProduct.getVariants() != null) {
                    savedProduct.getVariants().add(variant);
                }
            } catch (Exception e) {
                System.out.println("FATAL ERROR CREATING Saree VARIANT: " + e.getMessage());
                e.printStackTrace();
                throw new RuntimeException("Variant auto-creation failed: " + e.getMessage());
            }
        }

        return savedProduct;
    }
}
