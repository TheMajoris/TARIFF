package com.cs203.core.controller;

import com.cs203.core.dto.CountryDto;
import com.cs203.core.service.CountryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/countries")
@Tag(name = "Country")
public class CountryController {

    @Autowired
    private CountryService countryService;

    @GetMapping
    @Operation(summary = "Get all countries")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "All countries successfully fetched.")
    })
    public ResponseEntity<List<CountryDto>> getAllCountries() {
        return ResponseEntity.ok(countryService.getAllCountries());
    }

}