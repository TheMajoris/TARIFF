package com.cs203.core.entity;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class SavedCalculationsEntityTest {
    @Test
    void testNoArgsConstructor() {
        SavedCalculationsEntity entity = new SavedCalculationsEntity();
        assertNotNull(entity);
    }

    @Test
    void testAllArgsConstructor() {
        String calcName = "Tariff Calculation";
        UserEntity user = new UserEntity();
        CountryEntity importing = new CountryEntity();
        CountryEntity exporting = new CountryEntity();
        ProductCategoriesEntity product = new ProductCategoriesEntity();
        BigDecimal productValue = new BigDecimal("100.00");
        BigDecimal productQuantity = new BigDecimal("100.00");
        String currency = "USD";
        BigDecimal tariffRate = new BigDecimal("5.00");
        BigDecimal unitQuantity = new BigDecimal("100.0");
        String rateUnit = "Percentage";
        String tariffType = "ad_valorem";
        BigDecimal tariffCost = new BigDecimal("5.00");
        BigDecimal totalCost = new BigDecimal("105.00");
        String notes = "Sample calculation";

        SavedCalculationsEntity entity = new SavedCalculationsEntity(
                calcName, user, importing, exporting, product,
                productValue, productQuantity, currency, tariffRate, unitQuantity, rateUnit, tariffType,
                tariffCost, totalCost, notes
        );

        assertEquals(calcName, entity.getCalculationName());
        assertEquals(user, entity.getUser());
        assertEquals(importing, entity.getImportingCountry());
        assertEquals(exporting, entity.getExportingCountry());
        assertEquals(product, entity.getProductCategory());
        assertEquals(productValue, entity.getProductValue());
        assertEquals(currency, entity.getCurrencyCode());
        assertEquals(tariffRate, entity.getTariffRate());
        assertEquals(tariffType, entity.getTariffType());
        assertEquals(tariffCost, entity.getCalculatedTariffCost());
        assertEquals(totalCost, entity.getTotalCost());
        assertEquals(notes, entity.getNotes());
    }

    @Test
    void testToStringContainsKeyFields() {
        SavedCalculationsEntity entity = new SavedCalculationsEntity();
        entity.setId(1L);
        entity.setCalculationName("TestCalc");
        entity.setProductValue(new BigDecimal("200"));
        entity.setCurrencyCode("EUR");
        entity.setTariffRate(new BigDecimal("10"));
        entity.setTariffType("Fixed");
        entity.setCalculatedTariffCost(new BigDecimal("20"));
        entity.setTotalCost(new BigDecimal("220"));
        entity.setNotes("Testing toString");

        String result = entity.toString();

        assertTrue(result.contains("SavedCalculationsEntity"));
        assertTrue(result.contains("TestCalc"));
        assertTrue(result.contains("200"));
        assertTrue(result.contains("EUR"));
        assertTrue(result.contains("Fixed"));
        assertTrue(result.contains("220"));
    }

    @Test
    void testGettersAndSetters() {
        SavedCalculationsEntity entity = new SavedCalculationsEntity();

        // Setup related entities
        UserEntity user = new UserEntity();
        CountryEntity importing = new CountryEntity();
        CountryEntity exporting = new CountryEntity();
        ProductCategoriesEntity productCategory = new ProductCategoriesEntity();

        LocalDateTime createdAt = LocalDateTime.now().minusDays(1);
        LocalDateTime updatedAt = LocalDateTime.now();

        // Set fields
        entity.setUser(user);
        entity.setImportingCountry(importing);
        entity.setExportingCountry(exporting);
        entity.setProductCategory(productCategory);
        entity.setCreatedAt(createdAt);
        entity.setUpdatedAt(updatedAt);
        entity.setId(99L);

        // Verify getters
        assertEquals(user, entity.getUser());
        assertEquals(importing, entity.getImportingCountry());
        assertEquals(exporting, entity.getExportingCountry());
        assertEquals(productCategory, entity.getProductCategory());
        assertEquals(createdAt, entity.getCreatedAt());
        assertEquals(updatedAt, entity.getUpdatedAt());
        assertEquals(99L, entity.getId());
    }


}
