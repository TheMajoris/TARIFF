package com.cs203.core.dto;

import com.cs203.core.entity.CountryEntity;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateNationalTariffLinesDto {
    private Long id;

    @NotNull(message = "Country is required")
    private String countryCode;

    @NotBlank(message = "Tariff line code is required")
    @Size(max = 20, message = "Tariff line code cannot exceed 20 characters")
    private String tariffLineCode;

    @Size(max = 500, message = "Description cannot exceed 500 characters")
    private String description;

    @NotNull(message = "Parent HS Code is required")
    private String parentHsCode;

    @Min(value = 1, message = "Level must be at least 1")
    @Max(value = 10, message = "Level cannot exceed 10")
    private Integer level;
}
