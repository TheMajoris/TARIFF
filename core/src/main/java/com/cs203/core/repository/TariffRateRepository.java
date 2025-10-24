package com.cs203.core.repository;

import com.cs203.core.entity.TariffRateEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface TariffRateRepository extends JpaRepository<TariffRateEntity, Long> {
    
    // Find tariff rates by country pair and HS code
    @Query("SELECT t FROM TariffRateEntity t WHERE " +
           "t.importingCountry.id = :importingCountryId AND " +
           "t.exportingCountry.id = :exportingCountryId AND " +
           "t.productCategory.categoryCode = :hsCode")
    List<TariffRateEntity> findByImportingCountryIdAndExportingCountryIdAndHsCode(
        @Param("importingCountryId") Long importingCountryId, 
        @Param("exportingCountryId") Long exportingCountryId, 
        @Param("hsCode") Integer hsCode);
    
    // Find current active tariff rate for a specific trade scenario
    @Query("SELECT t FROM TariffRateEntity t WHERE " +
           "t.importingCountry.id = :importingCountryId AND " +
           "t.exportingCountry.id = :exportingCountryId AND " +
           "t.productCategory.categoryCode = :hsCode AND " +
           "t.effectiveDate <= :currentDate AND " +
           "(t.expiryDate IS NULL OR t.expiryDate >= :currentDate) " +
           "ORDER BY t.effectiveDate DESC")
    Optional<TariffRateEntity> findCurrentTariffRate(
        @Param("importingCountryId") Long importingCountryId,
        @Param("exportingCountryId") Long exportingCountryId,
        @Param("hsCode") Integer hsCode,
        @Param("currentDate") LocalDate currentDate);
    
    // Find all tariff rates by importing country
    @Query("SELECT t FROM TariffRateEntity t WHERE t.importingCountry.id = :importingCountryId")
    List<TariffRateEntity> findByImportingCountryId(@Param("importingCountryId") Long importingCountryId);
    
    // Find all tariff rates by exporting country
    @Query("SELECT t FROM TariffRateEntity t WHERE t.exportingCountry.id = :exportingCountryId")
    List<TariffRateEntity> findByExportingCountryId(@Param("exportingCountryId") Long exportingCountryId);
    
    // Find all tariff rates for a specific HS code
    @Query("SELECT t FROM TariffRateEntity t WHERE t.productCategory.categoryCode = :hsCode")
    List<TariffRateEntity> findByHsCode(@Param("hsCode") Integer hsCode);

    // Find all tariff rates ids for a specific HS code
    @Query("SELECT t.id FROM TariffRateEntity t WHERE t.productCategory.categoryCode = :hsCode")
    List<Long> findIdsByHsCode(@Param("hsCode") Integer hsCode);

    // Count all tariff rates for a specific HS code
    @Query("SELECT COUNT(t) FROM TariffRateEntity t WHERE t.productCategory.categoryCode = :hsCode")
    long countByHsCode(@Param("hsCode") Integer hsCode);
    
    // Find tariff rates by type
    List<TariffRateEntity> findByTariffType(String tariffType);
    
    // Find preferential tariff rates
    List<TariffRateEntity> findByPreferentialTariffTrue();
    
    // Find non-preferential tariff rates
    List<TariffRateEntity> findByPreferentialTariffFalse();
    
    // Find tariff rates by rate range
    List<TariffRateEntity> findByTariffRateBetween(BigDecimal minRate, BigDecimal maxRate);
    
    // Find tariff rates greater than specified rate
    List<TariffRateEntity> findByTariffRateGreaterThan(BigDecimal rate);
    
    // Find tariff rates less than specified rate
    List<TariffRateEntity> findByTariffRateLessThan(BigDecimal rate);
    
    // Find tariff rates effective on or after a date
    List<TariffRateEntity> findByEffectiveDateGreaterThanEqual(LocalDate date);
    
    // Find tariff rates expiring on or before a date
    List<TariffRateEntity> findByExpiryDateLessThanEqual(LocalDate date);
    
    // Find tariff rates without expiry date (permanent rates)
    List<TariffRateEntity> findByExpiryDateIsNull();
    
    // Find currently active tariff rates
    @Query("SELECT t FROM TariffRateEntity t WHERE " +
           "t.effectiveDate <= :currentDate AND " +
           "(t.expiryDate IS NULL OR t.expiryDate >= :currentDate)")
    List<TariffRateEntity> findActiveRates(@Param("currentDate") LocalDate currentDate);
    
    // Find expired tariff rates
    @Query("SELECT t FROM TariffRateEntity t WHERE " +
           "t.expiryDate IS NOT NULL AND t.expiryDate < :currentDate")
    List<TariffRateEntity> findExpiredRates(@Param("currentDate") LocalDate currentDate);
    
    // Find future tariff rates (not yet effective)
    @Query("SELECT t FROM TariffRateEntity t WHERE t.effectiveDate > :currentDate")
    List<TariffRateEntity> findFutureRates(@Param("currentDate") LocalDate currentDate);
    
    // Find tariff rates by country pair
    @Query("SELECT t FROM TariffRateEntity t WHERE " +
           "t.importingCountry.id = :importingCountryId AND " +
           "t.exportingCountry.id = :exportingCountryId")
    List<TariffRateEntity> findByImportingCountryIdAndExportingCountryId(
        @Param("importingCountryId") Long importingCountryId, 
        @Param("exportingCountryId") Long exportingCountryId);
    
    // Find preferential rates for country pair
    @Query("SELECT t FROM TariffRateEntity t WHERE " +
           "t.importingCountry.id = :importingCountryId AND " +
           "t.exportingCountry.id = :exportingCountryId AND " +
           "t.preferentialTariff = true")
    List<TariffRateEntity> findByImportingCountryIdAndExportingCountryIdAndPreferentialTariffTrue(
        @Param("importingCountryId") Long importingCountryId, 
        @Param("exportingCountryId") Long exportingCountryId);
    
    // Count tariff rates by importing country
    @Query("SELECT COUNT(t) FROM TariffRateEntity t WHERE t.importingCountry.id = :importingCountryId")
    long countByImportingCountryId(@Param("importingCountryId") Long importingCountryId);
    
    // Count tariff rates by exporting country
    @Query("SELECT COUNT(t) FROM TariffRateEntity t WHERE t.exportingCountry.id = :exportingCountryId")
    long countByExportingCountryId(@Param("exportingCountryId") Long exportingCountryId);
    
    // Count preferential tariff rates
    long countByPreferentialTariffTrue();
    
    // Count active tariff rates
    @Query("SELECT COUNT(t) FROM TariffRateEntity t WHERE " +
           "t.effectiveDate <= :currentDate AND " +
           "(t.expiryDate IS NULL OR t.expiryDate >= :currentDate)")
    long countActiveRates(@Param("currentDate") LocalDate currentDate);
    
    // Find rates ordered by tariff rate (ascending)
    List<TariffRateEntity> findAllByOrderByTariffRateAsc();
    
    // Find rates ordered by effective date (descending - most recent first)
    List<TariffRateEntity> findAllByOrderByEffectiveDateDesc();
    
    // Find rates for importing country ordered by HS code
    @Query("SELECT t FROM TariffRateEntity t WHERE t.importingCountry.id = :importingCountryId ORDER BY t.productCategory.categoryCode ASC")
    List<TariffRateEntity> findByImportingCountryIdOrderByHsCodeAsc(@Param("importingCountryId") Long importingCountryId);
    
    // Check if a tariff rate exists for specific trade scenario
    @Query("SELECT CASE WHEN COUNT(t) > 0 THEN true ELSE false END FROM TariffRateEntity t WHERE " +
           "t.importingCountry.id = :importingCountryId AND " +
           "t.exportingCountry.id = :exportingCountryId AND " +
           "t.productCategory.categoryCode = :hsCode")
    boolean existsByImportingCountryIdAndExportingCountryIdAndHsCode(
        @Param("importingCountryId") Long importingCountryId, 
        @Param("exportingCountryId") Long exportingCountryId, 
        @Param("hsCode") Integer hsCode);
    
    // Complex query to find competitive rates (comparing with other exporters)
    @Query("SELECT t FROM TariffRateEntity t WHERE " +
           "t.importingCountry.id = :importingCountryId AND " +
           "t.productCategory.categoryCode = :hsCode AND " +
           "t.effectiveDate <= :currentDate AND " +
           "(t.expiryDate IS NULL OR t.expiryDate >= :currentDate) " +
           "ORDER BY t.tariffRate ASC")
    List<TariffRateEntity> findCompetitiveRatesForProduct(
        @Param("importingCountryId") Long importingCountryId,
        @Param("hsCode") Integer hsCode,
        @Param("currentDate") LocalDate currentDate);
}
