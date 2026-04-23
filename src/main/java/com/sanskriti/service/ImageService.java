package com.sanskriti.service;

import com.sanskriti.dto.ImageRequest;
import com.sanskriti.model.Product;
import com.sanskriti.model.ProductImage;
import com.sanskriti.repository.ProductImageRepository;
import com.sanskriti.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ImageService {

    private final ProductRepository productRepository;
    private final ProductImageRepository imageRepository;

    public ProductImage saveImage(ImageRequest request) {
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        ProductImage image = ProductImage.builder()
                .product(product)
                .imageUrl(request.getImageUrl())
                .imageType(request.getImageType())
                .build();

        return imageRepository.save(image);
    }
}
