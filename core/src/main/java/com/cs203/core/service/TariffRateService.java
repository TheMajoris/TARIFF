package com.cs203.core.service;

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
}
