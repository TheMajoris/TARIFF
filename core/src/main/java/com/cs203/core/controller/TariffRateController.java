package com.cs203.core.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cs203.core.dto.CreateTariffRateDto;
import com.cs203.core.dto.GenericResponse;
import com.cs203.core.dto.TariffRateDto;
import com.cs203.core.dto.requests.TariffCalculatorRequestDto;
import com.cs203.core.dto.responses.TariffCalculatorResponseDto;
import com.cs203.core.entity.TariffRateEntity;
import com.cs203.core.service.TariffRateService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Tariff Rate")
@RestController
@RequestMapping("/api/v1/tariff-rate")
public class TariffRateController {

    @Autowired
    TariffRateService tariffRateService;

    @Operation(summary = "Get all tariff rates")
    @ApiResponse(responseCode = "200", description = "All tariff rates successfuly fetched.")
    @GetMapping("/all")
    public ResponseEntity<List<TariffRateDto>> getAllTariffRates() {
        return ResponseEntity.ok(tariffRateService.getAllTariffRates());
    }

    @Operation(summary = "Get tariff rates with page and limit")
    @ApiResponse(responseCode = "200", description = "Tariff rates successfuly fetched.")
    @ApiResponse(responseCode = "400", description = "Invalid page number")
    @ApiResponse(responseCode = "400", description = "Invalid page size")
    @ApiResponse(responseCode = "400", description = "Invalid property referenced")
    @ApiResponse(responseCode = "400", description = "Invalid sort direction")
    @GetMapping
    public ResponseEntity<GenericResponse<Page<TariffRateDto>>> getTariffRates(
            @RequestParam(defaultValue = "0") Integer pageNo, @RequestParam(defaultValue = "40") Integer size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "ascending") String sortDirection
        ) {
        GenericResponse<Page<TariffRateDto>> response;
        if (pageNo < 0) {
            response = new GenericResponse<Page<TariffRateDto>>(HttpStatus.BAD_REQUEST, "Invalid page number", null);
            return new ResponseEntity<>(response, response.getStatus());
        }
        if (size < 1) {
            response = new GenericResponse<Page<TariffRateDto>>(HttpStatus.BAD_REQUEST, "Invalid page size", null);
            return new ResponseEntity<>(response, response.getStatus());
        }

        if(sortBy.equals("importingCountryCode")){
            sortBy = "importingCountry.countryName";
        }else if(sortBy.equals("exportingCountryCode")){
            sortBy = "exportingCountry.countryName";
        }
        
        if (sortDirection.equals("ascending")) {
            Pageable pageable = PageRequest.of(pageNo, size, Sort.by(sortBy).ascending());
            response = new GenericResponse<Page<TariffRateDto>>(HttpStatus.OK, "Tariff rates successfuly fetched.", tariffRateService.getTariffRates(pageable));
            return new ResponseEntity<>(response, response.getStatus());
        } else if (sortDirection.equals("descending")) {
            Pageable pageable = PageRequest.of(pageNo, size, Sort.by(sortBy).descending());
            response = new GenericResponse<Page<TariffRateDto>>(HttpStatus.OK, "Tariff rates successfuly fetched.", tariffRateService.getTariffRates(pageable));
            return new ResponseEntity<>(response, response.getStatus());
        } else {
            response = new GenericResponse<Page<TariffRateDto>>(HttpStatus.BAD_REQUEST, "Invalid sort direction", null);
            return new ResponseEntity<>(response, response.getStatus());
        }
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
    @ApiResponse(responseCode = "400", description = "Data is invalid")
    @ApiResponse(responseCode = "404", description = "Importing Country code does not exist")
    @ApiResponse(responseCode = "404", description = "Exporting Country code does not exist")
    @ApiResponse(responseCode = "404", description = "HS code does not exist")
    @PostMapping
    public ResponseEntity<GenericResponse<TariffRateDto>> createTariffRate(@Valid @RequestBody CreateTariffRateDto createTariffRateDto) {
        GenericResponse<TariffRateDto> response = tariffRateService.createTariffRate(createTariffRateDto);
        return new ResponseEntity<>(response, response.getStatus());
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
    @ApiResponse(responseCode = "404", description = "No tariff data found for the specified countries and product")
    @PostMapping("/calculation")
    public ResponseEntity<TariffCalculatorResponseDto> getTariffCalculation(
            @Valid @RequestBody TariffCalculatorRequestDto requestBodyDTO) {
        Optional<TariffRateEntity> tariffRate = tariffRateService.getLowestActiveTariff(requestBodyDTO.importingCountryId(),
                requestBodyDTO.exportingCountryId(),
                requestBodyDTO.hsCode(),
                requestBodyDTO.initialPrice(),
                requestBodyDTO.date());
        if (tariffRate.isEmpty()) {
            return ResponseEntity.status(404).build();
        }

        BigDecimal finalPrice = tariffRateService.getFinalPrice(
                requestBodyDTO.importingCountryId(),
                requestBodyDTO.exportingCountryId(),
                requestBodyDTO.hsCode(),
                requestBodyDTO.initialPrice(),
                requestBodyDTO.quantity(),
                requestBodyDTO.date());
        BigDecimal tariffCost = tariffRateService.getTariffCost(finalPrice, requestBodyDTO.initialPrice());
        return ResponseEntity.ok(new TariffCalculatorResponseDto(tariffRate.get().getTariffRate(), tariffRate.get().getTariffType(), tariffRate.get().getRateUnit(), tariffRate.get().getUnitQuantity(), tariffCost, finalPrice));
    }
}