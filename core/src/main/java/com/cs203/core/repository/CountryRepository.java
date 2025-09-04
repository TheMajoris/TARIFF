package com.cs203.core.repository;

import com.cs203.core.entity.CountryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CountryRepository extends JpaRepository<CountryEntity, Long> {
    
    // Find country by country code (e.g., "US", "SG")
    Optional<CountryEntity> findByCountryCode(String countryCode);
    
    // Find country by name
    Optional<CountryEntity> findByCountryName(String countryName);
    
    // Find countries by region
    List<CountryEntity> findByRegion(String region);
    
    // Find countries by currency code (e.g., "USD", "SGD")
    List<CountryEntity> findByCurrencyCode(String currencyCode);
    
    // Check if country code exists
    boolean existsByCountryCode(String countryCode);
    
    // Check if country name exists
    boolean existsByCountryName(String countryName);
    
    // Find countries by partial name (case insensitive)
    List<CountryEntity> findByCountryNameContainingIgnoreCase(String countryName);
    
    // Find countries by region (case insensitive)
    List<CountryEntity> findByRegionIgnoreCase(String region);
    
    // Find all unique regions
    @Query("SELECT DISTINCT c.region FROM CountryEntity c WHERE c.region IS NOT NULL ORDER BY c.region")
    List<String> findAllRegions();
    
    // Find all unique currency codes
    @Query("SELECT DISTINCT c.currencyCode FROM CountryEntity c WHERE c.currencyCode IS NOT NULL ORDER BY c.currencyCode")
    List<String> findAllCurrencyCodes();
    
    // Find countries by multiple regions
    List<CountryEntity> findByRegionIn(List<String> regions);
    
    // Find countries by multiple currency codes
    List<CountryEntity> findByCurrencyCodeIn(List<String> currencyCodes);
    
    // Search countries by any field containing text
    @Query("SELECT c FROM CountryEntity c WHERE " +
           "LOWER(c.countryCode) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(c.countryName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(c.region) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(c.currencyCode) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<CountryEntity> searchCountries(@Param("searchTerm") String searchTerm);
    
    // Count countries by region
    long countByRegion(String region);
    
    // Count countries by currency code
    long countByCurrencyCode(String currencyCode);
    
    // Find countries ordered by name
    List<CountryEntity> findAllByOrderByCountryNameAsc();
    
    // Find countries ordered by region then name
    List<CountryEntity> findAllByOrderByRegionAscCountryNameAsc();
}