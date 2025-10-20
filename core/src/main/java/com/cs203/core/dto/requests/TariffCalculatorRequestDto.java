package com.cs203.core.dto.requests;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

//DTO to obtain the params to be used as RequestBody instead of using @RequestParam 
public record TariffCalculatorRequestDto(
        @NotNull
        @Positive
        Long importingCountryId,
        @NotNull
        @Positive
        Long exportingCountryId,
        @NotNull
        @Positive
        Integer hsCode,
        @NotNull
        @PositiveOrZero
        BigDecimal initialPrice,
        @NotNull
        LocalDate date) {
}
