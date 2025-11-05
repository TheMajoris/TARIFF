package com.cs203.core.controller;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.cs203.core.dto.responses.AlternativeRouteDto;
import com.cs203.core.dto.responses.RouteDto;
import com.cs203.core.service.RouteService;
import com.cs203.core.dto.GenericResponse;
import com.cs203.core.dto.ProductCategoriesDto;
import com.cs203.core.dto.TariffRateDto;
import com.cs203.core.dto.requests.RouteOptimizationRequestDto;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Routes Controller")
@RestController
@RequestMapping("/api/v1/route")
public class RouteController {

    @Autowired
    RouteService routeService;

    @PostMapping("/alternative")
    public ResponseEntity<GenericResponse<List<AlternativeRouteDto>>> getAlternativeRoute(@RequestBody @Valid RouteOptimizationRequestDto routeOptimizationRequestDto) {
        GenericResponse<List<AlternativeRouteDto>> response = routeService.getAlternativeRoute(routeOptimizationRequestDto);
        return new ResponseEntity<>(response, response.getStatus());
    }
}
