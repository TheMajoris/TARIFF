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
    List<TariffRateEntity> findByImportingCountryIdAndExportingCountryIdAndHsCode(
        Long importingCountryId, Long exportingCountryId, Integer hsCode);
    
    // Find current active tariff rate for a specific trade scenario
    @Query("SELECT t FROM TariffRateEntity t WHERE " +
           "t.importingCountryId = :importingCountryId AND " +
           "t.exportingCountryId = :exportingCountryId AND " +
           "t.hsCode = :hsCode AND " +
           "t.effectiveDate <= :currentDate AND " +
           "(t.expiryDate IS NULL OR t.expiryDate >= :currentDate) " +
           "ORDER BY t.effectiveDate DESC")
    Optional<TariffRateEntity> findCurrentTariffRate(
        @Param("importingCountryId") Long importingCountryId,
        @Param("exportingCountryId") Long exportingCountryId,
        @Param("hsCode") Integer hsCode,
        @Param("currentDate") LocalDate currentDate);
    
    // Find all tariff rates by importing country
    List<TariffRateEntity> findByImportingCountryId(Long importingCountryId);
    
    // Find all tariff rates by exporting country
    List<TariffRateEntity> findByExportingCountryId(Long exportingCountryId);
    
    // Find all tariff rates for a specific HS code
    List<TariffRateEntity> findByHsCode(Integer hsCode);
    
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
    List<TariffRateEntity> findByImportingCountryIdAndExportingCountryId(
        Long importingCountryId, Long exportingCountryId);
    
    // Find preferential rates for country pair
    List<TariffRateEntity> findByImportingCountryIdAndExportingCountryIdAndPreferentialTariffTrue(
        Long importingCountryId, Long exportingCountryId);
    
    // Count tariff rates by importing country
    long countByImportingCountryId(Long importingCountryId);
    
    // Count tariff rates by exporting country
    long countByExportingCountryId(Long exportingCountryId);
    
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
    List<TariffRateEntity> findByImportingCountryIdOrderByHsCodeAsc(Long importingCountryId);
    
    // Check if a tariff rate exists for specific trade scenario
    boolean existsByImportingCountryIdAndExportingCountryIdAndHsCode(
        Long importingCountryId, Long exportingCountryId, Integer hsCode);
    
    // Complex query to find competitive rates (comparing with other exporters)
    @Query("SELECT t FROM TariffRateEntity t WHERE " +
           "t.importingCountryId = :importingCountryId AND " +
           "t.hsCode = :hsCode AND " +
           "t.effectiveDate <= :currentDate AND " +
           "(t.expiryDate IS NULL OR t.expiryDate >= :currentDate) " +
           "ORDER BY t.tariffRate ASC")
    List<TariffRateEntity> findCompetitiveRatesForProduct(
        @Param("importingCountryId") Long importingCountryId,
        @Param("hsCode") Integer hsCode,
        @Param("currentDate") LocalDate currentDate);
}
