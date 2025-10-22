package com.cs203.core.service;

import java.util.List;

import com.cs203.core.dto.GenericResponse;
import com.cs203.core.dto.SavedCalculationDto;
import com.cs203.core.dto.requests.SaveCalculationRequestDTO;

public interface CalculationHistoryService {
    public GenericResponse<SavedCalculationDto> saveCalculation(SaveCalculationRequestDTO requestDTO, Long userId);

    public GenericResponse<List<SavedCalculationDto>> getCalculationsByUserId(Long userId);
}
