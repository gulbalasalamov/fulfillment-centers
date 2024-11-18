package com.gulbalasalamov.fulfillment_centers.controller;

import com.gulbalasalamov.fulfillment_centers.model.dto.ProductDTO;
import com.gulbalasalamov.fulfillment_centers.model.enums.Status;
import com.gulbalasalamov.fulfillment_centers.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable long id) {
        ProductDTO productDTO = productService.getProductById(id);
        return new ResponseEntity<>(productDTO,HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<ProductDTO>> getAllProducts() {
        List<ProductDTO> allProducts = productService.getAllProducts();
        return new ResponseEntity<>(allProducts, HttpStatus.OK);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<ProductDTO>> getProductByStatus(@PathVariable String status) {
        List<ProductDTO> productsByStatus = productService.getProductsByStatus(status);
        return new ResponseEntity<>(productsByStatus, HttpStatus.OK);
    }

    @GetMapping("/total-value/status/{status}")
    public ResponseEntity<Double> getTotalValueByStatus(@PathVariable String status) {
        Status statusEnum = Status.valueOf(status.toUpperCase());
        double totalValuesByStatus = productService.getTotalValuesByStatus(statusEnum);
        return new ResponseEntity<>(totalValuesByStatus, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<ProductDTO> createProduct(@RequestBody ProductDTO productDTO) {
        ProductDTO savedProduct = productService.saveProduct(productDTO);
        return new ResponseEntity<>(savedProduct,HttpStatus.CREATED);
    }

    @PatchMapping("/update/{id}")
    public ResponseEntity<ProductDTO> updateProduct(@PathVariable long id, @RequestBody ProductDTO productDTO) {
        ProductDTO updatedProduct = productService.updateProduct(id, productDTO);
        return new ResponseEntity<>(updatedProduct,HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id) {
        String responseMessage = productService.deleteProduct(id);
        return new ResponseEntity<>(responseMessage, HttpStatus.OK);
    }
}
