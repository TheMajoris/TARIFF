package com.cs203.core.repository;

import com.cs203.core.entity.ProductCategoriesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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
    boolean existsByCategoryCode(String categoryCode);
    
    // Check if category name exists
    boolean existsByCategoryName(String categoryName);
    
    // Find all active categories
    List<ProductCategoriesEntity> findByIsActiveTrue();
    
    // Find all inactive categories
    List<ProductCategoriesEntity> findByIsActiveFalse();
    
    // Find categories by partial name (case insensitive)
    List<ProductCategoriesEntity> findByCategoryNameContainingIgnoreCase(String categoryName);
    
    // Find categories by partial code (case insensitive)
    List<ProductCategoriesEntity> findByCategoryCodeContainingIgnoreCase(String categoryCode);
    
    // Find categories by tariff rate range
    List<ProductCategoriesEntity> findByTariffBaseRateBetween(Double minRate, Double maxRate);
    
    // Find categories with tariff rate greater than specified value
    List<ProductCategoriesEntity> findByTariffBaseRateGreaterThan(Double rate);
    
    // Find categories with tariff rate less than specified value
    List<ProductCategoriesEntity> findByTariffBaseRateLessThan(Double rate);
    
    // Find active categories ordered by name
    List<ProductCategoriesEntity> findByIsActiveTrueOrderByCategoryNameAsc();
    
    // Find active categories ordered by tariff rate
    List<ProductCategoriesEntity> findByIsActiveTrueOrderByTariffBaseRateAsc();
    
    // Search categories by any field containing text
    @Query("SELECT p FROM ProductCategoriesEntity p WHERE " +
           "LOWER(p.categoryCode) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(p.categoryName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(p.description) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<ProductCategoriesEntity> searchCategories(@Param("searchTerm") String searchTerm);
    
    // Count active categories
    long countByIsActiveTrue();
    
    // Count inactive categories
    long countByIsActiveFalse();
    
    // Find all categories ordered by name
    List<ProductCategoriesEntity> findAllByOrderByCategoryNameAsc();
    
    // Find all categories ordered by tariff rate
    List<ProductCategoriesEntity> findAllByOrderByTariffBaseRateAsc();
}
