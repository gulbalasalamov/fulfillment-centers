package com.gulbalasalamov.fulfillment_centers.model.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.gulbalasalamov.fulfillment_centers.model.enums.Status;
import com.gulbalasalamov.fulfillment_centers.serializer.StatusDeserializer;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {
    private Long id;
    private String productId;
    @JsonDeserialize(using = StatusDeserializer.class)
    private Status status;
    private String fulfillmentCenter;
    private Integer quantity;
    private Double value;
}
