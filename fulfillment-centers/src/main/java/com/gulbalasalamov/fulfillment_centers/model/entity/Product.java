package com.gulbalasalamov.fulfillment_centers.model.entity;


import com.gulbalasalamov.fulfillment_centers.model.enums.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "product")
@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_seq")
    @SequenceGenerator(name = "product_seq", sequenceName = "product_seq", allocationSize = 1)
    private Long id;
    private String productId;
    @Enumerated(EnumType.STRING)
    private Status status;
    private String fulfillmentCenter;
    private Integer quantity;
    private Double value;
}
