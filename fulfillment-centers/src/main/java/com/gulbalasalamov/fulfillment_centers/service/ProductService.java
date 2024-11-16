package com.gulbalasalamov.fulfillment_centers.service;

import com.gulbalasalamov.fulfillment_centers.exception.ProductNotFoundException;
import com.gulbalasalamov.fulfillment_centers.model.dto.ProductDTO;
import com.gulbalasalamov.fulfillment_centers.model.entity.Product;
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
        List<Product> products = productRepository.findByStatus(status);
        return products.stream()
                .map(ProductMapper::toDto)
                .collect(Collectors.toList());
    }

    public ProductDTO getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product with id: " + id + " not found"));
        return ProductMapper.toDto(product);
    }

    public ProductDTO saveProduct(ProductDTO productDTO) {
        Product product = ProductMapper.toEntity(productDTO);
        product = productRepository.save(product);
        return ProductMapper.toDto(product);
    }

    public ProductDTO updateProduct(Long id, ProductDTO productDTO) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product with id: " + id + " not found"));
        product.setProductId(productDTO.getProductId());
        product.setStatus(productDTO.getStatus());
        product.setFulfillmentCenter(productDTO.getFulfillmentCenter());
        product.setQuantity(productDTO.getQuantity());
        product.setValue(product.getValue());
        product = productRepository.save(product);
        return ProductMapper.toDto(product);
    }

    public String deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ProductNotFoundException("Product with id: " + id + " not found");
        }
        productRepository.deleteById(id);
        return "Product with id: " + id + " deleted successfully";
    }

    public double getTotalValuesByStatus(String status) {
        return productRepository.findByStatus(status)
                .stream()
                .mapToDouble(Product::getValue)
                .sum();
    }
}
