package com.cs203.core.repository;

import com.cs203.core.entity.NationalTariffLinesEntity;
import com.cs203.core.entity.CountryEntity;
import com.cs203.core.entity.ProductCategoriesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface NationalTariffLinesRepository extends JpaRepository<NationalTariffLinesEntity, Long> {
    
    // Find by tariff line code
    Optional<NationalTariffLinesEntity> findByTariffLineCode(String tariffLineCode);
    
    // Find national tariffs by country
    List<NationalTariffLinesEntity> findByCountry(CountryEntity country);
    
    // Find national tariffs by country ID
    List<NationalTariffLinesEntity> findByCountryId(Long countryId);
    
    // Find by parent HS code
    List<NationalTariffLinesEntity> findByParentHsCode(ProductCategoriesEntity parentHsCode);
    
    // Find by level
    List<NationalTariffLinesEntity> findByLevel(Integer level);
    
    // Find by country and level
    List<NationalTariffLinesEntity> findByCountryAndLevel(CountryEntity country, Integer level);
    
    // Find by country ID and level
    List<NationalTariffLinesEntity> findByCountryIdAndLevel(Long countryId, Integer level);
    
    // Find by tariff line code containing (for partial matches)
    List<NationalTariffLinesEntity> findByTariffLineCodeContaining(String tariffLineCode);
    
    // Custom query to find tariff lines by country code
    @Query("SELECT ntl FROM NationalTariffLinesEntity ntl JOIN ntl.country c WHERE c.countryCode = :countryCode")
    List<NationalTariffLinesEntity> findByCountryCode(@Param("countryCode") String countryCode);
    
    // Custom query to find tariff lines by HS code and country
    @Query("SELECT ntl FROM NationalTariffLinesEntity ntl WHERE ntl.parentHsCode.categoryCode = :hsCode AND ntl.country.id = :countryId")
    List<NationalTariffLinesEntity> findByHsCodeAndCountryId(@Param("hsCode") Integer hsCode, @Param("countryId") Long countryId);
    
    // Check if tariff line exists for country and code
    boolean existsByCountryAndTariffLineCode(CountryEntity country, String tariffLineCode);

    @Query("SELECT COUNT(ntl) FROM NationalTariffLinesEntity ntl WHERE ntl.parentHsCode.categoryCode = :hsCode")
    long countByHsCode(@Param("hsCode") Integer hsCode);

    @Query("SELECT ntl.id FROM NationalTariffLinesEntity ntl WHERE ntl.parentHsCode.categoryCode = :hsCode")
    List<Long> findIdsByHsCode(@Param("hsCode") Integer hsCode);
}
