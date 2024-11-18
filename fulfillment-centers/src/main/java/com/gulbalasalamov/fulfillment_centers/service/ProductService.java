package com.gulbalasalamov.fulfillment_centers.service;

import com.gulbalasalamov.fulfillment_centers.exception.ProductNotFoundException;
import com.gulbalasalamov.fulfillment_centers.model.dto.ProductDTO;
import com.gulbalasalamov.fulfillment_centers.model.entity.Product;
import com.gulbalasalamov.fulfillment_centers.model.enums.Status;
import com.gulbalasalamov.fulfillment_centers.model.mapper.ProductMapper;
import com.gulbalasalamov.fulfillment_centers.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {
    private ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<ProductDTO> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return products.stream()
                .map(ProductMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<ProductDTO> getProductsByStatus(String status) {
        try {
            Status statusEnum = Status.valueOf(status.toUpperCase());
            List<Product> products = productRepository.findByStatus(statusEnum);
            return products.stream()
                    .map(ProductMapper::toDto)
                    .collect(Collectors.toList());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid status value. Accepted values are: SELLABLE, UNFULFILLABLE, INBOUND");
        }
    }

    public ProductDTO getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product with id: " + id + " not found"));
        return ProductMapper.toDto(product);
    }

    public ProductDTO saveProduct(ProductDTO productDTO) {
        validateProductDTO(productDTO);
        Product product = ProductMapper.toEntity(productDTO);
        product = productRepository.save(product);
        return ProductMapper.toDto(product);
    }

    public ProductDTO updateProduct(Long id, ProductDTO productDTO) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product with id: " + id + " not found"));

        applyPartialUpdates(existingProduct, productDTO);

        existingProduct = productRepository.save(existingProduct);
        return ProductMapper.toDto(existingProduct);
    }

    public String deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ProductNotFoundException("Product with id: " + id + " not found");
        }
        productRepository.deleteById(id);
        return "Product with id: " + id + " deleted successfully";
    }

    public double getTotalValuesByStatus(Status status) {
        return productRepository.findByStatus(status)
                .stream()
                .mapToDouble(Product::getValue)
                .sum();
    }

    private void validateProductDTO(ProductDTO productDTO) {
        if (productDTO.getProductId() == null || productDTO.getProductId().isEmpty()) {
            throw new IllegalArgumentException("Product ID cannot be null or empty");
        }
        if (productDTO.getStatus() == null) {
            throw new IllegalArgumentException("Status cannot be null");
        }
        try {
            String name = productDTO.getStatus().name();
            Status status = Status.valueOf(name);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid status value: " + productDTO.getStatus() + ". Status must be: SELLABLE, UNFULFILLABLE, INBOUND");
        }
        if (productDTO.getFulfillmentCenter() == null || productDTO.getFulfillmentCenter().isEmpty()) {
            throw new IllegalArgumentException("Fulfillment Center cannot be null or empty");
        }
        if (productDTO.getQuantity() != null && productDTO.getQuantity() < 0) {
            throw new IllegalArgumentException("Quantity cannot be null or negative");
        }
        if (productDTO.getValue() != null && productDTO.getValue() < 0) {
            throw new IllegalArgumentException("Value cannot be null or negative");
        }
    }


    private void applyPartialUpdates(Product existingProduct, ProductDTO productDTO) {
        // Only provided fields will be updated
        if (productDTO.getProductId() != null && !productDTO.getProductId().isEmpty()) {
            existingProduct.setProductId(productDTO.getProductId());
        }
        if (productDTO.getStatus() != null) {
            try {
                Status status = Status.valueOf(productDTO.getStatus().name().toUpperCase());
                existingProduct.setStatus(status);
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid status value: " + productDTO.getStatus() + ". Status must be: SELLABLE, UNFULFILLABLE, INBOUND");
            }
            existingProduct.setStatus(productDTO.getStatus());
        }
        if (productDTO.getFulfillmentCenter() != null) {
            existingProduct.setFulfillmentCenter(productDTO.getFulfillmentCenter());
        }
        if (productDTO.getQuantity() != null) {
            if (productDTO.getQuantity() < 0) {
                throw new IllegalArgumentException("Invalid quantity value: " + productDTO.getQuantity() + ". Quantity cannot be negative.");
            }
            existingProduct.setQuantity(productDTO.getQuantity());
        }
        if (productDTO.getValue() != null) {
            if (productDTO.getValue() < 0) {
                throw new IllegalArgumentException("Invalid value: " + productDTO.getValue() + ". Value cannot be negative.");
            }
            existingProduct.setValue(productDTO.getValue());
        }
    }


}
