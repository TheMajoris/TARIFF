package com.cs203.core.controller;

import java.lang.reflect.Field;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import com.cs203.core.dto.CreateNationalTariffLinesDto;
import com.cs203.core.dto.NationalTariffLinesDto;
import com.cs203.core.service.NationalTariffLinesService;

@SpringBootTest
@AutoConfigureMockMvc

public class NationalTariffLinesControllerTests {
    @Autowired // Use the Spring-managed MockMvc
    private MockMvc mockMvc;

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
}
