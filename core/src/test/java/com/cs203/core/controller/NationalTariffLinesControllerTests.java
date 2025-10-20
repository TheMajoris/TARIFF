package com.cs203.core.controller;

import java.lang.reflect.Field;
import java.math.BigDecimal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import com.cs203.core.dto.CreateNationalTariffLinesDto;
import com.cs203.core.dto.GenericResponse;
import com.cs203.core.dto.NationalTariffLinesDto;
import com.cs203.core.dto.TariffRateDto;
import com.cs203.core.repository.CountryRepository;
import com.cs203.core.repository.NationalTariffLinesRepository;
import com.cs203.core.repository.ProductCategoriesRepository;
import com.cs203.core.service.NationalTariffLinesService;

@SpringBootTest
@AutoConfigureMockMvc

public class NationalTariffLinesControllerTests {
    @Autowired // Use the Spring-managed MockMvc
    private MockMvc mockMvc;

    @MockitoBean
    private NationalTariffLinesRepository nationalTariffLinesRepository;

    @MockitoBean
    private CountryRepository countryRepository;

    @MockitoBean
    private ProductCategoriesRepository productCategoriesRepository;

    @MockitoBean // This mocks the service in the Spring context
    private NationalTariffLinesService nationalTariffLinesService;

    @Test
    @DisplayName("POST /tariffs only accepts valid fields")
    void post_tariffs_acceptsOnlyValidFields() throws Exception {

        // Mock the service to return a dummy response
        NationalTariffLinesDto mockResponse = new NationalTariffLinesDto();
        // Set up mockResponse properties as needed
        when(nationalTariffLinesService.createNationalTariffLines(any(CreateNationalTariffLinesDto.class)))
                .thenReturn(mockResponse);

        String validRequest = """
                {
                "countryCode": "US",
                "tariffLineCode": "1001.10.00",
                "description": "High quality wheat",
                "parentHsCode": 1001,
                "level": 5
                }
                """;

        mockMvc.perform(MockMvcRequestBuilders.post("/tariffs")
                .contentType(MediaType.APPLICATION_JSON)
                .content(validRequest))
                .andExpect(MockMvcResultMatchers.status().isCreated());

        String nullCountryCode = """
                {
                    "countryCode": null,
                    "tariffLineCode": "1001.10.00",
                    "parentHsCode": 1001,
                    "level": 5
                }
                """;

        mockMvc.perform(MockMvcRequestBuilders.post("/tariffs")
                .contentType(MediaType.APPLICATION_JSON)
                .content(nullCountryCode))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(400))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message")
                        .value("Data may be invalid. Please check your inputs."));

        String blankTariffLineCode = """
                {
                "countryCode": "US",
                "tariffLineCode": null,
                "parentHsCode": 1001,
                "level": 5
                }
                """;

        mockMvc.perform(MockMvcRequestBuilders.post("/tariffs")
                .contentType(MediaType.APPLICATION_JSON)
                .content(blankTariffLineCode))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(400))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message")
                        .value("Data may be invalid. Please check your inputs."));

        String oversizedTariffLineCode = """
                {
                    "countryCode": "US",
                    "tariffLineCode": "123456789012345678901",
                    "parentHsCode": 1001,
                    "level": 5
                }
                """;

        mockMvc.perform(MockMvcRequestBuilders.post("/tariffs")
                .contentType(MediaType.APPLICATION_JSON)
                .content(oversizedTariffLineCode))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(400))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message")
                        .value("Data may be invalid. Please check your inputs."));

        String invalidLevel = """
                {
                    "countryCode": "US",
                    "tariffLineCode": "1001.10.00",
                    "parentHsCode": 1001,
                    "level": 0
                }
                """;

        mockMvc.perform(MockMvcRequestBuilders.post("/tariffs")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidLevel))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(400))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message")
                        .value("Data may be invalid. Please check your inputs."));

    }

    @Test
    @DisplayName("PUT /tariffs/{id} updates successfully with valid data")
    void put_nationalTariffLines_updatesSuccessfully() throws Exception {
        String validUpdateRequest = """
                {
                    "countryCode": "US",
                    "tariffLineCode": "1001.10.00",
                    "description": "Updated description",
                    "level": 5
                }
                """;

        // Create proper mock response
        NationalTariffLinesDto mockDto = new NationalTariffLinesDto();
        mockDto.setId(1L);
        mockDto.setCountryCode("US");
        mockDto.setTariffLineCode("1001.10.00");

        GenericResponse<NationalTariffLinesDto> mockResponse = new GenericResponse<>(
                HttpStatus.OK, "Successfully updated National Tariff Line", mockDto);

        when(nationalTariffLinesService.updateNationalTariffLines(any(NationalTariffLinesDto.class), eq(1L)))
                .thenReturn(mockResponse);

        mockMvc.perform(MockMvcRequestBuilders.put("/tariffs/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(validUpdateRequest))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("OK"))
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.message").value("Successfully updated National Tariff Line"));
    }

    @Test
    @DisplayName("PUT /tariffs/{id} returns 404 when entity not found")
    void put_nationalTariffLines_returns404WhenNotFound() throws Exception {
        String updateRequest = """
                {
                    "countryCode": "US",
                    "tariffLineCode": "1001.10.00",
                    "description": "Updated description",
                    "level": 5
                }
                """;

        GenericResponse<NationalTariffLinesDto> mockResponse = new GenericResponse<>(
                HttpStatus.NOT_FOUND, "National Tariff Line not found with id: ", null);

        when(nationalTariffLinesService.updateNationalTariffLines(any(NationalTariffLinesDto.class), eq(999L)))
                .thenReturn(mockResponse);

        mockMvc.perform(MockMvcRequestBuilders.put("/tariffs/999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(updateRequest))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("NOT_FOUND"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message")
                        .value("National Tariff Line not found with id: "));
    }

    @Test
    @DisplayName("PUT /tariffs/{id} returns 404 when country code not found")
    void put_nationalTariffLines_returns404WhenCountryNotFound() throws Exception {
        String updateRequestInvalidCountry = """
                {
                    "countryCode": "INVALID",
                    "tariffLineCode": "1001.10.00",
                    "description": "Updated description",
                    "level": 5
                }
                """;

        GenericResponse<NationalTariffLinesDto> mockResponse = new GenericResponse<>(
                HttpStatus.NOT_FOUND, "Country not found with code: INVALID", null);

        when(nationalTariffLinesService.updateNationalTariffLines(any(NationalTariffLinesDto.class), eq(1L)))
                .thenReturn(mockResponse);

        mockMvc.perform(MockMvcRequestBuilders.put("/tariffs/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(updateRequestInvalidCountry))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("NOT_FOUND"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message")
                        .value("Country not found with code: INVALID"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").isEmpty());
    }

    @Test
    @DisplayName("PUT /tariffs/{id} returns 404 when parent HS code not found")
    void put_nationalTariffLines_returns404WhenParentHsCodeNotFound() throws Exception {
        String updateRequestInvalidParentHsCode = """
                {
                    "countryCode": "US",
                    "tariffLineCode": "1001.10.00",
                    "description": "Updated description",
                    "parentHsCode": {
                        "categoryCode": "9999",
                        "categoryName": "Invalid Category"
                    },
                    "level": 5
                }
                """;

        GenericResponse<NationalTariffLinesDto> mockResponse = new GenericResponse<>(
                HttpStatus.NOT_FOUND, "Product Category not found with code: 9999", null);

        when(nationalTariffLinesService.updateNationalTariffLines(any(NationalTariffLinesDto.class), eq(1L)))
                .thenReturn(mockResponse);

        mockMvc.perform(MockMvcRequestBuilders.put("/tariffs/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(updateRequestInvalidParentHsCode))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("NOT_FOUND"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message")
                        .value("Product Category not found with code: 9999"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").isEmpty());
    }

    @Test
    @DisplayName("PUT /tariffs/{id} returns 404 when multiple validation failures")
    void put_nationalTariffLines_returns404WhenMultipleEntitiesNotFound() throws Exception {
        String updateRequestMultipleInvalid = """
                {
                    "countryCode": "US",
                    "tariffLineCode": "1001.10.00",
                    "description": "Updated description",
                    "parentHsCode": {
                        "categoryCode": 9999,
                        "categoryName": "Invalid Category"
                    },
                    "level": 5
                }
                """;

        // Service will return first error it encounters (usually country code check
        // comes first)
        GenericResponse<NationalTariffLinesDto> mockResponse = new GenericResponse<>(
                HttpStatus.NOT_FOUND, "Country not found with code: INVALID_COUNTRY", null);

        when(nationalTariffLinesService.updateNationalTariffLines(any(NationalTariffLinesDto.class), eq(1L)))
                .thenReturn(mockResponse);

        mockMvc.perform(MockMvcRequestBuilders.put("/tariffs/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(updateRequestMultipleInvalid))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("NOT_FOUND"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message")
                        .value("Country not found with code: INVALID_COUNTRY"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").isEmpty());

        verify(nationalTariffLinesService).updateNationalTariffLines(any(NationalTariffLinesDto.class), eq(1L));
    }

    @Test
    @DisplayName("PUT /tariffs/{id} returns 404 when null parent HS code provided")
    void put_nationalTariffLines_returns404WhenNullParentHsCode() throws Exception {
        String updateRequestNullParentHsCode = """
                {
                    "countryCode": "US",
                    "tariffLineCode": "1001.10.00",
                    "description": "Updated description",
                    "parentHsCode": null,
                    "level": 5
                }
                """;

        GenericResponse<NationalTariffLinesDto> mockResponse = new GenericResponse<>(
                HttpStatus.NOT_FOUND, "Product Category not found with code: null", null);

        when(nationalTariffLinesService.updateNationalTariffLines(any(NationalTariffLinesDto.class), eq(1L)))
                .thenReturn(mockResponse);

        mockMvc.perform(MockMvcRequestBuilders.put("/tariffs/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(updateRequestNullParentHsCode))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("NOT_FOUND"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message")
                        .value("Product Category not found with code: null"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").isEmpty());
    }

    @Test
    @DisplayName("DELETE /tariffs/{id} successfully deletes existing entity")
    void delete_nationalTariffLines_deletesSuccessfully() throws Exception {
        // Mock successful deletion response
        GenericResponse<Void> mockResponse = new GenericResponse<>(
                HttpStatus.OK, "Successfully deleted National Tariff Line", null);

        when(nationalTariffLinesService.deleteNationalTariffLines(eq(1L)))
                .thenReturn(mockResponse);

        mockMvc.perform(MockMvcRequestBuilders.delete("/tariffs/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("OK"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message")
                        .value("Successfully deleted National Tariff Line"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").isEmpty());

        // Verify the service method was called with correct ID
        verify(nationalTariffLinesService).deleteNationalTariffLines(eq(1L));
    }

    @Test
    @DisplayName("DELETE /tariffs/{id} returns 404 when entity not found")
    void delete_nationalTariffLines_returns404WhenNotFound() throws Exception {
        // Mock not found response
        GenericResponse<Void> mockResponse = new GenericResponse<>(
                HttpStatus.NOT_FOUND, "Nationl Tariff Line not found with id: ", null);

        when(nationalTariffLinesService.deleteNationalTariffLines(eq(999L)))
                .thenReturn(mockResponse);

        mockMvc.perform(MockMvcRequestBuilders.delete("/tariffs/999"))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("NOT_FOUND"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message")
                        .value("Nationl Tariff Line not found with id: "))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").isEmpty());

        // Verify the service method was called with correct ID
        verify(nationalTariffLinesService).deleteNationalTariffLines(eq(999L));
    }

    @Test
    @DisplayName("DELETE /tariffs/{id} handles different ID formats")
    void delete_nationalTariffLines_handlesVariousIds() throws Exception {
        // Test with different valid ID formats
        Long[] testIds = { 1L, 100L, 9999L };

        for (Long testId : testIds) {
            GenericResponse<Void> mockResponse = new GenericResponse<>(
                    HttpStatus.OK, "Successfully deleted National Tariff Line", null);

            when(nationalTariffLinesService.deleteNationalTariffLines(eq(testId)))
                    .thenReturn(mockResponse);

            mockMvc.perform(MockMvcRequestBuilders.delete("/tariffs/" + testId))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("OK"));

            verify(nationalTariffLinesService).deleteNationalTariffLines(eq(testId));

            // Reset mock for next iteration
            reset(nationalTariffLinesService);
        }
    }

}
