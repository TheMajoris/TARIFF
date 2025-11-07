package com.cs203.core.controller;

import com.cs203.core.dto.CountryDto;
import com.cs203.core.service.impl.CountryServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class CountryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CountryServiceImpl countryService;

    @Test
    @DisplayName("GET /api/v1/countries returns all countries")
    void getAllCountries_returnsCountries() throws Exception {
        CountryDto countryDto = new CountryDto();
        countryDto.setId(1L);
        countryDto.setCountryName("Singapore");
        countryDto.setCountryCode("SG");
        countryDto.setRegion("Asia");
        countryDto.setCurrencyCode("SGD");
        countryDto.setCurrencyName("Singaporean Dollar");

        List<CountryDto> countries = List.of(countryDto);

        // Mock service call
        Mockito.when(countryService.getAllCountries()).thenReturn(countries);

        // Perform GET request
        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get("/api/v1/countries")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].countryName").value("Singapore"))
                .andExpect(jsonPath("$[0].countryCode").value("SG"))
                .andExpect(jsonPath("$[0].region").value("Asia"))
                .andExpect(jsonPath("$[0].currencyCode").value("SGD"))
                .andExpect(jsonPath("$[0].currencyName").value("Singaporean Dollar"));
    }
}
