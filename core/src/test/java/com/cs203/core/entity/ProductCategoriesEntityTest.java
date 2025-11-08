package com.cs203.core.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

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

    @Test
    void testConstructorWithIdAndAllFields() {
        Long id = 1L;
        Integer code = 123456;
        String name = "Electronics";
        String desc = "Category for electronic items";
        Boolean isActive = false;

        ProductCategoriesEntity entity = new ProductCategoriesEntity(id, code, name, desc, isActive);

        assertEquals(code, entity.getCategoryCode());
        assertEquals(name, entity.getCategoryName());
        assertEquals(desc, entity.getDescription());
        assertEquals(isActive, entity.getIsActive());
    }

    @Test
    void testConstructorWithoutId() {
        Integer code = 654321;
        String name = "Clothing";
        String desc = "Apparel and garments";
        Boolean isActive = true;

        ProductCategoriesEntity entity = new ProductCategoriesEntity(code, name, desc, isActive);

        assertNull(entity.getId());
        assertEquals(code, entity.getCategoryCode());
        assertEquals(name, entity.getCategoryName());
        assertEquals(desc, entity.getDescription());
        assertEquals(isActive, entity.getIsActive());
    }

    @Test
    void testGettersAndSettersForTariffRates() {
        ProductCategoriesEntity entity = new ProductCategoriesEntity();
        List<TariffRateEntity> rates = new ArrayList<>();
        rates.add(new TariffRateEntity());
        entity.setTariffRates(rates);

        assertEquals(rates, entity.getTariffRates());
    }

    @Test
    void testGettersAndSettersForNationalTariffLines() {
        ProductCategoriesEntity entity = new ProductCategoriesEntity();
        List<NationalTariffLinesEntity> lines = new ArrayList<>();
        lines.add(new NationalTariffLinesEntity());
        entity.setNationalTariffLines(lines);

        assertEquals(lines, entity.getNationalTariffLines());
    }

    @Test
    @DisplayName("Should test remaining getters and setters for ProductCategoriesEntity")
    void testRemainingGettersAndSetters() {
        ProductCategoriesEntity category = new ProductCategoriesEntity();

        // setCategoryName
        category.setCategoryName("Electronics");
        assertEquals("Electronics", category.getCategoryName());

        // setDescription and getDescription
        category.setDescription("Consumer electronics and gadgets");
        assertEquals("Consumer electronics and gadgets", category.getDescription());
    }


}
