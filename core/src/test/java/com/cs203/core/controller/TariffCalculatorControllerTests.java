package com.cs203.core.controller;

import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.lang.reflect.Field;
import java.math.BigDecimal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.cs203.core.service.TariffRateService;

class TariffRateControllerTest {

    @Test
    @DisplayName("POST /api/v1/tariff-rate/calculate returns TariffCalculatorResponseDto with tariffRate, tariffCost, and finalPrice")
    void post_calculate_returnsTariffCalculatorResponseDto() throws Exception {
        TariffRateService service = Mockito.mock(TariffRateService.class);
        TariffRateController controller = new TariffRateController();

        // Inject mocked service into controller (field injection)
        Field f = TariffRateController.class.getDeclaredField("tariffRateService");
        f.setAccessible(true);
        f.set(controller, service);

        // Mock the service methods to return expected values
        Mockito.when(service.getLowestTariffRate(eq(1L), eq(2L), eq(123), eq(new BigDecimal("100.00"))))
            .thenReturn(new BigDecimal("0.10"));
        Mockito.when(service.getFinalPrice(eq(1L), eq(2L), eq(123), eq(new BigDecimal("100.00"))))
            .thenReturn(new BigDecimal("110.00"));
        Mockito.when(service.getTariffCost(eq(new BigDecimal("110.00")), eq(new BigDecimal("100.00"))))
            .thenReturn(new BigDecimal("10.00"));

        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(controller).build();

        String requestJson = "{\"importingCountryId\":1,\"exportingCountryId\":2,\"hsCode\":123,\"initialPrice\":100.00}";

        mockMvc.perform(post("/api/v1/tariff-rate/calculate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.tariffRate").value(0.10))
            .andExpect(jsonPath("$.tariffCost").value(10.00))
            .andExpect(jsonPath("$.finalPrice").value(110.00));
    }

    @Test
    @DisplayName("POST /api/v1/tariff-rate/calculate with zero tariff rate returns zero tariff cost")
    void post_calculate_zeroTariffRate_returnsZeroTariffCost() throws Exception {
        TariffRateService service = Mockito.mock(TariffRateService.class);
        TariffRateController controller = new TariffRateController();

        // Inject mocked service into controller (field injection)
        Field f = TariffRateController.class.getDeclaredField("tariffRateService");
        f.setAccessible(true);
        f.set(controller, service);

        // Mock the service methods to return expected values for zero tariff rate
        Mockito.when(service.getLowestTariffRate(eq(1L), eq(2L), eq(123), eq(new BigDecimal("100.00"))))
            .thenReturn(BigDecimal.ZERO);
        Mockito.when(service.getFinalPrice(eq(1L), eq(2L), eq(123), eq(new BigDecimal("100.00"))))
            .thenReturn(new BigDecimal("100.00"));
        Mockito.when(service.getTariffCost(eq(new BigDecimal("100.00")), eq(new BigDecimal("100.00"))))
            .thenReturn(BigDecimal.ZERO);

        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(controller).build();

        String requestJson = "{\"importingCountryId\":1,\"exportingCountryId\":2,\"hsCode\":123,\"initialPrice\":100.00}";

        mockMvc.perform(post("/api/v1/tariff-rate/calculate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.tariffRate").value(0))
            .andExpect(jsonPath("$.tariffCost").value(0))
            .andExpect(jsonPath("$.finalPrice").value(100.00));
    }
}



