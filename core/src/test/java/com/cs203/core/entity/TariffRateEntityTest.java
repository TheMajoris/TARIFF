package com.cs203.core.entity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("TariffRateEntity Tests")
class TariffRateEntityTest {

    @Test
    @DisplayName("Should create valid TariffRateEntity")
    void shouldCreateValidTariffRateEntity() {
        // Create entity objects
        CountryEntity importingCountry = new CountryEntity();
        importingCountry.setId(1L);
        importingCountry.setCountryCode("US");
        
        CountryEntity exportingCountry = new CountryEntity();
        exportingCountry.setId(2L);
        exportingCountry.setCountryCode("SG");
        
        ProductCategoriesEntity productCategory = new ProductCategoriesEntity();
        productCategory.setCategoryCode(850110);
        productCategory.setCategoryName("Electronics");
        
        TariffRateEntity tariffRate = new TariffRateEntity(
            importingCountry, 
            exportingCountry, 
            productCategory,
            new BigDecimal("5.25"), 
            "AD_VALOREM", 
            "PERCENT", 
            LocalDate.of(2024, 1, 1), 
            LocalDate.of(2025, 12, 31),
            false 
        );

        assertEquals(1L, tariffRate.getImportingCountryId());
        assertEquals(2L, tariffRate.getExportingCountryId());
        assertEquals(850110, tariffRate.getHsCode());
        assertEquals(new BigDecimal("5.25"), tariffRate.getTariffRate());
        assertEquals("AD_VALOREM", tariffRate.getTariffType());
        assertEquals("PERCENT", tariffRate.getRateUnit());
        assertEquals(LocalDate.of(2024, 1, 1), tariffRate.getEffectiveDate());
        assertEquals(LocalDate.of(2025, 12, 31), tariffRate.getExpiryDate());
        assertFalse(tariffRate.getPreferentialTariff());
    }

    @Test
    @DisplayName("Should validate required fields")
    void shouldValidateRequiredFields() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        
        TariffRateEntity tariffRate = new TariffRateEntity();
        
        Set<ConstraintViolation<TariffRateEntity>> violations = validator.validate(tariffRate);
        assertFalse(violations.isEmpty());
        
