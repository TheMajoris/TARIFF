package com.cs203.core.strategy;

import com.cs203.core.entity.TariffRateEntity;

import java.math.BigDecimal;

public interface TariffCalculationStrategy {
    BigDecimal calculateFinalPrice(BigDecimal initialPrice, BigDecimal quantity, TariffRateEntity tariff);
}