package com.cs203.core.TariffCalculator;

import com.cs203.core.entity.CountryEntity;
import com.cs203.core.entity.ProductCategoriesEntity;
import com.cs203.core.entity.TariffRateEntity;
import com.cs203.core.repository.TariffRateRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

class TariffCalculatorServiceTests {

    private CountryEntity country(long id) {
        CountryEntity c = new CountryEntity();
        c.setId(id);
        return c;
    }

    private ProductCategoriesEntity category(int code) {
        ProductCategoriesEntity p = new ProductCategoriesEntity();
        p.setCategoryCode(code);
        return p;
    }

    @Test
    @DisplayName("setTariffRate picks the lowest available rate")
    void setTariffRate_usesLowestRate() {
        TariffRateRepository repository = Mockito.mock(TariffRateRepository.class);
        TariffCalculatorService service = new TariffCalculatorService(repository);

        CountryEntity importing = country(1L);
        CountryEntity exporting = country(2L);
        ProductCategoriesEntity cat = category(123);

        TariffRateEntity high = new TariffRateEntity(
                importing, exporting, cat, new BigDecimal("0.20"), "ad-valorem", "percent",
                LocalDate.now(), null, false);
        TariffRateEntity low = new TariffRateEntity(
                importing, exporting, cat, new BigDecimal("0.10"), "ad-valorem", "percent",
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

    @Test
    @DisplayName("setTariffRate throws when repository returns no rates")
    void setTariffRate_noRates_throws() {
        TariffRateRepository repository = Mockito.mock(TariffRateRepository.class);
        TariffCalculatorService service = new TariffCalculatorService(repository);

        when(repository.findByImportingCountryIdAndExportingCountryIdAndHsCode(eq(1L), eq(2L), eq(123)))
                .thenReturn(Collections.emptyList());

        TariffCalculator calculator = new TariffCalculator(null, null);

        assertThrows(IndexOutOfBoundsException.class,
                () -> service.setTariffRate(calculator, 1L, 2L, 123));
    }
}
