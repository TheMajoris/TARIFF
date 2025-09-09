package com.cs203.core.TariffCalculator;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doAnswer;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

class TariffCalculatorControllerTests {

	@Test
	@DisplayName("GET /Calculator creates calculator and returns tariff and final price")
	void calculator_returnsTariffAndFinalPrice() throws Exception {
		TariffCalculatorService service = Mockito.mock(TariffCalculatorService.class);
		TariffCalculatorController controller = new TariffCalculatorController(service);

		// When controller asks service to set the tariff rate, set it to 0.10
		doAnswer(invocation -> {
			TariffCalculator calc = invocation.getArgument(0);
			calc.setTariffRate(new BigDecimal("0.10"));
			return null;
		}).when(service).setTariffRate(any(TariffCalculator.class), eq(1L), eq(2L), eq(123));

		// When controller asks service to set the final price, compute price + price*tariff
		doAnswer(invocation -> {
			BigDecimal initialPrice = invocation.getArgument(0);
			TariffCalculator calc = invocation.getArgument(1);
			BigDecimal finalPrice = initialPrice.add(initialPrice.multiply(calc.getTariffRate()));
			calc.setFinalPrice(finalPrice);
			return null;
		}).when(service).setFinalPrice(eq(new BigDecimal("100.00")), any(TariffCalculator.class));

		MockMvc mockMvc = MockMvcBuilders.standaloneSetup(controller).build();

		mockMvc.perform(get("/Calculator")
				.param("importingCountryId", "1")
				.param("exportingCountryId", "2")
				.param("hsCode", "123")
				.param("initialPrice", "100.00"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.tariffRate").value(0.10))
			.andExpect(jsonPath("$.finalPrice").value(110.00));
	}
}


