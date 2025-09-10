package com.cs203.core.entity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ProductCategoriesEntity Tests")
class ProductCategoriesEntityTest {

    @Test
    @DisplayName("Should create valid ProductCategoriesEntity")
    void shouldCreateValidProductCategoriesEntity() {
        ProductCategoriesEntity category = new ProductCategoriesEntity(
            850110, 
            "Electric Motors", 
            "Electric motors of an output not exceeding 37.5 W", 
            5.5
        );

        assertEquals(850110, category.getCategoryCode());
        assertEquals("Electric Motors", category.getCategoryName());
        assertEquals("Electric motors of an output not exceeding 37.5 W", category.getDescription());
        assertEquals(5.5, category.getTariffBaseRate());
        assertTrue(category.getIsActive()); // Default value
    }

    @Test
    @DisplayName("Should validate required fields")
    void shouldValidateRequiredFields() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        
        ProductCategoriesEntity category = new ProductCategoriesEntity();
        
        Set<ConstraintViolation<ProductCategoriesEntity>> violations = validator.validate(category);
        assertFalse(violations.isEmpty());
        
        // Should have violations for required fields
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Category name is required")));
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("HS code is required")));
    }

    @Test
    @DisplayName("Should validate category name constraints")
    void shouldValidateCategoryNameConstraints() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        
        ProductCategoriesEntity category = new ProductCategoriesEntity();
        category.setCategoryCode(123456);
        
        // Test empty category name
        category.setCategoryName("");
        Set<ConstraintViolation<ProductCategoriesEntity>> violations = validator.validate(category);
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Category name is required")));
        
        // Test category name too long
        String longName = "A".repeat(101);
        category.setCategoryName(longName);
        violations = validator.validate(category);
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Category name cannot exceed 100 characters")));
        
        // Test valid category name
        category.setCategoryName("Valid Category");
        violations = validator.validate(category);
        assertTrue(violations.stream().noneMatch(v -> v.getPropertyPath().toString().equals("categoryName")));
    }

    @Test
    @DisplayName("Should validate category code constraints")
    void shouldValidateCategoryCodeConstraints() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        
        ProductCategoriesEntity category = new ProductCategoriesEntity();
        category.setCategoryName("Test Category");
        
        // Test HS code too small (less than 2 digits)
        category.setCategoryCode(5);
        Set<ConstraintViolation<ProductCategoriesEntity>> violations = validator.validate(category);
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("HS code must be at least 2 digits")));
        
        // Test HS code too large (more than 9 digits)
        category.setCategoryCode(1000000000); // 10 digits
        violations = validator.validate(category);
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("HS code cannot exceed 9 digits")));
        
        // Test valid HS code
        category.setCategoryCode(850110);
        violations = validator.validate(category);
        assertTrue(violations.stream().noneMatch(v -> v.getPropertyPath().toString().equals("categoryCode")));
    }

    @Test
    @DisplayName("Should validate description constraints")
    void shouldValidateDescriptionConstraints() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        
        ProductCategoriesEntity category = new ProductCategoriesEntity(123456, "Test Category", null, null);
        
        // Test description too long
        String longDescription = "A".repeat(501);
        category.setDescription(longDescription);
        Set<ConstraintViolation<ProductCategoriesEntity>> violations = validator.validate(category);
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Description cannot exceed 500 characters")));
        
        // Test valid description
        category.setDescription("Valid description");
        violations = validator.validate(category);
        assertTrue(violations.stream().noneMatch(v -> v.getPropertyPath().toString().equals("description")));
        
        // Test null description (should be valid)
        category.setDescription(null);
        violations = validator.validate(category);
        assertTrue(violations.stream().noneMatch(v -> v.getPropertyPath().toString().equals("description")));
    }

    @Test
    @DisplayName("Should validate tariff base rate constraints")
    void shouldValidateTariffBaseRateConstraints() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        
        ProductCategoriesEntity category = new ProductCategoriesEntity(123456, "Test Category", "Test Description", null);
        
        // Test negative tariff rate (if there are such constraints)
        category.setTariffBaseRate(-1.0);
        Set<ConstraintViolation<ProductCategoriesEntity>> violations = validator.validate(category);
        // Note: No explicit validation constraints for tariff rate in the entity, so this should pass
        
        // Test valid tariff rate
        category.setTariffBaseRate(10.5);
        violations = validator.validate(category);
        assertTrue(violations.stream().noneMatch(v -> v.getPropertyPath().toString().equals("tariffBaseRate")));
        
        // Test null tariff rate (should be valid)
        category.setTariffBaseRate(null);
        violations = validator.validate(category);
        assertTrue(violations.stream().noneMatch(v -> v.getPropertyPath().toString().equals("tariffBaseRate")));
    }

    @Test
    @DisplayName("Should set default values correctly")
    void shouldSetDefaultValuesCorrectly() {
        ProductCategoriesEntity category = new ProductCategoriesEntity();
        
        // Default isActive should be true
        assertTrue(category.getIsActive());
        
        // Default categoryCode should be null (Integer default)
        assertNull(category.getCategoryCode());
    }

    @Test
    @DisplayName("Should handle all getters and setters")
    void shouldHandleAllGettersAndSetters() {
        ProductCategoriesEntity category = new ProductCategoriesEntity();
        
        category.setId(100L);
        category.setCategoryCode(850120);
        category.setCategoryName("Electric Generators");
        category.setDescription("Electric generators and motors");
        category.setTariffBaseRate(7.25);
        category.setIsActive(false);
        
        assertEquals(100L, category.getId());
        assertEquals(850120, category.getCategoryCode());
        assertEquals("Electric Generators", category.getCategoryName());
        assertEquals("Electric generators and motors", category.getDescription());
        assertEquals(7.25, category.getTariffBaseRate());
        assertFalse(category.getIsActive());
    }

    @Test
    @DisplayName("Should create category with minimal required fields")
    void shouldCreateCategoryWithMinimalRequiredFields() {
        ProductCategoriesEntity category = new ProductCategoriesEntity(123456, "Minimal Category", null, null);
        
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<ProductCategoriesEntity>> violations = validator.validate(category);
        
        // Should be valid with just code and name
        assertTrue(violations.isEmpty());
        assertEquals(123456, category.getCategoryCode());
        assertEquals("Minimal Category", category.getCategoryName());
        assertNull(category.getDescription());
        assertNull(category.getTariffBaseRate());
        assertTrue(category.getIsActive()); // Default value
    }

    @Test
    @DisplayName("Should handle empty constructor")
    void shouldHandleEmptyConstructor() {
        ProductCategoriesEntity category = new ProductCategoriesEntity();
        
        assertNull(category.getId());
        assertNull(category.getCategoryCode()); // Integer default
        assertNull(category.getCategoryName());
        assertNull(category.getDescription());
        assertNull(category.getTariffBaseRate());
        assertTrue(category.getIsActive()); // Default value
    }

    @Test
    @DisplayName("Should handle complete category creation")
    void shouldHandleCompleteCategoryCreation() {
        ProductCategoriesEntity category = new ProductCategoriesEntity(
            850131, 
            "DC Motors", 
            "Direct current motors of an output not exceeding 750 W", 
            6.0
        );
        category.setIsActive(true);
        
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<ProductCategoriesEntity>> violations = validator.validate(category);
        
        assertTrue(violations.isEmpty());
        assertEquals(850131, category.getCategoryCode());
        assertEquals("DC Motors", category.getCategoryName());
        assertEquals("Direct current motors of an output not exceeding 750 W", category.getDescription());
        assertEquals(6.0, category.getTariffBaseRate());
        assertTrue(category.getIsActive());
    }

    @Test
    @DisplayName("Should handle inactive category")
    void shouldHandleInactiveCategory() {
        ProductCategoriesEntity category = new ProductCategoriesEntity(999999, "Inactive Category", "Discontinued product", 0.0);
        category.setIsActive(false);
        
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<ProductCategoriesEntity>> violations = validator.validate(category);
        
        assertTrue(violations.isEmpty());
        assertFalse(category.getIsActive());
        assertEquals(0.0, category.getTariffBaseRate());
    }
}
