package com.cs203.core.dto.responses;

import com.cs203.core.dto.TariffRateDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Data
@Setter()
@AllArgsConstructor
@NoArgsConstructor
public class RouteDto {
    private String sourceCountryCode;
    private String destinationCountryCode;
    private TariffRateDto tariffRate;
    private BigDecimal cost;
}
