package com.gulbalasalamov.fulfillment_centers.init;

import com.gulbalasalamov.fulfillment_centers.service.ProductService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final ProductService productService;

    public DataInitializer(ProductService productService) {
        this.productService = productService;
    }

    @Override
    public void run(String... args) throws Exception {
        productService.createDummyData();
        System.out.println("Dummy data has been initialized.");
    }
}
