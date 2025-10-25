package com.cs203.core.strategy.impl;

import com.cs203.core.entity.TariffRateEntity;
import com.cs203.core.strategy.TariffCalculationStrategy;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component("adValoremStrategy")
public class AdValoremTariffCalculationStrategyImpl implements TariffCalculationStrategy {
    @Override
    public BigDecimal calculateFinalPrice(BigDecimal initialPrice, BigDecimal quantity, TariffRateEntity tariff) {
        BigDecimal rateDecimal = tariff.getTariffRate().divide(BigDecimal.valueOf(100));
        return initialPrice.add(initialPrice.multiply(rateDecimal));
    }
}
