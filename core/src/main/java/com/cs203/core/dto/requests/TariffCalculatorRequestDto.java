package com.cs203.core.dto.requests;

import java.math.BigDecimal;
import java.time.LocalDate;

//DTO to obtain te params to be used as RequstBody instead of using @RequestParam 
public record TariffCalculatorRequestDto(
        long importingCountryId,
        long exportingCountryId,
        int hsCode,
        BigDecimal initialPrice,
        LocalDate date) {

}
