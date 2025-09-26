package com.cs203.core.repository;

import com.cs203.core.entity.ProductCategoriesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductCategoriesRepository extends JpaRepository<ProductCategoriesEntity, Long> {

    // Find category by category code
    Optional<ProductCategoriesEntity> findByCategoryCode(int categoryCode);

    // Find category by name
    Optional<ProductCategoriesEntity> findByCategoryName(String categoryName);

    // Check if category code exists
    boolean existsByCategoryCode(int categoryCode);

    // Check if category name exists
    boolean existsByCategoryName(String categoryName);

    // Find all active categories
    List<ProductCategoriesEntity> findByIsActiveTrue();

    // Find all inactive categories
    List<ProductCategoriesEntity> findByIsActiveFalse();

    // Find categories by partial name (case insensitive)
    List<ProductCategoriesEntity> findByCategoryNameContainingIgnoreCase(String categoryName);

    // Find active categories ordered by name
    List<ProductCategoriesEntity> findByIsActiveTrueOrderByCategoryNameAsc();

    // Count active categories
    long countByIsActiveTrue();

    // Count inactive categories
    long countByIsActiveFalse();

    // Find all categories ordered by name
    List<ProductCategoriesEntity> findAllByOrderByCategoryNameAsc();
}
