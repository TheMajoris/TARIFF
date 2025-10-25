package com.cs203.core.strategy.impl;

import com.cs203.core.entity.TariffRateEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SpecificTariffCalculationStrategyImplTest {

    private final SpecificTariffCalculationStrategyImpl strategy = new SpecificTariffCalculationStrategyImpl();

    @Test
    @DisplayName("calculateFinalPrice adds specific tariff correctly for whole units")
    void calculateFinalPrice_wholeUnits() {
        TariffRateEntity tariff = new TariffRateEntity();
        tariff.setTariffRate(new BigDecimal("5.00"));        // $5 per unit
        tariff.setUnitQuantity(BigDecimal.ONE);               // per 1 unit

        BigDecimal initialPrice = new BigDecimal("100.00");
        BigDecimal quantity = new BigDecimal("3");            // 3 units

        BigDecimal result = strategy.calculateFinalPrice(initialPrice, quantity, tariff);

        // total tariff = 5 * 3 = 15 → final = 100 + 15 = 115
        assertEquals(0, result.compareTo(new BigDecimal("115.00")));
    }

    @Test
    @DisplayName("calculateFinalPrice handles fractional quantities correctly (with CEILING rounding)")
    void calculateFinalPrice_fractionalQuantity() {
        TariffRateEntity tariff = new TariffRateEntity();
        tariff.setTariffRate(new BigDecimal("2.50"));          // $2.50 per 0.5 unit
        tariff.setUnitQuantity(new BigDecimal("0.5"));

        BigDecimal initialPrice = new BigDecimal("50.00");
        BigDecimal quantity = new BigDecimal("1.2");           // 1.2 / 0.5 = 2.4 → rounds up to 2.4 units

        BigDecimal result = strategy.calculateFinalPrice(initialPrice, quantity, tariff);

        // units = 1.2 / 0.5 = 2.4, total tariff = 2.4 * 2.5 = 6.00 → final = 56.00
        assertEquals(0, result.compareTo(new BigDecimal("56.00")));
    }

    @Test
    @DisplayName("throws when quantity is null")
    void calculateFinalPrice_nullQuantity_throws() {
        TariffRateEntity tariff = new TariffRateEntity();
        tariff.setTariffRate(new BigDecimal("5.00"));
        tariff.setUnitQuantity(BigDecimal.ONE);

        assertThrows(IllegalArgumentException.class,
                () -> strategy.calculateFinalPrice(new BigDecimal("100.00"), null, tariff));
    }

    @Test
    @DisplayName("throws when unitQuantity is null")
    void calculateFinalPrice_nullUnitQuantity_throws() {
        TariffRateEntity tariff = new TariffRateEntity();
        tariff.setTariffRate(new BigDecimal("5.00"));
        tariff.setUnitQuantity(null);

        assertThrows(IllegalArgumentException.class,
                () -> strategy.calculateFinalPrice(new BigDecimal("100.00"), BigDecimal.ONE, tariff));
    }

    @Test
    @DisplayName("throws when unitQuantity is zero or negative")
    void calculateFinalPrice_invalidUnitQuantity_throws() {
        TariffRateEntity tariff = new TariffRateEntity();
        tariff.setTariffRate(new BigDecimal("5.00"));
        tariff.setUnitQuantity(BigDecimal.ZERO);

        assertThrows(IllegalArgumentException.class,
                () -> strategy.calculateFinalPrice(new BigDecimal("100.00"), BigDecimal.ONE, tariff));
    }

    @Test
    @DisplayName("calculateFinalPrice rounds up correctly for non-divisible quantities")
    void calculateFinalPrice_roundsUpWithCeiling() {
        TariffRateEntity tariff = new TariffRateEntity();
        tariff.setTariffRate(new BigDecimal("10.00"));
        tariff.setUnitQuantity(new BigDecimal("4")); // per 4 units

        BigDecimal initialPrice = new BigDecimal("100.00");
        BigDecimal quantity = new BigDecimal("5"); // 5/4 = 1.25 → ceil to 1.25 → totalTariff = 12.5

        BigDecimal result = strategy.calculateFinalPrice(initialPrice, quantity, tariff);

        assertEquals(0, result.compareTo(new BigDecimal("112.50")));
    }
}
