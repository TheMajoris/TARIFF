package com.cs203.core.strategy.impl;


import com.cs203.core.entity.TariffRateEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class AdValoremTariffCalculationStrategyImplTest {

    private final AdValoremTariffCalculationStrategyImpl strategy = new AdValoremTariffCalculationStrategyImpl();

    @Test
    @DisplayName("calculateFinalPrice should apply ad valorem rate correctly")
    void calculateFinalPrice_appliesRateCorrectly() {
        // given
        TariffRateEntity tariff = new TariffRateEntity();
        tariff.setTariffRate(new BigDecimal("20.00")); // 20%

        BigDecimal initialPrice = new BigDecimal("100.00");
        BigDecimal quantity = BigDecimal.ONE; // not used in this strategy

        // when
        BigDecimal finalPrice = strategy.calculateFinalPrice(initialPrice, quantity, tariff);

        // then
        assertEquals(0, finalPrice.compareTo(new BigDecimal("120.00")));
    }

    @Test
    @DisplayName("calculateFinalPrice with zero rate returns same price")
    void calculateFinalPrice_zeroRate_returnsSamePrice() {
        TariffRateEntity tariff = new TariffRateEntity();
        tariff.setTariffRate(BigDecimal.ZERO);

        BigDecimal result = strategy.calculateFinalPrice(new BigDecimal("100.00"), BigDecimal.ONE, tariff);

        assertEquals(0, result.compareTo(new BigDecimal("100.00")));
    }

    @Test
    @DisplayName("calculateFinalPrice handles fractional rates correctly")
    void calculateFinalPrice_fractionalRate() {
        TariffRateEntity tariff = new TariffRateEntity();
        tariff.setTariffRate(new BigDecimal("7.5")); // 7.5%

        BigDecimal result = strategy.calculateFinalPrice(new BigDecimal("200.00"), BigDecimal.ONE, tariff);

        assertEquals(0, result.compareTo(new BigDecimal("215.00")));
    }
}