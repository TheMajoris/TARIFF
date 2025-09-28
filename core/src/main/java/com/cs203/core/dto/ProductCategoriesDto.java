package com.cs203.core.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ProductCategoriesDto {
    private Long id;

    @NotNull(message = "HS code is required")
    @Min(value = 100000, message = "HS code must be at least 6 digits")
    @Max(value = 999999, message = "HS code cannot exceed 6 digits")
    private Integer categoryCode;

    @NotBlank(message = "Category name is required")
    @Size(max = 100, message = "Category name cannot exceed 100 characters")
    private String categoryName;

    @Size(max = 500, message = "Description cannot exceed 500 characters")
    private String description;
    
    @NotNull(message = "isActive has to be set")
    private Boolean isActive;
}
