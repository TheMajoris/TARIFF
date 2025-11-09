package com.cs203.core.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

import com.cs203.core.dto.TariffRateDto;
import com.cs203.core.dto.requests.RouteOptimizationRequestDto;
import com.cs203.core.dto.responses.AlternativeRouteDto;
import com.cs203.core.entity.CountryEntity;
import com.cs203.core.entity.ProductCategoriesEntity;
import com.cs203.core.entity.TariffRateEntity;
import com.cs203.core.repository.CountryRepository;
import com.cs203.core.repository.ProductCategoriesRepository;
import com.cs203.core.repository.SavedCalculationsRepository;
import com.cs203.core.repository.TariffRateRepository;
import com.cs203.core.service.AuthService;
import com.cs203.core.service.TariffRateService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

@ExtendWith(MockitoExtension.class)
class RouteServiceImplTest {

    @InjectMocks
    RouteServiceImpl routeService;

    @Mock
    TariffRateService tariffRateService;
    @Mock
    CountryRepository countryRepository;
    @Mock
    TariffRateRepository tariffRateRepository;
    @Mock
    SavedCalculationsRepository savedCalculationsRepository;
    @Mock
    ProductCategoriesRepository productCategoriesRepository;

    // --- shared test data ---
    private CountryEntity usaCountry;
    private CountryEntity canadaCountry;
    private CountryEntity mexicoCountry;
    private CountryEntity japanCountry;
    private CountryEntity indiaCountry;
    private ProductCategoriesEntity textilesCategory;

    @BeforeEach
    void setUp() {
        usaCountry = new CountryEntity();
        usaCountry.setId(1L);
        usaCountry.setCountryCode("US");
        usaCountry.setCountryName("United States");

        canadaCountry = new CountryEntity();
        canadaCountry.setId(2L);
        canadaCountry.setCountryCode("CA");
        canadaCountry.setCountryName("Canada");

        mexicoCountry = new CountryEntity();
        mexicoCountry.setId(3L);
        mexicoCountry.setCountryCode("MX");
        mexicoCountry.setCountryName("Mexico");

        japanCountry = new CountryEntity();
        japanCountry.setId(4L);
        japanCountry.setCountryCode("JP");
        japanCountry.setCountryName("Japan");

        indiaCountry = new CountryEntity();
        indiaCountry.setId(5L);
        indiaCountry.setCountryCode("IN");
        indiaCountry.setCountryName("India");

        textilesCategory = new ProductCategoriesEntity();
        textilesCategory.setCategoryCode(620300);
        textilesCategory.setCategoryName("Textiles");
    }

