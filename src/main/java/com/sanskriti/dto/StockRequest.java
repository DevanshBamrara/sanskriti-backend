package com.sanskriti.dto;

import lombok.Data;

@Data
public class StockRequest {
    private Long variantId;
    private Integer quantity;
    private String reason;
}
