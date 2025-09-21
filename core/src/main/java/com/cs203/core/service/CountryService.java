package com.cs203.core.service;

import java.util.List;

import com.cs203.core.dto.CountryDto;

public interface CountryService {
    List<CountryDto> getAllCountries();
}
