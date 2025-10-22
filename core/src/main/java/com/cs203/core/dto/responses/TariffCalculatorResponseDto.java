package com.cs203.core.dto.responses;

import jakarta.annotation.Nullable;

import java.math.BigDecimal;

//dont need tariffRate since client only wants to know price
public record TariffCalculatorResponseDto(
        BigDecimal tariffRate,
        String tariffType,  // "ad valorem" or "specific"
        @Nullable BigDecimal quantity,  // used for specific
        BigDecimal tariffCost,
        BigDecimal finalPrice) {
}
