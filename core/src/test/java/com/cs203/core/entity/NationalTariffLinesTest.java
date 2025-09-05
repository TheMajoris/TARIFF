package com.cs203.core.entity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("NationalTariffLines Tests")
class NationalTariffLinesTest {

    private CountryEntity testCountry;
    private ProductCategoriesEntity testCategory;
    private UserEntity testUser;

    @BeforeEach
    void setUp() {
        testCountry = new CountryEntity("US", "United States", "North America", "USD");
        testCountry.setId(1L);
        
        testCategory = new ProductCategoriesEntity(850110, "Electric Motors", "Electric motors description", 5.5);
        testCategory.setId(1L);
        
        testUser = new UserEntity("testuser", "test@example.com", "hashedpassword");
        testUser.setId(1L);
    }

    @Test
    @DisplayName("Should create valid NationalTariffLines")
    void shouldCreateValidNationalTariffLines() {
        NationalTariffLines tariffLine = new NationalTariffLines(
            testCountry,
            "8501.10.10",
            "DC motors not exceeding 37.5 W",
            testCategory,
            8
        );

        assertEquals(testCountry, tariffLine.getCountry());
        assertEquals("8501.10.10", tariffLine.getTariffLineCode());
        assertEquals("DC motors not exceeding 37.5 W", tariffLine.getDescription());
        assertEquals(testCategory, tariffLine.getParentHsCode());
        assertEquals(8, tariffLine.getLevel());
    }

    @Test
    @DisplayName("Should validate required fields")
    void shouldValidateRequiredFields() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        
        NationalTariffLines tariffLine = new NationalTariffLines();
        
        Set<ConstraintViolation<NationalTariffLines>> violations = validator.validate(tariffLine);
        assertFalse(violations.isEmpty());
        
