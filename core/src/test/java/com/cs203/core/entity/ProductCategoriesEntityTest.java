package com.cs203.core.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("ProductCategoriesEntity Tests")
class ProductCategoriesEntityTest {

    @Test
    @DisplayName("Should create valid ProductCategoriesEntity")
    void shouldCreateValidProductCategoriesEntity() {
        ProductCategoriesEntity category = new ProductCategoriesEntity(
                850110,
                "Electric Motors",
                "Electric motors of an output not exceeding 37.5 W");

        assertEquals(850110, category.getCategoryCode());
        assertEquals("Electric Motors", category.getCategoryName());
        assertEquals("Electric motors of an output not exceeding 37.5 W", category.getDescription());
        assertTrue(category.getIsActive()); // Default value
    }

    @Test
    @DisplayName("Should allow setting active status")
    void shouldAllowSettingActiveStatus() {
        ProductCategoriesEntity category = new ProductCategoriesEntity(
                850120,
                "Electric Generators",
                "AC generators");
        category.setIsActive(false);
        assertEquals(false, category.getIsActive());
    }
}
