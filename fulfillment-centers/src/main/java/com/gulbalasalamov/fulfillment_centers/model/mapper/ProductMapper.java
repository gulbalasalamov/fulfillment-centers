package com.gulbalasalamov.fulfillment_centers.model.mapper;

import com.gulbalasalamov.fulfillment_centers.model.dto.ProductDTO;
import com.gulbalasalamov.fulfillment_centers.model.entity.Product;

public class ProductMapper {
    public static ProductDTO toDto(Product product) {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setProductId(product.getProductId());
        productDTO.setStatus(product.getStatus());
        productDTO.setFulfillmentCenter(product.getFulfillmentCenter());
        productDTO.setQuantity(product.getQuantity());
        productDTO.setValue(product.getValue());
        return productDTO;
    }

    public static Product toEntity(ProductDTO productDTO) {
        Product product = new Product();
        product.setProductId(productDTO.getProductId());
        product.setStatus(productDTO.getStatus());
        product.setFulfillmentCenter(productDTO.getFulfillmentCenter());
        product.setQuantity(productDTO.getQuantity());
        product.setValue(productDTO.getValue());
        return product;
    }
}
