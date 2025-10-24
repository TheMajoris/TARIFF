package com.cs203.core.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
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

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;


@Tag(name = "Calculation History")
@RestController
@RequestMapping("/api/v1/calculation-history")
public class CalculationHistoryController {
    
    @Autowired
    CalculationHistoryService calculationHistoryService;

    @PostMapping
    public ResponseEntity<GenericResponse<SavedCalculationDto>> saveCalculation(
        @Valid @RequestBody SaveCalculationRequestDTO requestDto, Authentication authentication) {
            UserEntity user = (UserEntity) authentication.getPrincipal();
            Long userId = user.getId();
            GenericResponse<SavedCalculationDto> response = calculationHistoryService.saveCalculation(requestDto,userId);
        return new ResponseEntity<>(response, response.getStatus());
    }

    @GetMapping
    public ResponseEntity<GenericResponse<List<SavedCalculationDto>>> getCalculationsByUserId(Authentication authentication) {
            UserEntity user = (UserEntity) authentication.getPrincipal();
            Long userId = user.getId();
            GenericResponse<List<SavedCalculationDto>> response = calculationHistoryService.getCalculationsByUserId(userId);
        return new ResponseEntity<>(response, response.getStatus());
    }    
    
}
