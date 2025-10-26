package com.cs203.core.service;

import com.cs203.core.dto.CreateTariffRateDto;
import com.cs203.core.dto.GenericResponse;
import com.cs203.core.dto.TariffRateDto;
import com.cs203.core.entity.TariffRateEntity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TariffRateService {
    List<TariffRateDto> getAllTariffRates();

    Page<TariffRateDto> getTariffRates(Pageable pageable);

    GenericResponse<TariffRateDto> getTariffRateById(Long tariffRateId);

    GenericResponse<TariffRateDto> createTariffRate(CreateTariffRateDto createTariffRateDto);

    GenericResponse<TariffRateDto> updateTariffRate(TariffRateDto tariffRateDto, Long tariffRateId);

    GenericResponse<Void> deleteTariffRate(Long tariffRateId);

    BigDecimal getFinalPrice(Long importingCountryId, Long exportingCountryId, Integer hsCode,
                             BigDecimal initialPrice, BigDecimal quantity, LocalDate date);

    BigDecimal getLowestActiveTariffRate(Long importingCountryId, Long exportingCountryId, Integer hsCode,
                                         BigDecimal initialPrice, LocalDate date);

    Optional<TariffRateEntity> getLowestActiveTariff(Long importingCountryId, Long exportingCountryId, Integer hsCode,
                                                     BigDecimal initialPrice, LocalDate date);

    BigDecimal getTariffCost(BigDecimal finalPrice, BigDecimal initialPrice);
}
