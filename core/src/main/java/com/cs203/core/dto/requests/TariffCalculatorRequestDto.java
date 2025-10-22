package com.cs203.core.dto.requests;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.time.LocalDate;

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
        @Nullable
        @PositiveOrZero
        BigDecimal quantity,  // only required for specific-rate tariffs
        @NotNull
        LocalDate date
) {
    public TariffCalculatorRequestDto {
        if (quantity == null) {
            quantity = BigDecimal.ZERO;
        }
    }
}
