package com.cs203.core.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SavedCalculationDto {
    private Long id;

    @NotBlank(message = "Calculation name is required")
    @Size(max = 100, message = "Calculation name cannot exceed 100 characters")
    private String calculationName;

    @NotNull(message = "Product value is required")
    @DecimalMin(value = "0.01", message = "Product value must be greater than 0")
    private BigDecimal productValue;

    @NotNull(message = "Product quantity is required")
    @DecimalMin(value = "1", message = "Product quantity must be greater than 0")
    private BigDecimal productQuantity;

    @NotBlank(message = "Currency code is required")
    @Size(min = 3, max = 3, message = "Currency code must be 3 characters")
    private String currencyCode;

    @NotNull(message = "Tariff rate is required")
    @DecimalMin(value = "0.0", message = "Tariff rate must be non-negative")
    private BigDecimal tariffRate;

    @DecimalMin(value = "0.0", message = "Unit quantity must be positive")
    private BigDecimal unitQuantity;  // amt of unit that tariff applies to

    @Size(max = 20, message = "Rate unit cannot exceed 20 characters")
    private String rateUnit;

    @NotBlank(message = "Tariff type is required")
    @Size(max = 50, message = "Tariff type cannot exceed 50 characters")
    private String tariffType;

    @NotNull(message = "Calculated tariff cost is required")
    @DecimalMin(value = "0.0", message = "Calculated tariff cost must be non-negative")
    private BigDecimal calculatedTariffCost;

    @NotNull(message = "Total cost is required")
    @DecimalMin(value = "0.0", message = "Total cost must be non-negative")
    private BigDecimal totalCost;

    @Size(max = 500, message = "Notes cannot exceed 500 characters")
    private String notes;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @NotNull(message = "User is required")
    private Long userId;

    @NotNull(message = "Importing country is required")
    private String importingCountryCode;

    @NotNull(message = "Exporting country is required")
    private String exportingCountryCode;

    @NotNull(message = "Product category is required")
    private Integer productCategoryCode;
}
