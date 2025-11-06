package com.cs203.core.dto.responses;

import java.math.BigDecimal;
import java.util.List;

import lombok.Data;

@Data
public class AlternativeRouteDto {
    private List<RouteDto> routes;
    private BigDecimal tariffTotalCost;
    private BigDecimal totalCost;
}
