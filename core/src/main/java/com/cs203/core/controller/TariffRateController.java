package com.cs203.core.controller;

import com.cs203.core.dto.CreateTariffRateDto;
import com.cs203.core.dto.GenericResponse;
import com.cs203.core.dto.TariffRateDto;
import com.cs203.core.dto.requests.TariffCalculatorRequestDto;
import com.cs203.core.dto.responses.TariffCalculatorResponseDto;
import com.cs203.core.service.TariffRateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@Tag(name = "Tariff Rate")
@RestController
@RequestMapping("/api/v1/tariff-rate")
public class TariffRateController {

    @Autowired
    TariffRateService tariffRateService;

    @Operation(summary = "Get all tariff rates")
    @ApiResponse(responseCode = "200", description = "All tariff rates successfuly fetched.")
    @GetMapping
    public ResponseEntity<List<TariffRateDto>> getAllTariffRates() {
        return ResponseEntity.ok(tariffRateService.getAllTariffRates());
    }

    @Operation(summary = "Get tariff rate by id")
    @ApiResponse(responseCode = "200", description = "Found")
    @ApiResponse(responseCode = "404", description = "Tariff Rate not found with id")
    @GetMapping("/{tariffRateId}")
    public ResponseEntity<GenericResponse<TariffRateDto>> getTariffRateById(@PathVariable Long tariffRateId) {
        GenericResponse<TariffRateDto> response = tariffRateService.getTariffRateById(tariffRateId);
        return new ResponseEntity<>(response, response.getStatus());
    }

    @Operation(summary = "Create a new tariff rate with the response body")
    @ApiResponse(responseCode = "201", description = "Tariff rate created")
    @ApiResponse(responseCode = "400", description = "Create request is invalid")
    @PostMapping
    public ResponseEntity<TariffRateDto> createTariffRate(@Valid @RequestBody CreateTariffRateDto createTariffRateDto) {
        return new ResponseEntity<>(tariffRateService.createTariffRate(createTariffRateDto), HttpStatus.CREATED);
    }

    @Operation(summary = "Update tariff rate by id")
    @ApiResponse(responseCode = "200", description = "Successfully updated Tariff Rate")
    @ApiResponse(responseCode = "404", description = "Tariff Rate not found with id")
    @ApiResponse(responseCode = "404", description = "Importing Country not found with code")
    @ApiResponse(responseCode = "404", description = "Exporting Country not found with code")
    @ApiResponse(responseCode = "404", description = "Product Category not found with code")
    @PutMapping("/{tariffRateId}")
    public ResponseEntity<GenericResponse<TariffRateDto>> updateTariffRate(
            @Valid @RequestBody TariffRateDto tariffRateDto, @PathVariable Long tariffRateId) {
        GenericResponse<TariffRateDto> response = tariffRateService.updateTariffRate(tariffRateDto, tariffRateId);
        return new ResponseEntity<>(response, response.getStatus());
    }

    @Operation(summary = "Delete tariff rate by id")
    @ApiResponse(responseCode = "200", description = "Successfully deleted Tariff Rate")
    @ApiResponse(responseCode = "404", description = "Tariff Rate not found with id")
    @DeleteMapping("/{tariffRateId}")
    public ResponseEntity<GenericResponse<Void>> deleteTariffRate(@PathVariable Long tariffRateId) {
        GenericResponse<Void> response = tariffRateService.deleteTariffRate(tariffRateId);
        return new ResponseEntity<>(response, response.getStatus());
    }

    // using the params from request DTO, create final price n return response as
    // DTO
    @Operation(summary = "Calculate the tariff cost given tariff and price")
    @ApiResponse(responseCode = "200", description = "Request successful")
    @ApiResponse(responseCode = "400", description = "Calculate request is invalid")
    @PostMapping("/calculate")
    public ResponseEntity<TariffCalculatorResponseDto> getTariffCalculation(
            @RequestBody TariffCalculatorRequestDto requestBodyDTO) {
        BigDecimal tariffRate = tariffRateService.getLowestActiveTariffRate(
                requestBodyDTO.importingCountryId(),
                requestBodyDTO.exportingCountryId(),
                requestBodyDTO.hsCode(),
                requestBodyDTO.initialPrice(),
                requestBodyDTO.date());
        BigDecimal finalPrice = tariffRateService.getFinalPrice(
                requestBodyDTO.importingCountryId(),
                requestBodyDTO.exportingCountryId(),
                requestBodyDTO.hsCode(),
                requestBodyDTO.initialPrice(),
                requestBodyDTO.date());
        BigDecimal tariffCost = tariffRateService.getTariffCost(finalPrice, requestBodyDTO.initialPrice());
        return ResponseEntity.ok(new TariffCalculatorResponseDto(tariffRate, tariffCost, finalPrice));
    }
}