package com.sanskriti.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "sanskriti_order_items")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    @JsonIgnore
    private Order order;

    // We store variant ID, but also denormalize name/size in case variant is deleted later
    private Long variantId;
    
    @Column(nullable = false)
    private String productName;
    
    @Column(nullable = false)
    private String size;

    @Column(nullable = false)
    private Integer quantity;
}
