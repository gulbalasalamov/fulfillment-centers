package com.gulbalasalamov.fulfillment_centers.repository;

import com.gulbalasalamov.fulfillment_centers.model.entity.Product;
import com.gulbalasalamov.fulfillment_centers.model.enums.Status;
import com.gulbalasalamov.fulfillment_centers.service.ProductService;
import jakarta.persistence.criteria.Predicate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

/**
 * These fields make queries dynamic and allow us to further customize filters to return only products with the required conditions.
 */

public class ProductSpecification {
    private static final Logger logger = LogManager.getLogger(ProductSpecification.class);

    public static Specification<Product> withFilters(String productId, Status status, String fulfillmentCenter, Integer minQuantity, Integer maxQuantity, Double minValue, Double maxValue) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (productId != null && !productId.isEmpty()) {
                logger.info("Adding productId filter: {}", productId);
                predicates.add(criteriaBuilder.equal(root.get("productId"), productId));
            }
            if (status != null) {
                logger.info("Adding status filter: {}", status);
                predicates.add(criteriaBuilder.equal(root.get("status"), status));
            }
            if (fulfillmentCenter != null && !fulfillmentCenter.isEmpty()) {
                logger.info("Adding fulfillmentCenter filter: {}", fulfillmentCenter);
                predicates.add(criteriaBuilder.equal(root.get("fulfillmentCenter"), fulfillmentCenter));
            }
            if (minQuantity != null) {
                logger.info("Adding minQuantity filter: {}", minQuantity);
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("quantity"), minQuantity));
            }
            if (maxQuantity != null) {
                logger.info("Adding maxQuantity filter: {}", maxQuantity);
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("quantity"), maxQuantity));
            }
            if (minValue != null) {
                logger.info("Adding minValue filter: {}", minValue);
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("value"), minValue));
            }
            if (maxValue != null) {
                logger.info("Adding maxValue filter: {}", maxValue);
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("value"), maxValue));
            }

            logger.info("Predicates: {}", predicates);
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

}