        // Should have violations for required fields
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Importing country is required")));
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Exporting country is required")));
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("HS code is required")));
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Tariff rate is required")));
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Tariff type is required")));
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Effective date is required")));
    }

    @Test
    @DisplayName("Should validate tariff rate constraints")
    void shouldValidateTariffRateConstraints() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        
        // Create entity objects for testing
        CountryEntity importingCountry = new CountryEntity();
        importingCountry.setId(1L);
        importingCountry.setCountryCode("US");
        
        CountryEntity exportingCountry = new CountryEntity();
        exportingCountry.setId(2L);
        exportingCountry.setCountryCode("SG");
        
        ProductCategoriesEntity productCategory = new ProductCategoriesEntity();
        productCategory.setCategoryCode(850110);
        productCategory.setCategoryName("Electronics");
        
        TariffRateEntity tariffRate = new TariffRateEntity();
        tariffRate.setImportingCountry(importingCountry);
        tariffRate.setExportingCountry(exportingCountry);
        tariffRate.setProductCategory(productCategory);
        tariffRate.setTariffType("AD_VALOREM");
        tariffRate.setEffectiveDate(LocalDate.now());
        
        // Test negative tariff rate
        tariffRate.setTariffRate(new BigDecimal("-1.0"));
        Set<ConstraintViolation<TariffRateEntity>> violations = validator.validate(tariffRate);
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("non-negative")));
        
        // Test tariff rate too high
        tariffRate.setTariffRate(new BigDecimal("1000.0"));
        violations = validator.validate(tariffRate);
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("cannot exceed 999.9999")));
        
        // Test valid tariff rate
        tariffRate.setTariffRate(new BigDecimal("5.25"));
        violations = validator.validate(tariffRate);
        assertTrue(violations.stream().noneMatch(v -> v.getPropertyPath().toString().equals("tariffRate")));
    }

    @Test
    @DisplayName("Should handle helper methods correctly")
    void shouldHandleHelperMethodsCorrectly() {
        LocalDate now = LocalDate.now();
        
        // Test currently active rate
        TariffRateEntity activeRate = new TariffRateEntity();
        activeRate.setEffectiveDate(now.minusDays(30)); // Started 30 days ago
        activeRate.setExpiryDate(now.plusDays(30)); // Expires in 30 days
        
        assertTrue(activeRate.isCurrentlyActive());
        assertTrue(activeRate.isEffective());
        assertFalse(activeRate.isExpired());
        
        // Test expired rate
        TariffRateEntity expiredRate = new TariffRateEntity();
        expiredRate.setEffectiveDate(now.minusDays(60));
        expiredRate.setExpiryDate(now.minusDays(30)); // Expired 30 days ago
        
        assertFalse(expiredRate.isCurrentlyActive());
        assertTrue(expiredRate.isEffective());
        assertTrue(expiredRate.isExpired());
        
        // Test future rate
        TariffRateEntity futureRate = new TariffRateEntity();
        futureRate.setEffectiveDate(now.plusDays(30)); // Starts in 30 days
        futureRate.setExpiryDate(now.plusDays(60));
        
        assertFalse(futureRate.isCurrentlyActive());
        assertFalse(futureRate.isEffective());
        assertFalse(futureRate.isExpired());
        
        // Test permanent rate (no expiry)
        TariffRateEntity permanentRate = new TariffRateEntity();
        permanentRate.setEffectiveDate(now.minusDays(30));
        permanentRate.setExpiryDate(null); // No expiry date
        
        assertTrue(permanentRate.isCurrentlyActive());
        assertTrue(permanentRate.isEffective());
        assertFalse(permanentRate.isExpired());
    }

    @Test
    @DisplayName("Should set default values correctly")
    void shouldSetDefaultValuesCorrectly() {
        TariffRateEntity tariffRate = new TariffRateEntity();
        
        // Default preferential tariff should be false
        assertFalse(tariffRate.getPreferentialTariff());
    }

    @Test
    @DisplayName("Should handle all getters and setters")
    void shouldHandleAllGettersAndSetters() {
        // Create entity objects
        CountryEntity importingCountry = new CountryEntity();
        importingCountry.setId(1L);
        importingCountry.setCountryCode("US");
        
        CountryEntity exportingCountry = new CountryEntity();
        exportingCountry.setId(2L);
        exportingCountry.setCountryCode("SG");
        
        ProductCategoriesEntity productCategory = new ProductCategoriesEntity();
        productCategory.setCategoryCode(850110);
        productCategory.setCategoryName("Electronics");
        
        TariffRateEntity tariffRate = new TariffRateEntity();
        
        tariffRate.setId(100L);
        tariffRate.setImportingCountry(importingCountry);
        tariffRate.setExportingCountry(exportingCountry);
        tariffRate.setProductCategory(productCategory);
        tariffRate.setTariffRate(new BigDecimal("7.5"));
        tariffRate.setTariffType("SPECIFIC");
        tariffRate.setRateUnit("USD_PER_KG");
        tariffRate.setEffectiveDate(LocalDate.of(2024, 1, 1));
        tariffRate.setExpiryDate(LocalDate.of(2024, 12, 31));
        tariffRate.setPreferentialTariff(true);
        
        assertEquals(100L, tariffRate.getId());
        assertEquals(1L, tariffRate.getImportingCountryId());
        assertEquals(2L, tariffRate.getExportingCountryId());
        assertEquals(850110, tariffRate.getHsCode());
        assertEquals(new BigDecimal("7.5"), tariffRate.getTariffRate());
        assertEquals("SPECIFIC", tariffRate.getTariffType());
        assertEquals("USD_PER_KG", tariffRate.getRateUnit());
        assertEquals(LocalDate.of(2024, 1, 1), tariffRate.getEffectiveDate());
        assertEquals(LocalDate.of(2024, 12, 31), tariffRate.getExpiryDate());
        assertTrue(tariffRate.getPreferentialTariff());
    }
}
