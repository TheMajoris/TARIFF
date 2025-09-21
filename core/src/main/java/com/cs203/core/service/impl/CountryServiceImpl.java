package com.cs203.core.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs203.core.dto.CountryDto;
import com.cs203.core.entity.CountryEntity;
import com.cs203.core.repository.CountryRepository;
import com.cs203.core.service.CountryService;

@Service
public class CountryServiceImpl implements CountryService {

    @Autowired
    private CountryRepository countryRepository;

    @Override
    public List<CountryDto> getAllCountries() {
        List<CountryEntity> countries = countryRepository.findAllByOrderByCountryNameAsc();
        return countries.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private CountryDto convertToDto(CountryEntity entity) {
        CountryDto dto = new CountryDto();
        dto.setId(entity.getId());
        dto.setCountryName(entity.getCountryName());
        dto.setCountryCode(entity.getCountryCode());
        dto.setRegion(entity.getRegion());
        dto.setCurrencyCode(entity.getCurrencyCode());
        dto.setCurrencyName(null); // CountryEntity doesn't have currencyName field
        return dto;
    }
}
