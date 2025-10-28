package com.cs203.core.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cs203.core.dto.GenericResponse;
import com.cs203.core.dto.SavedCalculationDto;
import com.cs203.core.dto.requests.SaveCalculationRequestDTO;
import com.cs203.core.entity.UserEntity;
import com.cs203.core.service.CalculationHistoryService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Calculation History")
@RestController
@RequestMapping("/api/v1/calculation-history")
public class CalculationHistoryController {

    @Autowired
    CalculationHistoryService calculationHistoryService;

    @Operation(summary = "Save user's calculation")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Calculation successfully saved."),
            @ApiResponse(responseCode = "400", description = "Invalid inputs, please check all fields for correct inputs."),
            @ApiResponse(responseCode = "401", description = "Session expired."),
            @ApiResponse(responseCode = "403", description = "Unauthorized user.")
    })
    @PostMapping
    public ResponseEntity<GenericResponse<SavedCalculationDto>> saveCalculation(
            @Valid @RequestBody SaveCalculationRequestDTO requestDto, Authentication authentication) {
        Long userId = extractUserId(authentication);
        GenericResponse<SavedCalculationDto> response = calculationHistoryService.saveCalculation(requestDto, userId);
        return new ResponseEntity<>(response, response.getStatus());
    }

    @Operation(summary = "Get calculations by user ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Calculations successfully retrieved."),
            @ApiResponse(responseCode = "400", description = "Invalid inputs, please check all fields for correct inputs."),
            @ApiResponse(responseCode = "401", description = "Session expired."),
            @ApiResponse(responseCode = "403", description = "Unauthorized user.")
    })
    @GetMapping
    public ResponseEntity<GenericResponse<List<SavedCalculationDto>>> getCalculationsByUserId(
            Authentication authentication) {
        Long userId = extractUserId(authentication);
        GenericResponse<List<SavedCalculationDto>> response = calculationHistoryService.getCalculationsByUserId(userId);
        return new ResponseEntity<>(response, response.getStatus());
    }

    private Long extractUserId(Authentication authentication) {
        Object principal = authentication.getPrincipal();

        if (principal instanceof Jwt) {
            // Production environment - JWT token
            Jwt jwt = (Jwt) principal;
            return Long.parseLong(jwt.getSubject());
        } else if (principal instanceof UserEntity) {
            // Test environment - UserEntity
            UserEntity user = (UserEntity) principal;
            return user.getId();
        } else {
            throw new IllegalArgumentException("Unsupported principal type: " + principal.getClass());
        }
    }

}
