package com.cs203.core.controller;

import java.math.BigDecimal;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cs203.core.dto.CreateTariffRateDto;
import com.cs203.core.dto.GenericResponse;
import com.cs203.core.dto.TariffRateDto;
import com.cs203.core.dto.requests.TariffCalculatorRequestDto;
import com.cs203.core.dto.responses.TariffCalculatorResponseDto;
import com.cs203.core.service.TariffRateService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/tariff-rate")
public class TariffRateController {

    @Autowired
    TariffRateService tariffRateService;

    @GetMapping
    public ResponseEntity<List<TariffRateDto>> getAllTariffRates() {
        return ResponseEntity.ok(tariffRateService.getAllTariffRates());
    }

    @GetMapping("/{tariffRateId}")
    public ResponseEntity<GenericResponse<TariffRateDto>> getTariffRateById(@PathVariable Long tariffRateId) {
        GenericResponse<TariffRateDto> response = tariffRateService.getTariffRateById(tariffRateId);
        return new ResponseEntity<>(response, response.getStatus());
    }

    @PostMapping("/create")
    public ResponseEntity<TariffRateDto> createTariffRate(@Valid @RequestBody CreateTariffRateDto createTariffRateDto) {
        return new ResponseEntity<>(tariffRateService.createTariffRate(createTariffRateDto), HttpStatus.CREATED);
    }

    @PutMapping("/{tariffRateId}")
    public ResponseEntity<GenericResponse<TariffRateDto>> updateTariffRate(
            @Valid @RequestBody TariffRateDto tariffRateDto, @PathVariable Long tariffRateId) {
        GenericResponse<TariffRateDto> response = tariffRateService.updateTariffRate(tariffRateDto, tariffRateId);
        return new ResponseEntity<>(response, response.getStatus());
    }

    @DeleteMapping("/{tariffRateId}")
    public ResponseEntity<GenericResponse<Void>> deleteTariffRate(@PathVariable Long tariffRateId) {
        GenericResponse<Void> response = tariffRateService.deleteTariffRate(tariffRateId);
        return new ResponseEntity<>(response, response.getStatus());
    }

    // using the params from request DTO, create final price n return response as DTO
    @PostMapping("/calculate")
    public ResponseEntity<TariffCalculatorResponseDto> getTariffCalculation(@RequestBody TariffCalculatorRequestDto requestBodyDTO) {
        BigDecimal finalPrice = tariffRateService.getFinalPrice(
                requestBodyDTO.importingCountryId(),
                requestBodyDTO.exportingCountryId(),
                requestBodyDTO.hsCode(),
                requestBodyDTO.initialPrice()
        );
        return ResponseEntity.ok(new TariffCalculatorResponseDto(finalPrice));
    }
}