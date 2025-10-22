package com.cs203.core.strategy.impl;

import com.cs203.core.entity.TariffRateEntity;
import com.cs203.core.strategy.TariffCalculationStrategy;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component("specificRateStrategy")
public class SpecificTariffCalculationStrategyImpl implements TariffCalculationStrategy {
    @Override
    public BigDecimal calculateFinalPrice(BigDecimal initialPrice, BigDecimal quantity, TariffRateEntity tariff) {
        if (quantity == null) {
            throw new IllegalArgumentException("Quantity is required for specific tariff calculation");
        }
        if (tariff.getUnitQuantity() == null || tariff.getUnitQuantity().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Unit quantity must be set and positive for specific tariff");
        }

        // number of units the tariff applies to
        BigDecimal units = quantity.divide(tariff.getUnitQuantity(), 4, RoundingMode.CEILING);

        // total tariff = rate per unit * number of units
        BigDecimal totalTariff = tariff.getTariffRate().multiply(units);

        return initialPrice.add(totalTariff);
    }
}
