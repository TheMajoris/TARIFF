package com.cs203.core.dto.requests;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.Data;

@Data
public class RouteOptimizationRequestDto {
    private String importingCountryCode;
    private String exportingCountryCode;
    private Integer hsCode;
    private BigDecimal initialPrice; 
    private BigDecimal quantity;
    private LocalDate date;
}
