package com.cs203.core.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.cs203.core.entity.CountryEntity;
import com.cs203.core.entity.ProductCategoriesEntity;
import com.cs203.core.entity.TariffRateEntity;
import com.cs203.core.repository.TariffRateRepository;
import com.cs203.core.service.TariffRateService;

class TariffRateServiceTest {

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
    @DisplayName("getFinalPrice picks lowest rate and computes final price")
    void getFinalPrice_usesLowestRate() {
        TariffRateRepository repository = Mockito.mock(TariffRateRepository.class);
        TariffRateService service = new TariffRateServiceImpl();

        // Wire mock repo into service via reflection (field injection in impl)
        try {
            java.lang.reflect.Field f = TariffRateServiceImpl.class.getDeclaredField("tariffRateRepository");
            f.setAccessible(true);
            f.set(service, repository);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

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
        when(repository.findByImportingCountryIdAndExportingCountryIdAndHsCode(eq(1L), eq(2L), eq(123))).thenReturn(rates);

        BigDecimal result = service.getFinalPrice(1L, 2L, 123, new BigDecimal("100.00"));

        assertEquals(0, result.compareTo(new BigDecimal("110.00")));
    }

    @Test
    @DisplayName("getFinalPrice with no rates returns initial price")
    void getFinalPrice_noRates_returnsInitialPrice() {
        TariffRateRepository repository = Mockito.mock(TariffRateRepository.class);
        TariffRateService service = new TariffRateServiceImpl();

        try {
            java.lang.reflect.Field f = TariffRateServiceImpl.class.getDeclaredField("tariffRateRepository");
            f.setAccessible(true);
            f.set(service, repository);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        when(repository.findByImportingCountryIdAndExportingCountryIdAndHsCode(eq(1L), eq(2L), eq(123)))
                .thenReturn(Collections.emptyList());

        BigDecimal result = service.getFinalPrice(1L, 2L, 123, new BigDecimal("100.00"));

        assertEquals(0, result.compareTo(new BigDecimal("100.00")));
    }

    @Test
    @DisplayName("getLowestTariffRate returns lowest rate from multiple rates")
    void getLowestTariffRate_returnsLowestRate() {
        TariffRateRepository repository = Mockito.mock(TariffRateRepository.class);
        TariffRateService service = new TariffRateServiceImpl();

        try {
            java.lang.reflect.Field f = TariffRateServiceImpl.class.getDeclaredField("tariffRateRepository");
            f.setAccessible(true);
            f.set(service, repository);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

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
        when(repository.findByImportingCountryIdAndExportingCountryIdAndHsCode(eq(1L), eq(2L), eq(123))).thenReturn(rates);

        BigDecimal result = service.getLowestTariffRate(1L, 2L, 123, new BigDecimal("100.00"));

        assertEquals(0, result.compareTo(new BigDecimal("0.10")));
    }

    @Test
    @DisplayName("getLowestTariffRate with no rates returns zero")
    void getLowestTariffRate_noRates_returnsZero() {
        TariffRateRepository repository = Mockito.mock(TariffRateRepository.class);
        TariffRateService service = new TariffRateServiceImpl();

        try {
            java.lang.reflect.Field f = TariffRateServiceImpl.class.getDeclaredField("tariffRateRepository");
            f.setAccessible(true);
            f.set(service, repository);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        when(repository.findByImportingCountryIdAndExportingCountryIdAndHsCode(eq(1L), eq(2L), eq(123)))
                .thenReturn(Collections.emptyList());

        BigDecimal result = service.getLowestTariffRate(1L, 2L, 123, new BigDecimal("100.00"));

        assertEquals(0, result.compareTo(BigDecimal.ZERO));
    }

    @Test
    @DisplayName("getTariffCost returns difference between final and initial price")
    void getTariffCost_returnsDifference() {
        TariffRateService service = new TariffRateServiceImpl();

        BigDecimal finalPrice = new BigDecimal("110.00");
        BigDecimal initialPrice = new BigDecimal("100.00");

        BigDecimal result = service.getTariffCost(finalPrice, initialPrice);

        assertEquals(0, result.compareTo(new BigDecimal("10.00")));
    }

    @Test
    @DisplayName("getTariffCost with equal prices returns zero")
    void getTariffCost_equalPrices_returnsZero() {
        TariffRateService service = new TariffRateServiceImpl();

        BigDecimal finalPrice = new BigDecimal("100.00");
        BigDecimal initialPrice = new BigDecimal("100.00");

        BigDecimal result = service.getTariffCost(finalPrice, initialPrice);

        assertEquals(0, result.compareTo(BigDecimal.ZERO));
    }
}


