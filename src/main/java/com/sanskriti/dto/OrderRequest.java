package com.sanskriti.dto;

import lombok.Data;
import java.util.List;

@Data
public class OrderRequest {
    private String customerName;
    private String email;
    private String phone;
    private String address;
    private List<OrderItemDto> items;

    @Data
    public static class OrderItemDto {
        private Long variantId;
        private Integer quantity;
    }
}
