package com.gulbalasalamov.fulfillment_centers.repository;

import com.gulbalasalamov.fulfillment_centers.model.entity.Product;
import com.gulbalasalamov.fulfillment_centers.model.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product,Long> {
    List<Product> findByStatus(Status status);
}