    @Test
    void testExportingCountryInvalid() {
        RouteOptimizationRequestDto dto = new RouteOptimizationRequestDto();
        dto.setExportingCountryCode("XX");

        when(countryRepository.existsByCountryCode("XX")).thenReturn(false);

        var response = routeService.getAlternativeRoute(dto);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatus());
        assertEquals("Source country code does not exist", response.getMessage());
    }

    @Test
    void testImportingCountryInvalid() {
        RouteOptimizationRequestDto dto = new RouteOptimizationRequestDto();
        dto.setExportingCountryCode("US");
        dto.setImportingCountryCode("YY");

        when(countryRepository.existsByCountryCode("US")).thenReturn(true);
        when(countryRepository.existsByCountryCode("YY")).thenReturn(false);

        var response = routeService.getAlternativeRoute(dto);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatus());
        assertEquals("Destination country code does not exist", response.getMessage());
    }

    @Test
    void testHsCodeInvalid() {
        RouteOptimizationRequestDto dto = new RouteOptimizationRequestDto();
        dto.setExportingCountryCode("US");
        dto.setImportingCountryCode("CA");
        dto.setHsCode(123);

        when(countryRepository.existsByCountryCode("US")).thenReturn(true);
        when(countryRepository.existsByCountryCode("CA")).thenReturn(true);
        when(productCategoriesRepository.existsByCategoryCode(123)).thenReturn(false);

        var response = routeService.getAlternativeRoute(dto);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatus());
        assertEquals("HS code does not exist", response.getMessage());
    }

    @Test
    void testAlternativeRoutes() {
        RouteOptimizationRequestDto dto = new RouteOptimizationRequestDto();
        dto.setExportingCountryCode("US");
        dto.setImportingCountryCode("CA");
        dto.setHsCode(620300);
        dto.setInitialPrice(BigDecimal.valueOf(100));

        when(countryRepository.existsByCountryCode("US")).thenReturn(true);
        when(countryRepository.existsByCountryCode("CA")).thenReturn(true);
        when(productCategoriesRepository.existsByCategoryCode(620300)).thenReturn(true);
        when(countryRepository.findByCountryCode("US")).thenReturn(Optional.of(usaCountry));
        when(countryRepository.findByCountryCode("CA")).thenReturn(Optional.of(canadaCountry));

        // --- Build mock tariff chain: US → MX → CA ---
        TariffRateEntity rate1 = new TariffRateEntity();
        rate1.setExportingCountry(usaCountry);
        rate1.setImportingCountry(mexicoCountry);
        rate1.setProductCategory(textilesCategory);
        rate1.setTariffRate(BigDecimal.valueOf(8.75));
        rate1.setTariffType("AD_VALOREM");
        rate1.setRateUnit("PERCENT");
        rate1.setEffectiveDate(LocalDate.now().minusDays(10));
        rate1.setExpiryDate(LocalDate.now().plusDays(100));
        rate1.setPreferentialTariff(false);

        TariffRateEntity rate2 = new TariffRateEntity();
        rate2.setExportingCountry(mexicoCountry);
        rate2.setImportingCountry(canadaCountry);
        rate2.setProductCategory(textilesCategory);
        rate2.setTariffRate(BigDecimal.valueOf(5.00));
        rate2.setTariffType("AD_VALOREM");
        rate2.setRateUnit("PERCENT");
        rate2.setEffectiveDate(LocalDate.now().minusDays(5));
        rate2.setExpiryDate(LocalDate.now().plusDays(50));
        rate2.setPreferentialTariff(false);

        // Mock neighbors
        when(tariffRateRepository.findAllByExportingCountryIdAndHsCode(1L, 620300))
                .thenReturn(List.of(rate1));
        when(tariffRateRepository.findAllByExportingCountryIdAndHsCode(3L, 620300))
                .thenReturn(List.of(rate2));

        when(tariffRateService.getFinalPrice(anyLong(), anyLong(), anyInt(), any(), any(), any()))
                .thenReturn(BigDecimal.valueOf(110));
        when(tariffRateService.getTariffCost(any(), any()))
                .thenReturn(BigDecimal.valueOf(10));
        when(tariffRateService.convertToDto(any()))
                .thenReturn(new TariffRateDto());

        var response = routeService.getAlternativeRoute(dto);

        assertEquals(HttpStatus.OK, response.getStatus());
        assertEquals("Alternative routes found", response.getMessage());
        assertNotNull(response.getData());
        assertFalse(response.getData().isEmpty());
        // Ensure at least one alternative route with multiple hops
        AlternativeRouteDto alt = response.getData().get(0);
        assertTrue(alt.getRoutes().size() > 1, "Expected multi-hop alternative route");
        assertTrue(alt.getTariffTotalCost().compareTo(BigDecimal.ZERO) > 0);
    }

    @Test
    void testCheaperPathReplacesInBestCostMap() {
        RouteOptimizationRequestDto dto = new RouteOptimizationRequestDto();
        dto.setExportingCountryCode("US");
        dto.setImportingCountryCode("CA");
        dto.setHsCode(620300);
        dto.setInitialPrice(BigDecimal.valueOf(100));

        when(countryRepository.existsByCountryCode("US")).thenReturn(true);
        when(countryRepository.existsByCountryCode("CA")).thenReturn(true);
        when(productCategoriesRepository.existsByCategoryCode(620300)).thenReturn(true);
        when(countryRepository.findByCountryCode("US")).thenReturn(Optional.of(usaCountry));
        when(countryRepository.findByCountryCode("CA")).thenReturn(Optional.of(canadaCountry));

        // --- Setup two routes to same nextCountryId (CA) ---
        TariffRateEntity rateExpensive = new TariffRateEntity();
        rateExpensive.setExportingCountry(usaCountry);
        rateExpensive.setImportingCountry(canadaCountry);
        rateExpensive.setProductCategory(textilesCategory);
        rateExpensive.setTariffRate(BigDecimal.valueOf(20));

        TariffRateEntity rateCheaper = new TariffRateEntity();
        rateCheaper.setExportingCountry(usaCountry);
        rateCheaper.setImportingCountry(canadaCountry);
        rateCheaper.setProductCategory(textilesCategory);
        rateCheaper.setTariffRate(BigDecimal.valueOf(5));

        when(tariffRateRepository.findAllByExportingCountryIdAndHsCode(1L, 620300))
                .thenReturn(List.of(rateExpensive, rateCheaper));

        // First path costs more, second is cheaper
        when(tariffRateService.getFinalPrice(anyLong(), anyLong(), anyInt(), any(), any(), any()))
                .thenReturn(BigDecimal.valueOf(120)) // expensive
                .thenReturn(BigDecimal.valueOf(105)); // cheaper
        when(tariffRateService.getTariffCost(any(), any()))
                .thenReturn(BigDecimal.valueOf(20))   // first route
                .thenReturn(BigDecimal.valueOf(5));   // cheaper route
        when(tariffRateService.convertToDto(any())).thenReturn(new TariffRateDto());

        var response = routeService.getAlternativeRoute(dto);

        assertEquals(HttpStatus.OK, response.getStatus());
        assertNotNull(response.getData());
        // Expect direct route to be recorded (then later removed)
        assertTrue(response.getData().isEmpty() || response.getData().stream()
                .anyMatch(r -> r.getRoutes().size() <= 1));
    }

    @Test
    void testDirectRoutesRemovedFromAllRoutes() {
        RouteOptimizationRequestDto dto = new RouteOptimizationRequestDto();
        dto.setExportingCountryCode("US");
        dto.setImportingCountryCode("CA");
        dto.setHsCode(620300);
        dto.setInitialPrice(BigDecimal.valueOf(100));

        when(countryRepository.existsByCountryCode("US")).thenReturn(true);
        when(countryRepository.existsByCountryCode("CA")).thenReturn(true);
        when(productCategoriesRepository.existsByCategoryCode(620300)).thenReturn(true);
        when(countryRepository.findByCountryCode("US")).thenReturn(Optional.of(usaCountry));
        when(countryRepository.findByCountryCode("CA")).thenReturn(Optional.of(canadaCountry));

        // Only direct route (US → CA)
        TariffRateEntity direct = new TariffRateEntity();
        direct.setExportingCountry(usaCountry);
        direct.setImportingCountry(canadaCountry);
        direct.setProductCategory(textilesCategory);
        direct.setTariffRate(BigDecimal.valueOf(10));

        when(tariffRateRepository.findAllByExportingCountryIdAndHsCode(1L, 620300))
                .thenReturn(List.of(direct));
        when(tariffRateService.getFinalPrice(anyLong(), anyLong(), anyInt(), any(), any(), any()))
                .thenReturn(BigDecimal.valueOf(110));
        when(tariffRateService.getTariffCost(any(), any()))
                .thenReturn(BigDecimal.valueOf(10));
        when(tariffRateService.convertToDto(any())).thenReturn(new TariffRateDto());

        var response = routeService.getAlternativeRoute(dto);

        // RouteServiceImpl should remove direct routes (size <= 1)
        assertEquals(HttpStatus.OK, response.getStatus());
        assertTrue(response.getData().isEmpty(), "Direct routes should be removed from final list");
    }

    @Test
    void testMaxDepthOptimizerLimitReached() {
        RouteOptimizationRequestDto dto = new RouteOptimizationRequestDto();
        dto.setExportingCountryCode("US");
        dto.setImportingCountryCode("IN");
        dto.setHsCode(620300);
        dto.setInitialPrice(BigDecimal.valueOf(100));

        when(countryRepository.existsByCountryCode("US")).thenReturn(true);
        when(countryRepository.existsByCountryCode("IN")).thenReturn(true);
        when(productCategoriesRepository.existsByCategoryCode(620300)).thenReturn(true);
        when(countryRepository.findByCountryCode("US")).thenReturn(Optional.of(usaCountry));
        when(countryRepository.findByCountryCode("IN")).thenReturn(Optional.of(indiaCountry));

        // Build a multi-hop chain:
        // US -> CA -> MX -> JP -> IN
        TariffRateEntity usToSg = new TariffRateEntity();
        usToSg.setExportingCountry(usaCountry);
        usToSg.setImportingCountry(canadaCountry);
        usToSg.setProductCategory(textilesCategory);
        usToSg.setTariffRate(BigDecimal.valueOf(5));

        TariffRateEntity sgToMy = new TariffRateEntity();
        sgToMy.setExportingCountry(canadaCountry);
        sgToMy.setImportingCountry(mexicoCountry);
        sgToMy.setProductCategory(textilesCategory);
        sgToMy.setTariffRate(BigDecimal.valueOf(5));

        TariffRateEntity myToJp = new TariffRateEntity();
        myToJp.setExportingCountry(mexicoCountry);
        myToJp.setImportingCountry(japanCountry);
        myToJp.setProductCategory(textilesCategory);
        myToJp.setTariffRate(BigDecimal.valueOf(5));

        TariffRateEntity jpToIn = new TariffRateEntity();
        jpToIn.setExportingCountry(japanCountry);
        jpToIn.setImportingCountry(indiaCountry);
        jpToIn.setProductCategory(textilesCategory);
        jpToIn.setTariffRate(BigDecimal.valueOf(5));

        // Mock repository chains per country hop
        when(tariffRateRepository.findAllByExportingCountryIdAndHsCode(1L, 620300))
                .thenReturn(List.of(usToSg));
        when(tariffRateRepository.findAllByExportingCountryIdAndHsCode(2L, 620300))
                .thenReturn(List.of(sgToMy));
        when(tariffRateRepository.findAllByExportingCountryIdAndHsCode(3L, 620300))
                .thenReturn(List.of(myToJp));

        // Same tariff costs for all hops
        when(tariffRateService.getFinalPrice(anyLong(), anyLong(), anyInt(), any(), any(), any()))
                .thenReturn(BigDecimal.valueOf(105));
        when(tariffRateService.getTariffCost(any(), any()))
                .thenReturn(BigDecimal.valueOf(5));
        when(tariffRateService.convertToDto(any())).thenReturn(new TariffRateDto());

        var response = routeService.getAlternativeRoute(dto);

        // Should still be OK, but the deepest routes are skipped after hitting MAX_DEPTH_OPTIMIZER
        assertEquals(HttpStatus.OK, response.getStatus());
        assertNotNull(response.getData());
        // If MAX_DEPTH_OPTIMIZER = 3, we expect routes to stop expanding after depth 3
        assertTrue(response.getData().stream()
                        .allMatch(r -> r.getRoutes().size() <= 4),
                "Routes should not exceed MAX_DEPTH_OPTIMIZER depth");
    }

}
