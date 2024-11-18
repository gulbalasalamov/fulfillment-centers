package com.gulbalasalamov.fulfillment_centers;

import com.gulbalasalamov.fulfillment_centers.exception.ProductNotFoundException;
import com.gulbalasalamov.fulfillment_centers.model.dto.ProductDTO;
import com.gulbalasalamov.fulfillment_centers.model.entity.Product;
import com.gulbalasalamov.fulfillment_centers.model.enums.Status;
import com.gulbalasalamov.fulfillment_centers.model.mapper.ProductMapper;
import com.gulbalasalamov.fulfillment_centers.repository.ProductRepository;
import com.gulbalasalamov.fulfillment_centers.service.ProductService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    private static final Logger logger = LogManager.getLogger(ProductServiceTest.class);

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    private Product product1;
    private ProductDTO productDTO1;

    @BeforeEach
    void setUp() {
        product1 = new Product(1L, "P123", Status.SELLABLE, "FC1", 10, 100.0);
        productDTO1 = new ProductDTO(1L, "P123", Status.SELLABLE, "FC1", 10, 100.0);
    }

    @Test
    void testGetAllProducts() {
        logger.info("Testing getAllProducts method");
        when(productRepository.findAll()).thenReturn(Arrays.asList(product1));

        List<ProductDTO> result = productService.getAllProducts();

        assertEquals(1, result.size());
        verify(productRepository, times(1)).findAll();
        logger.info("getAllProducts test passed");
    }

    @Test
    void testGetProductsByStatus() {
        logger.info("Testing getProductsByStatus method");
        when(productRepository.findByStatus(Status.SELLABLE)).thenReturn(Arrays.asList(product1));

        List<ProductDTO> result = productService.getProductsByStatus("SELLABLE");

        assertEquals(1, result.size());
        verify(productRepository, times(1)).findByStatus(Status.SELLABLE);
        logger.info("getProductsByStatus test passed");
    }

    @Test
    void testGetProductById() {
        logger.info("Testing getProductById method");
        when(productRepository.findById(1L)).thenReturn(Optional.of(product1));

        ProductDTO result = productService.getProductById(1L);

        assertNotNull(result);
        assertEquals("P123", result.getProductId());
        verify(productRepository, times(1)).findById(1L);
        logger.info("getProductById test passed");
    }

    @Test
    void testGetProductById_NotFound() {
        logger.info("Testing getProductById method for not found scenario");
        when(productRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> productService.getProductById(2L));
        verify(productRepository, times(1)).findById(2L);
        logger.info("getProductById not found test passed");
    }

    @Test
    void testSaveProduct() {
        logger.info("Testing saveProduct method");
        when(productRepository.save(any(Product.class))).thenReturn(product1);

        ProductDTO result = productService.saveProduct(productDTO1);

        assertNotNull(result);
        assertEquals("P123", result.getProductId());
        verify(productRepository, times(1)).save(any(Product.class));
        logger.info("saveProduct test passed");
    }

    @Test
    void testUpdateProduct() {
        logger.info("Testing updateProduct method");
        when(productRepository.findById(1L)).thenReturn(Optional.of(product1));
        when(productRepository.save(any(Product.class))).thenReturn(product1);

        ProductDTO result = productService.updateProduct(1L, productDTO1);

        assertNotNull(result);
        assertEquals("P123", result.getProductId());
        verify(productRepository, times(1)).findById(1L);
        verify(productRepository, times(1)).save(any(Product.class));
        logger.info("updateProduct test passed");
    }

    @Test
    void testDeleteProduct() {
        logger.info("Testing deleteProduct method");
        when(productRepository.existsById(1L)).thenReturn(true);
        doNothing().when(productRepository).deleteById(1L);

        String result = productService.deleteProduct(1L);

        assertEquals("Product with id: 1 deleted successfully", result);
        verify(productRepository, times(1)).existsById(1L);
        verify(productRepository, times(1)).deleteById(1L);
        logger.info("deleteProduct test passed");
    }

    @Test
    void testDeleteProduct_NotFound() {
        logger.info("Testing deleteProduct method for not found scenario");
        when(productRepository.existsById(2L)).thenReturn(false);

        assertThrows(ProductNotFoundException.class, () -> productService.deleteProduct(2L));
        verify(productRepository, times(1)).existsById(2L);
        logger.info("deleteProduct not found test passed");
    }

    @Test
    void testGetTotalValuesByStatus() {
        logger.info("Testing getTotalValuesByStatus method");
        when(productRepository.findByStatus(Status.SELLABLE)).thenReturn(Arrays.asList(product1));

        double result = productService.getTotalValuesByStatus("SELLABLE");

        assertEquals(100.0, result);
        verify(productRepository, times(1)).findByStatus(Status.SELLABLE);
        logger.info("getTotalValuesByStatus test passed");
    }
}
