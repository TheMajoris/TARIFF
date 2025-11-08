package com.cs203.core.controller;

import com.cs203.core.dto.GenericResponse;
import com.cs203.core.dto.TariffRateDto;
import com.cs203.core.dto.requests.RouteOptimizationRequestDto;
import com.cs203.core.dto.responses.AlternativeRouteDto;
import com.cs203.core.dto.responses.RouteDto;
import com.cs203.core.service.impl.RouteServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class RouteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RouteServiceImpl routeService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("POST /api/v1/route/alternative returns alternative routes")
    void getAlternativeRoute_returnsRoutes() throws Exception {

        LocalDate date = LocalDate.now();
        // Prepare request
        RouteOptimizationRequestDto requestDto = new RouteOptimizationRequestDto(
                "SG",
                "MY",
                101010,
                new BigDecimal("10.0"),
                new BigDecimal("1.0"),
                date
        );

        TariffRateDto tariffRateDto = new TariffRateDto();
        tariffRateDto.setId(1L);
        tariffRateDto.setTariffRate(new BigDecimal("0.1"));
        tariffRateDto.setTariffType("ad-valorem");
        tariffRateDto.setUnitQuantity(new BigDecimal("1.0"));
        tariffRateDto.setRateUnit("percent");
        tariffRateDto.setEffectiveDate(LocalDate.now());
        tariffRateDto.setImportingCountryCode("US");
        tariffRateDto.setExportingCountryCode("CN");


        RouteDto mockRouteDto = new RouteDto(
                "MY",
                "SG",
                tariffRateDto,
                new BigDecimal("10.00")
        );

        // Prepare mock response
        AlternativeRouteDto route1 = new AlternativeRouteDto(
                List.of(mockRouteDto),
                new BigDecimal("10.00"),
                new BigDecimal("100.00")
        );

        List<AlternativeRouteDto> routes = List.of(route1);

        GenericResponse<List<AlternativeRouteDto>> response = new GenericResponse<>(
                HttpStatus.OK,
                "Alternative routes retrieved",
                routes
        );

        Mockito.when(routeService.getAlternativeRoute(any(RouteOptimizationRequestDto.class)))
                .thenReturn(response);

        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post("/api/v1/route/alternative")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.status().isOk())
                .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath("$.message").value("Alternative routes retrieved"))
                .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath("$.data").isArray());
    }
}
