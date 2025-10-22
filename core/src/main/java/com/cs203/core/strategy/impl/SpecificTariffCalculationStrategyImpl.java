package com.cs203.core.strategy.impl;

import com.cs203.core.entity.TariffRateEntity;
import com.cs203.core.strategy.TariffCalculationStrategy;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component("specificRateStrategy")
public class SpecificTariffCalculationStrategyImpl implements TariffCalculationStrategy {
    @Override
    public BigDecimal calculateFinalPrice(BigDecimal initialPrice, BigDecimal quantity, TariffRateEntity tariff) {
        if (quantity == null) {
            throw new IllegalArgumentException("Quantity is required for specific tariff calculation");
        }
        BigDecimal totalTariff = tariff.getTariffRate().multiply(quantity);
        return initialPrice.add(totalTariff);
    }
}
