package com.cs203.core.dto.requests;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

//request these from the user to pass to save calculation to make a new saved calculation
public record SaveCalculationRequestDTO(
    @NotBlank
    String calculationName,
    @NotNull
    @PositiveOrZero
    BigDecimal productValue,
    @NotBlank
    String currencyCode,
    @NotNull
    @PositiveOrZero
    BigDecimal tariffRate,
    @NotBlank
    String tariffType,
    @NotNull
    BigDecimal calculatedTariffCost,
    @NotNull 
    @PositiveOrZero
    BigDecimal totalCost,
    String notes,
    @NotBlank
    String importingCountryCode,
    @NotBlank
    String exportingCountryCode,
    @NotNull
    Integer productCategoryCode) {
}
