package com.cs203.core.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

import com.cs203.core.dto.CreateTariffRateDto;
import com.cs203.core.dto.GenericResponse;
import com.cs203.core.dto.TariffRateDto;

import jakarta.validation.Valid;

public interface TariffRateService {
    List<TariffRateDto> getAllTariffRates();

    GenericResponse<TariffRateDto> getTariffRateById(Long tariffRateId);

    TariffRateDto createTariffRate(CreateTariffRateDto createTariffRateDto);

    GenericResponse<TariffRateDto> updateTariffRate(TariffRateDto tariffRateDto, Long tariffRateId);

    GenericResponse<Void> deleteTariffRate(Long tariffRateId);

    BigDecimal getFinalPrice(Long importingCountryId, Long exportingCountryId, Integer hsCode,
            BigDecimal initialPrice, LocalDate date);

    BigDecimal getLowestActiveTariffRate(Long importingCountryId, Long exportingCountryId, Integer hsCode,
            BigDecimal initialPrice, LocalDate date);

    BigDecimal getTariffCost(BigDecimal finalPrice, BigDecimal initialPrice);
}
