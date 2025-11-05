package com.cs203.core.dto.responses;

import java.math.BigDecimal;

import com.cs203.core.dto.TariffRateDto;

import lombok.Data;

@Data
public class RouteDto {
    private String sourceCountryCode;
    private String destinationCountryCode;
    private TariffRateDto tariffRate;
    private BigDecimal cost;
}
