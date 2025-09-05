package com.cs203.core.repository;

import com.cs203.core.entity.NationalTariffLines;
import com.cs203.core.entity.CountryEntity;
import com.cs203.core.entity.ProductCategoriesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NationalTariffLinesRepository extends JpaRepository<NationalTariffLines, Long> {
    
    // Find by tariff line code
    Optional<NationalTariffLines> findByTariffLineCode(String tariffLineCode);
    
    // Find national tariffs by country
    List<NationalTariffLines> findByCountry(CountryEntity country);
    
    // Find national tariffs by country ID
    List<NationalTariffLines> findByCountryId(Long countryId);
    
    // Find by parent HS code
    List<NationalTariffLines> findByParentHsCode(ProductCategoriesEntity parentHsCode);
    
    // Find by level
    List<NationalTariffLines> findByLevel(Integer level);
    
    // Find by country and level
    List<NationalTariffLines> findByCountryAndLevel(CountryEntity country, Integer level);
    
    // Find by country ID and level
    List<NationalTariffLines> findByCountryIdAndLevel(Long countryId, Integer level);
    
    // Find by tariff line code containing (for partial matches)
    List<NationalTariffLines> findByTariffLineCodeContaining(String tariffLineCode);
    
    // Custom query to find tariff lines by country code
    @Query("SELECT ntl FROM NationalTariffLines ntl WHERE ntl.country.countryCode = :countryCode")
    List<NationalTariffLines> findByCountryCode(@Param("countryCode") String countryCode);
    
    // Custom query to find tariff lines by HS code and country
    @Query("SELECT ntl FROM NationalTariffLines ntl WHERE ntl.parentHsCode.categoryCode = :hsCode AND ntl.country.id = :countryId")
    List<NationalTariffLines> findByHsCodeAndCountryId(@Param("hsCode") Integer hsCode, @Param("countryId") Long countryId);
    
    // Check if tariff line exists for country and code
    boolean existsByCountryAndTariffLineCode(CountryEntity country, String tariffLineCode);
}
