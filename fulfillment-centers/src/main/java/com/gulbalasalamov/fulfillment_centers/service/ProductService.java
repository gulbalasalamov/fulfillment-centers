package com.gulbalasalamov.fulfillment_centers.service;

import com.gulbalasalamov.fulfillment_centers.exception.ProductNotFoundException;
import com.gulbalasalamov.fulfillment_centers.model.dto.ProductDTO;
import com.gulbalasalamov.fulfillment_centers.model.entity.Product;
import com.gulbalasalamov.fulfillment_centers.model.enums.Status;
import com.gulbalasalamov.fulfillment_centers.model.mapper.ProductMapper;
import com.gulbalasalamov.fulfillment_centers.repository.ProductRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {
    //private static final Logger logger = LoggerFactory.getLogger(ProductService.class);
    private static final Logger logger = LogManager.getLogger(ProductService.class);

    private ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<ProductDTO> getAllProducts() {
        logger.info("Retrieving all products");
        List<Product> products = productRepository.findAll();
        List<ProductDTO> productDTOs = products.stream().map(ProductMapper::toDto).collect(Collectors.toList());
        if (productDTOs.isEmpty()) {
            logger.warn("No products found");
        }
        logger.info("Retrieved {} products", productDTOs.size());
        return productDTOs;
    }

    public List<ProductDTO> getProductsByStatus(String status) {
        logger.info("Retrieving products with status: {}", status);
        try {
            Status statusEnum = Status.valueOf(status.toUpperCase());
            List<Product> products = productRepository.findByStatus(statusEnum);
            List<ProductDTO> productDTOs = products.stream().map(ProductMapper::toDto).collect(Collectors.toList());
            logger.info("Retrieved {} products with status: {}", productDTOs.size(), status);
            return productDTOs;
        } catch (IllegalArgumentException e) {
            logger.error("Invalid status value: {}", status, e);
            throw new IllegalArgumentException("Invalid status value. Accepted values are: SELLABLE, UNFULFILLABLE, INBOUND");
        }
    }

    public ProductDTO getProductById(Long id) {
        logger.info("Retrieving product with ID: {}", id);
        Product product = productRepository.findById(id).orElseThrow(() -> {
            logger.error("Product with ID: {} not found", id);
            return new ProductNotFoundException("Product with id: " + id + " not found");
        });
        logger.info("Retrieved product with ID: {}", id);
        return ProductMapper.toDto(product);
    }

    public ProductDTO saveProduct(ProductDTO productDTO) {
        logger.info("Saving product with ID: {}", productDTO.getId());
        validateProductDTO(productDTO);
        Product product = ProductMapper.toEntity(productDTO);
        product = productRepository.save(product);
        logger.info("Product saved successfully with ID: {}", product.getId());
        return ProductMapper.toDto(product);
    }

    public ProductDTO updateProduct(Long id, ProductDTO productDTO) {
        logger.info("Updating product with ID: {}", id);
        Product existingProduct = productRepository.findById(id).orElseThrow(() -> {
            logger.error("Product with ID: {} not found", id);
            return new ProductNotFoundException("Product with id: " + id + " not found");
        });
        applyPartialUpdates(existingProduct, productDTO);
        existingProduct = productRepository.save(existingProduct);
        logger.info("Product updated successfully with ID: {}", id);
        return ProductMapper.toDto(existingProduct);
    }

    public String deleteProduct(Long id) {
        logger.info("Deleting product with ID: {}", id);
        if (!productRepository.existsById(id)) {
            logger.error("Product with ID: {} not found", id);
            throw new ProductNotFoundException("Product with id: " + id + " not found");
        }
        productRepository.deleteById(id);
        logger.info("Product with ID: {} deleted successfully", id);
        return "Product with id: " + id + " deleted successfully";
    }

    public double getTotalValuesByStatus(String status) {
        logger.info("Calculating total value for products with status: {}", status);
        try {
            Status statusEnum = Status.valueOf(status.toUpperCase());
            List<Product> products = productRepository.findByStatus(statusEnum);
            double totalValue = products.stream().mapToDouble(Product::getValue).sum();
            logger.info("Total value for products with status {}: {}", status, totalValue);
            return totalValue;
        } catch (IllegalArgumentException e) {
            logger.error("Invalid status value: {}", status, e);
            throw new IllegalArgumentException("Invalid status value: " + status + " .Accepted values are: SELLABLE, UNFULFILLABLE, INBOUND");
        }
    }

    private void validateProductDTO(ProductDTO productDTO) {
        logger.info("Validating product DTO with ID: {}", productDTO.getId());
        if (productDTO.getProductId() == null || productDTO.getProductId().isEmpty()) {
            logger.error("Product ID cannot be null or empty");
            throw new IllegalArgumentException("Product ID cannot be null or empty");
        }
        if (productDTO.getStatus() == null) {
            logger.error("Status cannot be null");
            throw new IllegalArgumentException("Status cannot be null. Status must be: SELLABLE, UNFULFILLABLE, INBOUND");
        }
        try {
            Status statusEnum = Status.valueOf(productDTO.getStatus().name().toUpperCase());
        } catch (IllegalArgumentException e) {
            logger.error("Invalid status value: {}", productDTO.getStatus(), e);
            throw new IllegalArgumentException("Invalid status value: " + productDTO.getStatus() + ". Status must be: SELLABLE, UNFULFILLABLE, INBOUND");
        }
        if (productDTO.getFulfillmentCenter() == null || productDTO.getFulfillmentCenter().isEmpty()) {
            logger.error("Fulfillment Center cannot be null or empty");
            throw new IllegalArgumentException("Fulfillment Center cannot be null or empty");
        }
        if (productDTO.getQuantity() != null && productDTO.getQuantity() < 0) {
            logger.error("Quantity cannot be null or negative");
            throw new IllegalArgumentException("Quantity cannot be null or negative");
        }
        if (productDTO.getValue() != null && productDTO.getValue() < 0) {
            logger.error("Value cannot be null or negative");
            throw new IllegalArgumentException("Value cannot be null or negative");
        }
        logger.info("Product DTO with ID: {} is valid", productDTO.getId());
    }

    private void applyPartialUpdates(Product existingProduct, ProductDTO productDTO) {
        logger.info("Applying partial updates to product with ID: {}", existingProduct.getId()); // Only provided fields will be updated
        if (productDTO.getProductId() != null && !productDTO.getProductId().isEmpty()) {
            existingProduct.setProductId(productDTO.getProductId());
        }
        if (productDTO.getStatus() != null) {
            try {
                Status status = Status.valueOf(productDTO.getStatus().name().toUpperCase());
                existingProduct.setStatus(status);
            } catch (IllegalArgumentException e) {
                logger.error("Invalid status value: {}", productDTO.getStatus(), e);
                throw new IllegalArgumentException("Invalid status value: " + productDTO.getStatus() + ". Status must be: SELLABLE, UNFULFILLABLE, INBOUND");
            }
            existingProduct.setStatus(productDTO.getStatus());
        }
        if (productDTO.getFulfillmentCenter() != null) {
            existingProduct.setFulfillmentCenter(productDTO.getFulfillmentCenter());
        }
        if (productDTO.getQuantity() != null) {
            if (productDTO.getQuantity() < 0) {
                logger.error("Invalid quantity value: {}. Quantity cannot be negative", productDTO.getQuantity());
                throw new IllegalArgumentException("Invalid quantity value: " + productDTO.getQuantity() + ". Quantity cannot be negative.");
            }
            existingProduct.setQuantity(productDTO.getQuantity());
        }
        if (productDTO.getValue() != null) {
            if (productDTO.getValue() < 0) {
                logger.error("Invalid value: {}. Value cannot be negative", productDTO.getValue());
                throw new IllegalArgumentException("Invalid value: " + productDTO.getValue() + ". Value cannot be negative.");
            }
            existingProduct.setValue(productDTO.getValue());
        }
        logger.info("Partial updates applied to product with ID: {}", existingProduct.getId());
    }

    public List<ProductDTO> createDummyData() {
        List<ProductDTO> dummyProducts = Arrays.asList(
                new ProductDTO(null, "P121", Status.INBOUND, "FC4", 30, 721.4),
                new ProductDTO(null, "P122", Status.SELLABLE, "FC1", 10, 100.0),
                new ProductDTO(null, "P123", Status.SELLABLE, "FC2", 10, 134.0),
                new ProductDTO(null, "P124", Status.UNFULFILLABLE, "FC3", 20, 200.0),
                new ProductDTO(null, "P124", Status.UNFULFILLABLE, "FC3", 20, 847.0),
                new ProductDTO(null, "P125", Status.INBOUND, "FC4", 30, 300.0));
        List<ProductDTO> savedProducts = new ArrayList<>();
        for (ProductDTO productDTO : dummyProducts) {
            savedProducts.add(saveProduct(productDTO));
        }
        return savedProducts;
    }


}
