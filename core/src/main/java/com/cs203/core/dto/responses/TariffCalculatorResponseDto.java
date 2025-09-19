package com.cs203.core.dto.responses;

import java.math.BigDecimal;

//dont need tariffRate since client only wants to know price
public record TariffCalculatorResponseDto(
        BigDecimal finalPrice) {
}
