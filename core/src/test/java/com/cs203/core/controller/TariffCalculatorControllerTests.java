package com.cs203.core.TariffCalculator;

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

class TariffCalculatorControllerTests {

    @Test
    @DisplayName("POST /api/v1/tariff-rate/calculate returns final price via DTOs")
    void post_calculate_returnsFinalPrice() throws Exception {
        TariffCalculatorService service = Mockito.mock(TariffCalculatorService.class);
        TariffCalculatorController controller = new TariffCalculatorController();

        // Inject mocked service into controller (field injection)
        Field f = TariffCalculatorController.class.getDeclaredField("tariffCalculatorService");
        f.setAccessible(true);
        f.set(controller, service);

        Mockito.when(service.getFinalPrice(eq(1L), eq(2L), eq(123), eq(new BigDecimal("100.00"))))
            .thenReturn(new BigDecimal("110.00"));

        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(controller).build();

        String requestJson = "{\"importingCountryId\":1,\"exportingCountryId\":2,\"hsCode\":123,\"initialPrice\":100.00}";

        mockMvc.perform(post("/api/v1/tariff-rate/calculate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.finalPrice").value(110.00));
    }
}



