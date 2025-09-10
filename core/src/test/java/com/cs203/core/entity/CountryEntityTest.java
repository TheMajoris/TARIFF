package com.cs203.core.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
}