        // Should have violations for required fields
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Country is required")));
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Tariff line code is required")));
    }

    @Test
    @DisplayName("Should validate tariff line code constraints")
    void shouldValidateTariffLineCodeConstraints() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        
        NationalTariffLines tariffLine = new NationalTariffLines();
        tariffLine.setCountry(testCountry);
        
        // Test empty tariff line code
        tariffLine.setTariffLineCode("");
        Set<ConstraintViolation<NationalTariffLines>> violations = validator.validate(tariffLine);
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Tariff line code is required")));
        
        // Test tariff line code too long
        String longCode = "A".repeat(21);
        tariffLine.setTariffLineCode(longCode);
        violations = validator.validate(tariffLine);
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Tariff line code cannot exceed 20 characters")));
        
        // Test valid tariff line code
        tariffLine.setTariffLineCode("8501.10.10");
        violations = validator.validate(tariffLine);
        assertTrue(violations.stream().noneMatch(v -> v.getPropertyPath().toString().equals("tariffLineCode")));
    }

    @Test
    @DisplayName("Should validate description constraints")
    void shouldValidateDescriptionConstraints() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        
        NationalTariffLines tariffLine = new NationalTariffLines(testCountry, "8501.10.10", null, null, null);
        
        // Test description too long
        String longDescription = "A".repeat(501);
        tariffLine.setDescription(longDescription);
        Set<ConstraintViolation<NationalTariffLines>> violations = validator.validate(tariffLine);
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Description cannot exceed 500 characters")));
        
        // Test valid description
        tariffLine.setDescription("Valid description");
        violations = validator.validate(tariffLine);
        assertTrue(violations.stream().noneMatch(v -> v.getPropertyPath().toString().equals("description")));
        
        // Test null description (should be valid)
        tariffLine.setDescription(null);
        violations = validator.validate(tariffLine);
        assertTrue(violations.stream().noneMatch(v -> v.getPropertyPath().toString().equals("description")));
    }

    @Test
    @DisplayName("Should validate level constraints")
    void shouldValidateLevelConstraints() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        
        NationalTariffLines tariffLine = new NationalTariffLines(testCountry, "8501.10.10", "Test description", null, null);
        
        // Test level too low
        tariffLine.setLevel(0);
        Set<ConstraintViolation<NationalTariffLines>> violations = validator.validate(tariffLine);
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Level must be at least 1")));
        
        // Test level too high
        tariffLine.setLevel(11);
        violations = validator.validate(tariffLine);
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Level cannot exceed 10")));
        
        // Test valid level
        tariffLine.setLevel(5);
        violations = validator.validate(tariffLine);
        assertTrue(violations.stream().noneMatch(v -> v.getPropertyPath().toString().equals("level")));
        
        // Test null level (should be valid)
        tariffLine.setLevel(null);
        violations = validator.validate(tariffLine);
        assertTrue(violations.stream().noneMatch(v -> v.getPropertyPath().toString().equals("level")));
    }

    @Test
    @DisplayName("Should handle all getters and setters")
    void shouldHandleAllGettersAndSetters() {
        NationalTariffLines tariffLine = new NationalTariffLines();
        
        tariffLine.setId(100L);
        tariffLine.setCountry(testCountry);
        tariffLine.setTariffLineCode("8501.20.30");
        tariffLine.setDescription("AC motors over 37.5 W but not exceeding 750 W");
        tariffLine.setParentHsCode(testCategory);
        tariffLine.setLevel(6);
        tariffLine.setCreatedBy(testUser);
        tariffLine.setUpdatedBy(testUser);
        
        assertEquals(100L, tariffLine.getId());
        assertEquals(testCountry, tariffLine.getCountry());
        assertEquals("8501.20.30", tariffLine.getTariffLineCode());
        assertEquals("AC motors over 37.5 W but not exceeding 750 W", tariffLine.getDescription());
        assertEquals(testCategory, tariffLine.getParentHsCode());
        assertEquals(6, tariffLine.getLevel());
        assertEquals(testUser, tariffLine.getCreatedBy());
        assertEquals(testUser, tariffLine.getUpdatedBy());
    }

    @Test
    @DisplayName("Should create tariff line with minimal required fields")
    void shouldCreateTariffLineWithMinimalRequiredFields() {
        NationalTariffLines tariffLine = new NationalTariffLines(testCountry, "8501.10.90", null, null, null);
        
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<NationalTariffLines>> violations = validator.validate(tariffLine);
        
        assertTrue(violations.isEmpty());
        assertEquals(testCountry, tariffLine.getCountry());
        assertEquals("8501.10.90", tariffLine.getTariffLineCode());
        assertNull(tariffLine.getDescription());
        assertNull(tariffLine.getParentHsCode());
        assertNull(tariffLine.getLevel());
    }

    @Test
    @DisplayName("Should handle empty constructor")
    void shouldHandleEmptyConstructor() {
        NationalTariffLines tariffLine = new NationalTariffLines();
        
        assertNull(tariffLine.getId());
        assertNull(tariffLine.getCountry());
        assertNull(tariffLine.getTariffLineCode());
        assertNull(tariffLine.getDescription());
        assertNull(tariffLine.getParentHsCode());
        assertNull(tariffLine.getLevel());
        assertNull(tariffLine.getCreatedBy());
        assertNull(tariffLine.getUpdatedBy());
    }

    @Test
    @DisplayName("Should handle complete tariff line creation")
    void shouldHandleCompleteTariffLineCreation() {
        NationalTariffLines tariffLine = new NationalTariffLines(
            testCountry,
            "8501.31.10",
            "DC motors over 750 W but not exceeding 75 kW",
            testCategory,
            8
        );
        tariffLine.setCreatedBy(testUser);
        tariffLine.setUpdatedBy(testUser);
        
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<NationalTariffLines>> violations = validator.validate(tariffLine);
        
        assertTrue(violations.isEmpty());
        assertEquals(testCountry, tariffLine.getCountry());
        assertEquals("8501.31.10", tariffLine.getTariffLineCode());
        assertEquals("DC motors over 750 W but not exceeding 75 kW", tariffLine.getDescription());
        assertEquals(testCategory, tariffLine.getParentHsCode());
        assertEquals(8, tariffLine.getLevel());
        assertEquals(testUser, tariffLine.getCreatedBy());
        assertEquals(testUser, tariffLine.getUpdatedBy());
    }

    @Test
    @DisplayName("Should handle relationships correctly")
    void shouldHandleRelationshipsCorrectly() {
        // Create different entities for relationships
        CountryEntity canadaCountry = new CountryEntity("CA", "Canada", "North America", "CAD");
        canadaCountry.setId(2L);
        
        ProductCategoriesEntity otherCategory = new ProductCategoriesEntity(850120, "Electric Generators", "Generator description", 7.0);
        otherCategory.setId(2L);
        
        UserEntity otherUser = new UserEntity("adminuser", "admin@example.com", "adminpassword");
        otherUser.setId(2L);
        
        NationalTariffLines tariffLine = new NationalTariffLines(
            canadaCountry,
            "8501.20.10",
            "Canadian specific tariff line",
            otherCategory,
            7
        );
        tariffLine.setCreatedBy(testUser);
        tariffLine.setUpdatedBy(otherUser);
        
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<NationalTariffLines>> violations = validator.validate(tariffLine);
        
        assertTrue(violations.isEmpty());
        assertEquals(canadaCountry, tariffLine.getCountry());
        assertEquals(otherCategory, tariffLine.getParentHsCode());
        assertEquals(testUser, tariffLine.getCreatedBy());
        assertEquals(otherUser, tariffLine.getUpdatedBy());
    }

    @Test
    @DisplayName("Should validate boundary level values")
    void shouldValidateBoundaryLevelValues() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        
        NationalTariffLines tariffLine = new NationalTariffLines(testCountry, "8501.10.10", "Test", null, null);
        
        // Test minimum valid level
        tariffLine.setLevel(1);
        Set<ConstraintViolation<NationalTariffLines>> violations = validator.validate(tariffLine);
        assertTrue(violations.stream().noneMatch(v -> v.getPropertyPath().toString().equals("level")));
        
        // Test maximum valid level
        tariffLine.setLevel(10);
        violations = validator.validate(tariffLine);
        assertTrue(violations.stream().noneMatch(v -> v.getPropertyPath().toString().equals("level")));
    }
}
