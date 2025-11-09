package com.cs203.core.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.cs203.core.enums.TariffType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("TariffRateEntity Tests")
class TariffRateEntityTest {

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
        @DisplayName("Should create TariffRateEntity with all-args constructor and verify fields")
        void testAllArgsConstructorAndGetters() {
            CountryEntity importing = new CountryEntity();
            importing.setId(1L);
            importing.setCountryCode("SG");

            CountryEntity exporting = new CountryEntity();
            exporting.setId(2L);
            exporting.setCountryCode("MY");

            ProductCategoriesEntity product = new ProductCategoriesEntity();
            product.setCategoryCode(123456);

            LocalDate effectiveDate = LocalDate.now().minusDays(1);
            LocalDate expiryDate = LocalDate.now().plusDays(1);

            TariffRateEntity entity = new TariffRateEntity(
                    importing, exporting, product,
                    new BigDecimal("5.0"),
                    "Percentage", "per unit",
                    effectiveDate, expiryDate,
                    true
            );

            // Verify fields via getters
            assertEquals(importing, entity.getImportingCountry());
            assertEquals(exporting, entity.getExportingCountry());
            assertEquals(product, entity.getProductCategory());
            assertEquals(new BigDecimal("5.0"), entity.getTariffRate());
            assertEquals("Percentage", entity.getTariffType());
            assertEquals("per unit", entity.getRateUnit());
            assertEquals(effectiveDate, entity.getEffectiveDate());
            assertEquals(expiryDate, entity.getExpiryDate());
            assertTrue(entity.getPreferentialTariff());

            // Convenience getters
            assertEquals(1L, entity.getImportingCountryId());
            assertEquals(2L, entity.getExportingCountryId());
            assertEquals(123456, entity.getHsCode());

            // Helper methods
            assertTrue(entity.isCurrentlyActive());
            assertFalse(entity.isExpired());
            assertTrue(entity.isEffective());

            // Enum getter/setter
            entity.setTariffTypeEnum(TariffType.SPECIFIC);
            assertEquals(TariffType.SPECIFIC, entity.getTariffTypeEnum());
        }

        @Test
        @DisplayName("Should allow setting all fields via setters")
        void testSetters() {
            TariffRateEntity entity = new TariffRateEntity();

            CountryEntity importing = new CountryEntity();
            importing.setId(10L);
            entity.setImportingCountry(importing);

            CountryEntity exporting = new CountryEntity();
            exporting.setId(20L);
            entity.setExportingCountry(exporting);

            ProductCategoriesEntity product = new ProductCategoriesEntity();
            product.setCategoryCode(654321);
            entity.setProductCategory(product);

            entity.setTariffRate(new BigDecimal("10.0"));
            entity.setTariffType("Fixed");
            entity.setRateUnit("per kg");

            LocalDate effectiveDate = LocalDate.now().minusDays(2);
            LocalDate expiryDate = LocalDate.now().plusDays(2);

            entity.setEffectiveDate(effectiveDate);
            entity.setExpiryDate(expiryDate);
            entity.setPreferentialTariff(false);

            // Verify via getters
            assertEquals(importing, entity.getImportingCountry());
            assertEquals(exporting, entity.getExportingCountry());
            assertEquals(product, entity.getProductCategory());
            assertEquals(new BigDecimal("10.0"), entity.getTariffRate());
            assertEquals("Fixed", entity.getTariffType());
            assertEquals("per kg", entity.getRateUnit());
            assertEquals(effectiveDate, entity.getEffectiveDate());
            assertEquals(expiryDate, entity.getExpiryDate());
            assertFalse(entity.getPreferentialTariff());

            assertEquals(10L, entity.getImportingCountryId());
            assertEquals(20L, entity.getExportingCountryId());
            assertEquals(654321, entity.getHsCode());
        }

    @Test
    @DisplayName("Should correctly evaluate isCurrentlyActive, isExpired, isEffective")
    void testDateHelperMethods() {
        CountryEntity importing = new CountryEntity();
        importing.setId(1L);
        CountryEntity exporting = new CountryEntity();
        exporting.setId(2L);
        ProductCategoriesEntity product = new ProductCategoriesEntity();
        product.setCategoryCode(123456);

        LocalDate past = LocalDate.now().minusDays(5);
        LocalDate future = LocalDate.now().plusDays(5);

        // Active case
        TariffRateEntity active = new TariffRateEntity(importing, exporting, product,
                new BigDecimal("5"), "Percentage", "kg", past, future, true);
        assertTrue(active.isCurrentlyActive());
        assertFalse(active.isExpired());
        assertTrue(active.isEffective());

        // Expired case
        TariffRateEntity expired = new TariffRateEntity(importing, exporting, product,
                new BigDecimal("5"), "Percentage", "kg", past, past, true);
        assertFalse(expired.isCurrentlyActive());
        assertTrue(expired.isExpired());
        assertTrue(expired.isEffective());

        // Not yet effective
        TariffRateEntity notEffective = new TariffRateEntity(importing, exporting, product,
                new BigDecimal("5"), "Percentage", "kg", future, future, true);
        assertFalse(notEffective.isCurrentlyActive());
        assertFalse(notEffective.isExpired());
        assertFalse(notEffective.isEffective());

        // Both dates null
        TariffRateEntity nullDates = new TariffRateEntity(importing, exporting, product,
                new BigDecimal("5"), "Percentage", "kg", null, null, true);
        assertTrue(nullDates.isCurrentlyActive());
        assertFalse(nullDates.isExpired());
        assertFalse(nullDates.isEffective());
    }

    @Test
    @DisplayName("Should handle null TariffTypeEnum")
    void testNullTariffTypeEnum() {
        TariffRateEntity entity = new TariffRateEntity();
        entity.setTariffTypeEnum(null);
        assertNull(entity.getTariffTypeEnum());
    }

    @Test
    @DisplayName("Convenience getters should handle null references")
    void testConvenienceGettersNull() {
        TariffRateEntity entity = new TariffRateEntity();

        // All foreign keys null
        entity.setImportingCountry(null);
        entity.setExportingCountry(null);
        entity.setProductCategory(null);

        assertNull(entity.getImportingCountryId());
        assertNull(entity.getExportingCountryId());
        assertNull(entity.getHsCode());
    }


}
