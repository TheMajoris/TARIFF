package com.cs203.core.dto.requests;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RouteOptimizationRequestDto {
    @NotBlank(message = "Importing country code is required")
    @Size(min = 2, max = 2, message = "Country code must be 2 characters")
    private String importingCountryCode;

    @NotBlank(message = "Exporting country code is required")
    @Size(min = 2, max = 2, message = "Country code must be 2 characters")
    private String exportingCountryCode;

    
    @NotNull(message = "HS code is required")
    @Min(value = 100000, message = "HS code must be at least 6 digits")
    @Max(value = 999999, message = "HS code cannot exceed 6 digits")
    private Integer hsCode;

    @Positive(message = "Initial price must be positive")
    @NotNull(message = "Initial price is required")
    private BigDecimal initialPrice; 

    @Positive(message = "Quantity must be positive")
    @NotNull(message = "Quantity is required")
    private BigDecimal quantity;

    @NotNull(message = "Date is required")
    private LocalDate date;
}
