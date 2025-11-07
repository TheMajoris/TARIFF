package com.cs203.core.dto;

import lombok.Data;
import lombok.Setter;

@Data
@Setter
public class CountryDto {
    private Long id;
    private String countryName;
    private String countryCode;
    private String region;
    private String currencyCode;
    private String currencyName;
}