package com.cs203.core.TariffCalculator;

import java.math.BigDecimal;

//DTO to obtain te params to be used as RequstBody instead of using @RequestParam 
public record TariffCalculatorRequestDTO(
        long importingCountryId,
        long exportingCountryId,
        int hsCode,
        BigDecimal initialPrice) {

}
