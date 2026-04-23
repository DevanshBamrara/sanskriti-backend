package com.sanskriti.dto;

import lombok.Data;

@Data
public class ImageRequest {
    private Long productId;
    private String imageUrl;
    private String imageType;
}
