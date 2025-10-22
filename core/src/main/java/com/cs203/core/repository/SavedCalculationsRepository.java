package com.cs203.core.repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cs203.core.entity.CountryEntity;
import com.cs203.core.entity.ProductCategoriesEntity;
import com.cs203.core.entity.SavedCalculationsEntity;
import com.cs203.core.entity.UserEntity;

@Repository
public interface SavedCalculationsRepository extends JpaRepository<SavedCalculationsEntity, Long> {
    
    // Find calculations by user
    List<SavedCalculationsEntity> findByUser(UserEntity user);
    
    // Find calculations by user ID
    List<SavedCalculationsEntity> findByUserId(Long userId);
    
    // Find calculation by name and user
    Optional<SavedCalculationsEntity> findByCalculationNameAndUser(String calculationName, UserEntity user);
    
    // Find calculations by user ordered by creation date (newest first)
    List<SavedCalculationsEntity> findByUserOrderByCreatedAtDesc(UserEntity user);
    
    // Find calculations by user ordered by calculation name
    List<SavedCalculationsEntity> findByUserOrderByCalculationNameAsc(UserEntity user);
    
    // Find calculations by importing country
    List<SavedCalculationsEntity> findByImportingCountry(CountryEntity importingCountry);
    
    // Find calculations by exporting country
    List<SavedCalculationsEntity> findByExportingCountry(CountryEntity exportingCountry);
    
    // Find calculations by product category
    List<SavedCalculationsEntity> findByProductCategory(ProductCategoriesEntity productCategory);
    
    // Find calculations by currency code
    List<SavedCalculationsEntity> findByCurrencyCode(String currencyCode);
    
    // Find calculations by tariff type
    List<SavedCalculationsEntity> findByTariffType(String tariffType);
    
    // Find calculations by user and currency code
    List<SavedCalculationsEntity> findByUserAndCurrencyCode(UserEntity user, String currencyCode);
    
    // Find calculations by user and tariff type
    List<SavedCalculationsEntity> findByUserAndTariffType(UserEntity user, String tariffType);
    
    // Find calculations by user and importing country
    List<SavedCalculationsEntity> findByUserAndImportingCountry(UserEntity user, CountryEntity importingCountry);
    
    // Find calculations by user and exporting country
    List<SavedCalculationsEntity> findByUserAndExportingCountry(UserEntity user, CountryEntity exportingCountry);
    
    // Find calculations by user and product category
    List<SavedCalculationsEntity> findByUserAndProductCategory(UserEntity user, ProductCategoriesEntity productCategory);
    
    // Find calculations with product value greater than specified amount
    List<SavedCalculationsEntity> findByProductValueGreaterThan(BigDecimal productValue);
    
    // Find calculations with total cost greater than specified amount
    List<SavedCalculationsEntity> findByTotalCostGreaterThan(BigDecimal totalCost);
    
    // Find calculations by user with product value greater than specified amount
    List<SavedCalculationsEntity> findByUserAndProductValueGreaterThan(UserEntity user, BigDecimal productValue);
    
    // Find calculations by user with total cost greater than specified amount
    List<SavedCalculationsEntity> findByUserAndTotalCostGreaterThan(UserEntity user, BigDecimal totalCost);
    
    // Find calculations created after specified date
    List<SavedCalculationsEntity> findByCreatedAtAfter(LocalDateTime createdAt);
    
    // Find calculations created between dates
    List<SavedCalculationsEntity> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    // Find calculations by user created between dates
    List<SavedCalculationsEntity> findByUserAndCreatedAtBetween(UserEntity user, LocalDateTime startDate, LocalDateTime endDate);
    
    // Search calculations by calculation name (case insensitive, partial match)
    List<SavedCalculationsEntity> findByCalculationNameContainingIgnoreCase(String calculationName);
    
    // Search calculations by user and calculation name (case insensitive, partial match)
    List<SavedCalculationsEntity> findByUserAndCalculationNameContainingIgnoreCase(UserEntity user, String calculationName);
    
    // Search calculations by notes (case insensitive, partial match)
    List<SavedCalculationsEntity> findByNotesContainingIgnoreCase(String notes);
    
    // Check if calculation name exists for user
    boolean existsByCalculationNameAndUser(String calculationName, UserEntity user);
    
    // Count calculations by user
    long countByUser(UserEntity user);
    
    // Count calculations by currency code
    long countByCurrencyCode(String currencyCode);
    
    // Count calculations by tariff type
    long countByTariffType(String tariffType);
    
    // Count calculations by importing country
    long countByImportingCountry(CountryEntity importingCountry);
    
    // Count calculations by exporting country
    long countByExportingCountry(CountryEntity exportingCountry);
    
    // Count calculations by product category
    long countByProductCategory(ProductCategoriesEntity productCategory);
    
    // Find all unique currency codes used in calculations
    @Query("SELECT DISTINCT s.currencyCode FROM SavedCalculationsEntity s WHERE s.currencyCode IS NOT NULL ORDER BY s.currencyCode")
    List<String> findAllCurrencyCodes();
    
    // Find all unique tariff types used in calculations
    @Query("SELECT DISTINCT s.tariffType FROM SavedCalculationsEntity s WHERE s.tariffType IS NOT NULL ORDER BY s.tariffType")
    List<String> findAllTariffTypes();
    
    // Find calculations with comprehensive search across multiple fields
    @Query("SELECT s FROM SavedCalculationsEntity s WHERE " +
           "LOWER(s.calculationName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(s.currencyCode) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(s.tariffType) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(s.notes) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<SavedCalculationsEntity> searchCalculations(@Param("searchTerm") String searchTerm);
    
    // Find calculations by user with comprehensive search
    @Query("SELECT s FROM SavedCalculationsEntity s WHERE s.user = :user AND (" +
           "LOWER(s.calculationName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(s.currencyCode) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(s.tariffType) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(s.notes) LIKE LOWER(CONCAT('%', :searchTerm, '%')))")
    List<SavedCalculationsEntity> searchCalculationsByUser(@Param("user") UserEntity user, @Param("searchTerm") String searchTerm);
    
    // Calculate average product value for user
    @Query("SELECT AVG(s.productValue) FROM SavedCalculationsEntity s WHERE s.user = :user")
    BigDecimal calculateAverageProductValueByUser(@Param("user") UserEntity user);
    
    // Calculate average total cost for user
    @Query("SELECT AVG(s.totalCost) FROM SavedCalculationsEntity s WHERE s.user = :user")
    BigDecimal calculateAverageTotalCostByUser(@Param("user") UserEntity user);
    
    // Calculate sum of total costs for user
    @Query("SELECT SUM(s.totalCost) FROM SavedCalculationsEntity s WHERE s.user = :user")
    BigDecimal calculateTotalCostSumByUser(@Param("user") UserEntity user);
    
    // Find top N calculations by total cost for user
    @Query("SELECT s FROM SavedCalculationsEntity s WHERE s.user = :user ORDER BY s.totalCost DESC")
    List<SavedCalculationsEntity> findTopCalculationsByTotalCost(@Param("user") UserEntity user);
    
    // Find calculations ordered by creation date (newest first)
    List<SavedCalculationsEntity> findAllByOrderByCreatedAtDesc();
    
    // Find calculations ordered by calculation name
    List<SavedCalculationsEntity> findAllByOrderByCalculationNameAsc();
    
    // Find calculations ordered by total cost (highest first)
    List<SavedCalculationsEntity> findAllByOrderByTotalCostDesc();
}
