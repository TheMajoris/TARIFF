package com.cs203.core.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("CountryEntity Tests")
class CountryEntityTest {

    @Test
    @DisplayName("Should create valid CountryEntity")
    void shouldCreateValidCountryEntity() {
        CountryEntity country = new CountryEntity("US", "United States", "North America", "USD");
        assertEquals("US", country.getCountryCode());
        assertEquals("United States", country.getCountryName());
        assertEquals("North America", country.getRegion());
        assertEquals("USD", country.getCurrencyCode());
    }

        @Test
        @DisplayName("Should create CountryEntity with no-args constructor and set all fields")
        void testNoArgsConstructorAndSetters() {
            CountryEntity country = new CountryEntity();

            country.setId(1L);
            country.setCountryCode("SG");
            country.setCountryName("Singapore");
            country.setRegion("Asia");
            country.setCurrencyCode("SGD");

            assertEquals(1L, country.getId());
            assertEquals("SG", country.getCountryCode());
            assertEquals("Singapore", country.getCountryName());
            assertEquals("Asia", country.getRegion());
            assertEquals("SGD", country.getCurrencyCode());
        }

        @Test
        @DisplayName("Should create CountryEntity with all-args constructor and verify fields")
        void testAllArgsConstructor() {
            CountryEntity country = new CountryEntity("MY", "Malaysia", "Asia", "MYR");

            assertNull(country.getId()); // id is not set in all-args constructor
            assertEquals("MY", country.getCountryCode());
            assertEquals("Malaysia", country.getCountryName());
            assertEquals("Asia", country.getRegion());
            assertEquals("MYR", country.getCurrencyCode());
        }
}
