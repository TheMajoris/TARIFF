package com.cs203.core.TariffCalculator;

import java.math.BigDecimal;

//dont need tariffRate since client only wants to know price
public record TariffCalculatorResponseDTO(
        BigDecimal finalPrice) {
}
