package com.gulbalasalamov.fulfillment_centers.model.dto;

import com.gulbalasalamov.fulfillment_centers.model.enums.Status;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {
    private String productId;
    @Enumerated(EnumType.STRING)
    private Status status;
    private String fulfillmentCenter;
    private int quantity;
    private double value;
}
