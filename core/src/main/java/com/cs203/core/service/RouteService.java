package com.cs203.core.service;

import java.util.*;

import com.cs203.core.dto.GenericResponse;
import com.cs203.core.dto.ProductCategoriesDto;
import com.cs203.core.dto.requests.RouteOptimizationRequestDto;
import com.cs203.core.dto.responses.AlternativeRouteDto;
import com.cs203.core.dto.responses.RouteDto;

public interface RouteService {
    public GenericResponse<List<AlternativeRouteDto>> getAlternativeRoute(RouteOptimizationRequestDto routeOptimizationRequestDto);
}
