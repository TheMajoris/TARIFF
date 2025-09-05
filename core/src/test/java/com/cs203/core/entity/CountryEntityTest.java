package com.cs203.core.entity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CountryEntity Tests")
class CountryEntityTest {

    @Test
    @DisplayName("Should create valid CountryEntity")
    void shouldCreateValidCountryEntity() {
        CountryEntity country = new CountryEntity(
            "US", 
            "United States", 
            "North America", 
            "USD"
        );

        assertEquals("US", country.getCountryCode());
        assertEquals("United States", country.getCountryName());
        assertEquals("North America", country.getRegion());
        assertEquals("USD", country.getCurrencyCode());
    }

    @Test
    @DisplayName("Should validate required fields")
    void shouldValidateRequiredFields() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        
        CountryEntity country = new CountryEntity();
        
        Set<ConstraintViolation<CountryEntity>> violations = validator.validate(country);
        assertFalse(violations.isEmpty());
        
        // Should have violations for required fields
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Country code is required")));
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Country name is required")));
    }

    @Test
    @DisplayName("Should validate country code constraints")
    void shouldValidateCountryCodeConstraints() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        
        CountryEntity country = new CountryEntity();
        country.setCountryName("Test Country");
        
        // Test country code too short
        country.setCountryCode("A");
        Set<ConstraintViolation<CountryEntity>> violations = validator.validate(country);
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Country code must be 2 characters")));
        
        // Test country code too long
        country.setCountryCode("ABC");
        violations = validator.validate(country);
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Country code must be 2 characters")));
        
        // Test valid country code
        country.setCountryCode("US");
        violations = validator.validate(country);
        assertTrue(violations.stream().noneMatch(v -> v.getPropertyPath().toString().equals("countryCode")));
    }

    @Test
    @DisplayName("Should validate country name constraints")
    void shouldValidateCountryNameConstraints() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        
        CountryEntity country = new CountryEntity();
        country.setCountryCode("US");
        
        // Test empty country name
        country.setCountryName("");
        Set<ConstraintViolation<CountryEntity>> violations = validator.validate(country);
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Country name is required")));
        
        // Test country name too long
        String longName = "A".repeat(101);
        country.setCountryName(longName);
        violations = validator.validate(country);
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Country name cannot exceed 100 characters")));
        
        // Test valid country name
        country.setCountryName("United States");
        violations = validator.validate(country);
        assertTrue(violations.stream().noneMatch(v -> v.getPropertyPath().toString().equals("countryName")));
    }

    @Test
    @DisplayName("Should validate region constraints")
    void shouldValidateRegionConstraints() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        
        CountryEntity country = new CountryEntity("US", "United States", null, null);
        
        // Test region too long
        String longRegion = "A".repeat(101);
        country.setRegion(longRegion);
        Set<ConstraintViolation<CountryEntity>> violations = validator.validate(country);
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Region cannot exceed 100 characters")));
        
        // Test valid region
        country.setRegion("North America");
        violations = validator.validate(country);
        assertTrue(violations.stream().noneMatch(v -> v.getPropertyPath().toString().equals("region")));
        
        // Test null region (should be valid)
        country.setRegion(null);
        violations = validator.validate(country);
        assertTrue(violations.stream().noneMatch(v -> v.getPropertyPath().toString().equals("region")));
    }

    @Test
    @DisplayName("Should validate currency code constraints")
    void shouldValidateCurrencyCodeConstraints() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        
        CountryEntity country = new CountryEntity("US", "United States", null, null);
        
        // Test currency code too short
        country.setCurrencyCode("US");
        Set<ConstraintViolation<CountryEntity>> violations = validator.validate(country);
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Currency code must be 3 characters")));
        
        // Test currency code too long
        country.setCurrencyCode("USDD");
        violations = validator.validate(country);
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Currency code must be 3 characters")));
        
        // Test valid currency code
        country.setCurrencyCode("USD");
        violations = validator.validate(country);
        assertTrue(violations.stream().noneMatch(v -> v.getPropertyPath().toString().equals("currencyCode")));
        
        // Test null currency code (should be valid)
        country.setCurrencyCode(null);
        violations = validator.validate(country);
        assertTrue(violations.stream().noneMatch(v -> v.getPropertyPath().toString().equals("currencyCode")));
    }

    @Test
    @DisplayName("Should handle all getters and setters")
    void shouldHandleAllGettersAndSetters() {
        CountryEntity country = new CountryEntity();
        
        country.setId(100L);
        country.setCountryCode("CA");
        country.setCountryName("Canada");
        country.setRegion("North America");
        country.setCurrencyCode("CAD");
        
        assertEquals(100L, country.getId());
        assertEquals("CA", country.getCountryCode());
        assertEquals("Canada", country.getCountryName());
        assertEquals("North America", country.getRegion());
        assertEquals("CAD", country.getCurrencyCode());
    }

    @Test
    @DisplayName("Should create country with minimal required fields")
    void shouldCreateCountryWithMinimalRequiredFields() {
        CountryEntity country = new CountryEntity("GB", "United Kingdom", null, null);
        
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<CountryEntity>> violations = validator.validate(country);
        
        assertTrue(violations.isEmpty());
        assertEquals("GB", country.getCountryCode());
        assertEquals("United Kingdom", country.getCountryName());
        assertNull(country.getRegion());
        assertNull(country.getCurrencyCode());
    }

    @Test
    @DisplayName("Should handle empty constructor")
    void shouldHandleEmptyConstructor() {
        CountryEntity country = new CountryEntity();
        
        assertNull(country.getId());
        assertNull(country.getCountryCode());
        assertNull(country.getCountryName());
        assertNull(country.getRegion());
        assertNull(country.getCurrencyCode());
    }
}
