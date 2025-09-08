package com.cs203.core.TariffCalculator;

import org.junit.jupiter.api.*;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class TariffCalculatorTests {

    @Test
    @DisplayName("Should initialize fields correctly")
    void constructor_initializesFields() {
        BigDecimal initialTariffRate = new BigDecimal("0.15");
        BigDecimal initialFinalPrice = new BigDecimal("115.00");

        TariffCalculator calculator = new TariffCalculator(initialTariffRate, initialFinalPrice);

        assertTrue(0 == calculator.getTariffRate().compareTo(initialTariffRate));
        assertTrue(0 == calculator.getFinalPrice().compareTo(initialFinalPrice));
    }

    @Test
    @DisplayName("Should update values correctly")
    void setters_updateValues() {
        TariffCalculator calculator = new TariffCalculator(null, null);

        BigDecimal tariffRate = new BigDecimal("0.10");
        BigDecimal finalPrice = new BigDecimal("110.00");

        calculator.setTariffRate(tariffRate);
        calculator.setFinalPrice(finalPrice);

        assertTrue(0 == calculator.getTariffRate().compareTo(tariffRate));
        assertTrue(0 == calculator.getFinalPrice().compareTo(finalPrice));
    }

    @Test
    @DisplayName("Should handle nulls correctly")
    void getters_handleNullsWhenInitializedWithNulls() {
        TariffCalculator calculator = new TariffCalculator(null, null);

        assertNull(calculator.getTariffRate());
        assertNull(calculator.getFinalPrice());
    }
}
