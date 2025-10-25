package com.cs203.core.dto.responses;

import jakarta.annotation.Nullable;

import java.math.BigDecimal;

//dont need tariffRate since client only wants to know price
public record TariffCalculatorResponseDto(
        BigDecimal tariffRate,
        String tariffType,  // "ad_valorem" or "specific"
        String rateUnit, // used for specific calculation
        @Nullable BigDecimal quantity,  // used for specific calculation
        BigDecimal tariffCost,
        BigDecimal finalPrice) {
}
