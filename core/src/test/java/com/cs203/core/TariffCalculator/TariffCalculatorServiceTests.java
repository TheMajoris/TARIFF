package com.cs203.core.TariffCalculator;

import com.cs203.core.entity.TariffRateEntity;
import com.cs203.core.repository.TariffRateRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class TariffCalculatorServiceTests {

    @Test
    @DisplayName("setTariffRate picks the lowest available rate")
    void setTariffRate_usesLowestRate() {
        TariffRateRepository repository = Mockito.mock(TariffRateRepository.class);
        TariffCalculatorService service = new TariffCalculatorService(repository);

        TariffRateEntity high = new TariffRateEntity(
                1L, 2L, 123, new BigDecimal("0.20"), "ad-valorem", "percent",
                LocalDate.now(), null, false);
        TariffRateEntity low = new TariffRateEntity(
                1L, 2L, 123, new BigDecimal("0.10"), "ad-valorem", "percent",
                LocalDate.now(), null, false);
        List<TariffRateEntity> rates = Arrays.asList(high, low);
        when(repository.findByImportingCountryIdAndExportingCountryIdAndHsCode(1L, 2L, 123)).thenReturn(rates);

        TariffCalculator calculator = new TariffCalculator(null, null);
        service.setTariffRate(calculator, 1L, 2L, 123);

        assertEquals(0, calculator.getTariffRate().compareTo(new BigDecimal("0.10")));
    }

    @Test
    @DisplayName("setFinalPrice computes price + (price * tariffRate)")
    void setFinalPrice_computesCorrectly() {
        TariffRateRepository repository = Mockito.mock(TariffRateRepository.class);
        TariffCalculatorService service = new TariffCalculatorService(repository);

        TariffCalculator calculator = new TariffCalculator(new BigDecimal("0.10"), null);
        service.setFinalPrice(new BigDecimal("100.00"), calculator);

        assertEquals(0, calculator.getFinalPrice().compareTo(new BigDecimal("110.00")));
    }
}


