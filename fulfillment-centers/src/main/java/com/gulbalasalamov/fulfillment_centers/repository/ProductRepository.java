package com.gulbalasalamov.fulfillment_centers.repository;

import com.gulbalasalamov.fulfillment_centers.model.entity.Product;
import com.gulbalasalamov.fulfillment_centers.model.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * The Specification interface is used to create dynamic and flexible queries. This interface creates SQL queries using the JPA Criteria API.
 */
public interface ProductRepository extends JpaRepository<Product,Long>, JpaSpecificationExecutor<Product> {
    List<Product> findByStatus(Status status);

    @Query("SELECT p FROM Product p WHERE " +
            "(:productId IS NULL OR p.productId = :productId) AND " +
            "(:status IS NULL OR p.status = :status) AND " +
            "(:fulfillmentCenter IS NULL OR p.fulfillmentCenter = :fulfillmentCenter) AND " +
            "(:minQuantity IS NULL OR p.quantity >= :minQuantity) AND " +
            "(:maxQuantity IS NULL OR p.quantity <= :maxQuantity) AND " +
            "(:minValue IS NULL OR p.value >= :minValue) AND " +
            "(:maxValue IS NULL OR p.value <= :maxValue)")
    List<Product> findAllWithFilters(@Param("productId") String productId,
                                     @Param("status") Status status,
                                     @Param("fulfillmentCenter") String fulfillmentCenter,
                                     @Param("minQuantity") Integer minQuantity,
                                     @Param("maxQuantity") Integer maxQuantity,
                                     @Param("minValue") Double minValue,
                                     @Param("maxValue") Double maxValue);

}
