package com.cs203.core.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class TariffRateDto {

    private Long id;

    @NotNull(message = "Tariff rate is required")
    @DecimalMin(value = "0.0", message = "Tariff rate must be non-negative")
    @DecimalMax(value = "999.9999", message = "Tariff rate cannot exceed 999.9999")
    private BigDecimal tariffRate;

    @NotBlank(message = "Tariff type is required")
    @Size(max = 50, message = "Tariff type cannot exceed 50 characters")
    private String tariffType;

    @DecimalMin(value = "0.0", message = "Unit quantity must be positive")
    private BigDecimal unitQuantity;

    @Size(max = 20, message = "Rate unit cannot exceed 20 characters")
    private String rateUnit;

    @NotNull(message = "Effective date is required")
    private LocalDate effectiveDate;

    private LocalDate expiryDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Boolean preferentialTariff;

    @NotNull(message = "Importing country is required")
    private String importingCountryCode;

    @NotNull(message = "Exporting country is required")
    private String exportingCountryCode;

    @NotNull(message = "HS code is required")
    private ProductCategoriesDto productCategory;
}