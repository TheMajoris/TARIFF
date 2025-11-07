package com.cs203.core.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AlternativeRouteDto {
    private List<RouteDto> routes;
    private BigDecimal tariffTotalCost;
    private BigDecimal totalCost;
}
