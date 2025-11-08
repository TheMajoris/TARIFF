package com.cs203.core.service.impl;

import com.cs203.core.dto.CreateTariffRateDto;
import com.cs203.core.dto.GenericResponse;
import com.cs203.core.dto.ProductCategoriesDto;
import com.cs203.core.dto.TariffRateDto;
import com.cs203.core.entity.CountryEntity;
import com.cs203.core.entity.ProductCategoriesEntity;
import com.cs203.core.entity.TariffRateEntity;
import com.cs203.core.repository.CountryRepository;
import com.cs203.core.repository.ProductCategoriesRepository;
import com.cs203.core.repository.TariffRateRepository;
import com.cs203.core.service.TariffRateService;
import com.cs203.core.strategy.TariffCalculationStrategy;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TariffRateServiceTest {
    @Mock
    private TariffRateRepository tariffRateRepository;

    @Mock
    private TariffCalculationStrategy adValoremStrategy;

    @Mock
    private TariffCalculationStrategy specificStrategy;

    @Mock
    private CountryRepository countryRepository;

    @Mock
    private ProductCategoriesRepository productCategoriesRepository;

    @InjectMocks
    private TariffRateServiceImpl tariffRateService;

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
        CountryEntity importing = country(1L);
        CountryEntity exporting = country(2L);
        ProductCategoriesEntity cat = category(123);

        TariffRateEntity high = new TariffRateEntity(
                importing, exporting, cat, new BigDecimal("20.00"), "ad-valorem", "percent",
                LocalDate.now(), null, false);
        TariffRateEntity low = new TariffRateEntity(
                importing, exporting, cat, new BigDecimal("10.00"), "ad-valorem", "percent",
                LocalDate.now(), null, false);

        List<TariffRateEntity> rates = Arrays.asList(low, high);
        LocalDate date = LocalDate.of(2025, 1, 1);
        when(tariffRateRepository.findCurrentTariffRate(eq(1L), eq(2L), eq(123), eq(date))).thenReturn(Optional.of(low));
        when(adValoremStrategy.calculateFinalPrice(any(), any(), any()))
                .thenReturn(new BigDecimal("110.00"));

        BigDecimal result = tariffRateService.getFinalPrice(1L, 2L, 123, new BigDecimal("100.00"), new BigDecimal("1"), date);

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

        LocalDate date = LocalDate.of(2025, 1, 1);
        when(repository.findCurrentTariffRate(eq(1L), eq(2L), eq(123), eq(date)))
                .thenReturn(java.util.Optional.empty());

        BigDecimal result = service.getFinalPrice(1L, 2L, 123, new BigDecimal("100.00"), new BigDecimal("1"), date);

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
                importing, exporting, cat, new BigDecimal("20.00"), "ad-valorem", "percent",
                LocalDate.now(), null, false);
        TariffRateEntity low = new TariffRateEntity(
                importing, exporting, cat, new BigDecimal("10.00"), "ad-valorem", "percent",
                LocalDate.now(), null, false);

        List<TariffRateEntity> rates = Arrays.asList(low, high);
        LocalDate date = LocalDate.of(2025, 1, 1);
        when(repository.findCurrentTariffRate(eq(1L), eq(2L), eq(123), eq(date))).thenReturn(rates.stream().findFirst());

        BigDecimal result = service.getLowestActiveTariffRate(1L, 2L, 123, new BigDecimal("100.00"), date);

        assertEquals(0, result.compareTo(new BigDecimal("10.00")));
    }

    @Test
    @DisplayName("getLowestTariffRate with no rates returns -1")
    void getLowestTariffRate_noRates_returnsMinusOne() {
        TariffRateRepository repository = Mockito.mock(TariffRateRepository.class);
        TariffRateService service = new TariffRateServiceImpl();

        try {
            java.lang.reflect.Field f = TariffRateServiceImpl.class.getDeclaredField("tariffRateRepository");
            f.setAccessible(true);
            f.set(service, repository);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        LocalDate date = LocalDate.of(2025, 1, 1);
        when(repository.findCurrentTariffRate(eq(1L), eq(2L), eq(123), eq(date)))
                .thenReturn(java.util.Optional.empty());

        BigDecimal result = service.getLowestActiveTariffRate(1L, 2L, 123, new BigDecimal("100.00"), date);

        assertEquals(0, result.compareTo(new BigDecimal("-1")));
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

    @Test
    @DisplayName("getTariffRateById returns NOT_FOUND when ID does not exist")
    void getTariffRateById_notFound() {
        when(tariffRateRepository.findById(1L)).thenReturn(Optional.empty());

        GenericResponse<TariffRateDto> response = tariffRateService.getTariffRateById(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatus());
        assertNull(response.getData());
    }

    @Test
    @DisplayName("createTariffRate returns NOT_FOUND if importing country does not exist")
    void createTariffRate_importingCountryNotFound() {
        CreateTariffRateDto dto = new CreateTariffRateDto();
        dto.setExportingCountryCode("EXP");
        dto.setImportingCountryCode("IMP");
        dto.setHsCode(123);

        when(countryRepository.existsByCountryCode("EXP")).thenReturn(true);
        when(countryRepository.existsByCountryCode("IMP")).thenReturn(false);

        GenericResponse<TariffRateDto> response = tariffRateService.createTariffRate(dto);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatus());
        assertEquals("Importing Country code does not exist", response.getMessage());
    }

    @Test
    @DisplayName("createTariffRate returns NOT_FOUND if HS code does not exist")
    void createTariffRate_hsCodeNotFound() {
        CreateTariffRateDto dto = new CreateTariffRateDto();
        dto.setExportingCountryCode("EXP");
        dto.setImportingCountryCode("IMP");
        dto.setHsCode(123);

        when(countryRepository.existsByCountryCode("EXP")).thenReturn(true);
        when(countryRepository.existsByCountryCode("IMP")).thenReturn(true);
        when(productCategoriesRepository.existsByCategoryCode(123)).thenReturn(false);

        GenericResponse<TariffRateDto> response = tariffRateService.createTariffRate(dto);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatus());
        assertEquals("HS code does not exist", response.getMessage());
    }

    @Test
    @DisplayName("updateTariffRate returns NOT_FOUND if tariff rate does not exist")
    void updateTariffRate_notFound() {
        TariffRateDto dto = new TariffRateDto();
        dto.setId(1L);

        when(tariffRateRepository.findById(1L)).thenReturn(Optional.empty());

        GenericResponse<TariffRateDto> response = tariffRateService.updateTariffRate(dto, 1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatus());
    }

    @Test
    @DisplayName("updateTariffRate returns NOT_FOUND if product category not found")
    void updateTariffRate_productCategoryNotFound() {
        TariffRateDto dto = new TariffRateDto();
        dto.setId(1L);
        dto.setImportingCountryCode("IMP");
        dto.setExportingCountryCode("EXP");
        dto.setProductCategory(new ProductCategoriesDto());
        dto.getProductCategory().setCategoryCode(123);

        TariffRateEntity existing = new TariffRateEntity();
        when(tariffRateRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(countryRepository.findByCountryCode("IMP")).thenReturn(Optional.of(country(1L)));
        when(countryRepository.findByCountryCode("EXP")).thenReturn(Optional.of(country(2L)));
        when(productCategoriesRepository.findByCategoryCode(123)).thenReturn(Optional.empty());

        GenericResponse<TariffRateDto> response = tariffRateService.updateTariffRate(dto, 1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatus());
    }

    @Test
    @DisplayName("getFinalPrice uses SPECIFIC strategy when tariff type is SPECIFIC")
    void getFinalPrice_specificStrategy() {
        CountryEntity importing = country(1L);
        CountryEntity exporting = country(2L);
        ProductCategoriesEntity cat = category(123);

        TariffRateEntity tariff = new TariffRateEntity(
                importing, exporting, cat, new BigDecimal("20.00"), "SPECIFIC", "unit",
                LocalDate.now(), null, false);

        LocalDate date = LocalDate.of(2025, 1, 1);
        when(tariffRateRepository.findCurrentTariffRate(eq(1L), eq(2L), eq(123), eq(date)))
                .thenReturn(Optional.of(tariff));
        when(specificStrategy.calculateFinalPrice(any(), any(), any()))
                .thenReturn(new BigDecimal("120.00"));

        BigDecimal result = tariffRateService.getFinalPrice(1L, 2L, 123, new BigDecimal("100.00"), new BigDecimal("1"), date);

        assertEquals(0, result.compareTo(new BigDecimal("120.00")));
    }

    @Test
    @DisplayName("getAllTariffRates returns list of DTOs")
    void getAllTariffRates_returnsDtos() {
        CountryEntity importing = country(1L);
        CountryEntity exporting = country(2L);
        ProductCategoriesEntity cat = category(123);

        TariffRateEntity entity = new TariffRateEntity(
                importing, exporting, cat, new BigDecimal("10.00"), "ad-valorem", "percent",
                LocalDate.now(), null, false);

        when(tariffRateRepository.findAll()).thenReturn(List.of(entity));

        List<TariffRateDto> result = tariffRateService.getAllTariffRates();

        assertEquals(1, result.size());
        assertEquals(entity.getTariffRate(), result.get(0).getTariffRate());
        assertEquals(entity.getTariffType(), result.get(0).getTariffType());
    }

    @Test
    @DisplayName("getTariffRates with pageable returns paged DTOs")
    void getTariffRates_withPageable_returnsPagedDtos() {
        CountryEntity importing = country(1L);
        CountryEntity exporting = country(2L);
        ProductCategoriesEntity cat = category(123);

        TariffRateEntity entity = new TariffRateEntity(
                importing, exporting, cat, new BigDecimal("10.00"), "ad-valorem", "percent",
                LocalDate.now(), null, false);

        Page<TariffRateEntity> page = new PageImpl<>(List.of(entity));
        Pageable pageable = PageRequest.of(0, 10);

        when(tariffRateRepository.findAll(pageable)).thenReturn(page);

        Page<TariffRateDto> result = tariffRateService.getTariffRates(pageable);

        assertEquals(1, result.getContent().size());
        assertEquals(entity.getTariffRate(), result.getContent().get(0).getTariffRate());
        assertEquals(entity.getTariffType(), result.getContent().get(0).getTariffType());
    }

    @Test
    @DisplayName("getTariffRateById returns OK when tariff rate exists")
    void getTariffRateById_found() {
        // Arrange
        CountryEntity importing = country(1L);
        CountryEntity exporting = country(2L);
        ProductCategoriesEntity cat = category(123);

        TariffRateEntity entity = new TariffRateEntity(
                importing, exporting, cat, new BigDecimal("15.00"), "ad-valorem", "percent",
                LocalDate.now(), null, false);
        entity.setId(1L);

        when(tariffRateRepository.findById(1L)).thenReturn(Optional.of(entity));

        // Act
        GenericResponse<TariffRateDto> response = tariffRateService.getTariffRateById(1L);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatus());
        assertEquals("Found", response.getMessage());
        assertNotNull(response.getData());
        assertEquals(entity.getId(), response.getData().getId());
        assertEquals(entity.getTariffRate(), response.getData().getTariffRate());
        assertEquals(entity.getTariffType(), response.getData().getTariffType());
        assertEquals(entity.getImportingCountry().getCountryCode(), response.getData().getImportingCountryCode());
        assertEquals(entity.getExportingCountry().getCountryCode(), response.getData().getExportingCountryCode());
        assertEquals(entity.getProductCategory().getCategoryCode(), response.getData().getProductCategory().getCategoryCode());
    }

    @Test
    @DisplayName("createTariffRate returns NOT_FOUND if exporting country does not exist")
    void createTariffRate_exportingCountryNotFound() {
        CreateTariffRateDto dto = new CreateTariffRateDto();
        dto.setExportingCountryCode("EXP");
        dto.setImportingCountryCode("IMP");
        dto.setHsCode(123);

        when(countryRepository.existsByCountryCode("EXP")).thenReturn(false);

        GenericResponse<TariffRateDto> response = tariffRateService.createTariffRate(dto);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatus());
        assertEquals("Exporting Country code does not exist", response.getMessage());
        assertNull(response.getData());
    }

    @Test
    @DisplayName("createTariffRate creates tariff rate successfully")
    void createTariffRate_success() {
        // Arrange
        CreateTariffRateDto dto = new CreateTariffRateDto();
        dto.setExportingCountryCode("EXP");
        dto.setImportingCountryCode("IMP");
        dto.setHsCode(123);
        dto.setTariffRate(new BigDecimal("10.00"));
        dto.setTariffType("ad-valorem");
        dto.setUnitQuantity(new BigDecimal("1"));
        dto.setRateUnit("percent");
        dto.setEffectiveDate(LocalDate.now());
        dto.setPreferentialTariff(true);

        CountryEntity importing = country(1L);
        CountryEntity exporting = country(2L);
        ProductCategoriesEntity cat = category(123);

        when(countryRepository.existsByCountryCode("EXP")).thenReturn(true);
        when(countryRepository.existsByCountryCode("IMP")).thenReturn(true);
        when(productCategoriesRepository.existsByCategoryCode(123)).thenReturn(true);

        when(countryRepository.findByCountryCode("EXP")).thenReturn(Optional.of(exporting));
        when(countryRepository.findByCountryCode("IMP")).thenReturn(Optional.of(importing));
        when(productCategoriesRepository.findByCategoryCode(123)).thenReturn(Optional.of(cat));

        TariffRateEntity savedEntity = new TariffRateEntity();
        savedEntity.setId(1L);
        savedEntity.setTariffRate(dto.getTariffRate());
        savedEntity.setTariffType(dto.getTariffType());
        savedEntity.setImportingCountry(importing);
        savedEntity.setExportingCountry(exporting);
        savedEntity.setProductCategory(cat);

        when(tariffRateRepository.save(any(TariffRateEntity.class))).thenReturn(savedEntity);


        // Act
        GenericResponse<TariffRateDto> response = tariffRateService.createTariffRate(dto);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatus());
        assertEquals("Successfully created Tariff Rate", response.getMessage());
        assertNotNull(response.getData());
        assertEquals(savedEntity.getId(), response.getData().getId());
        assertEquals(savedEntity.getTariffRate(), response.getData().getTariffRate());
        assertEquals(savedEntity.getTariffType(), response.getData().getTariffType());
        assertEquals(importing.getCountryCode(), response.getData().getImportingCountryCode());
        assertEquals(exporting.getCountryCode(), response.getData().getExportingCountryCode());
        assertEquals(cat.getCategoryCode(), response.getData().getProductCategory().getCategoryCode());

    }

    @Test
    @DisplayName("updateTariffRate returns NOT_FOUND if importing country not found")
    void updateTariffRate_importingCountryNotFound() {
        TariffRateDto dto = new TariffRateDto();
        dto.setId(1L);
        dto.setImportingCountryCode("IMP");
        dto.setExportingCountryCode("EXP");
        dto.setProductCategory(new ProductCategoriesDto());
        dto.getProductCategory().setCategoryCode(123);

        TariffRateEntity existing = new TariffRateEntity();
        when(tariffRateRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(countryRepository.findByCountryCode("IMP")).thenReturn(Optional.empty());

        // Expect the exception to be thrown
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            tariffRateService.updateTariffRate(dto, 1L);
        });

        assertEquals("Country not found with code: IMP", exception.getMessage());

        verify(tariffRateRepository).findById(1L);
        verify(countryRepository).findByCountryCode("IMP");
    }

    @Test
    @DisplayName("updateTariffRate returns NOT_FOUND if exporting country not found")
    void updateTariffRate_exportingCountryNotFound() {
        TariffRateDto dto = new TariffRateDto();
        dto.setId(1L);
        dto.setImportingCountryCode("IMP");
        dto.setExportingCountryCode("EXP");
        dto.setProductCategory(new ProductCategoriesDto());
        dto.getProductCategory().setCategoryCode(123);

        TariffRateEntity existing = new TariffRateEntity();
        CountryEntity importingCountry = country(1L);

        when(tariffRateRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(countryRepository.findByCountryCode("IMP")).thenReturn(Optional.of(importingCountry));
        when(countryRepository.findByCountryCode("EXP")).thenReturn(Optional.empty());

        // Expect the exception to be thrown
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            tariffRateService.updateTariffRate(dto, 1L);
        });

        assertEquals("Country not found with code: EXP", exception.getMessage());

        verify(tariffRateRepository).findById(1L);
        verify(countryRepository).findByCountryCode("IMP");
        verify(countryRepository).findByCountryCode("EXP");
    }

    @Test
    @DisplayName("updateTariffRate successfully updates all fields")
    void updateTariffRate_success() {
        // Arrange
        Long tariffRateId = 1L;
        LocalDate effectiveDate = LocalDate.of(2024, 1, 1);
        LocalDate expiryDate = LocalDate.of(2025, 1, 1);
        LocalDateTime createdAt = LocalDateTime.of(2023, 1, 1, 0,0);

        TariffRateDto dto = new TariffRateDto();
        dto.setId(tariffRateId);
        dto.setTariffRate(new BigDecimal("15.5"));
        dto.setTariffType("AD_VALOREM");
        dto.setUnitQuantity(new BigDecimal("100.0"));
        dto.setRateUnit("KG");
        dto.setEffectiveDate(effectiveDate);
        dto.setExpiryDate(expiryDate);
        dto.setPreferentialTariff(true);
        dto.setImportingCountryCode("IMP");
        dto.setExportingCountryCode("EXP");

        ProductCategoriesDto categoryDto = new ProductCategoriesDto();
        categoryDto.setCategoryCode(123);
        dto.setProductCategory(categoryDto);

        // Existing entity
        TariffRateEntity existingEntity = new TariffRateEntity();
        existingEntity.setId(tariffRateId);
        existingEntity.setCreatedAt(createdAt);
        existingEntity.setTariffRate(new BigDecimal("10.0"));
        existingEntity.setTariffType("OLD_TYPE");

        // Mock countries
        CountryEntity importingCountry = country(1L);
        importingCountry.setCountryCode("IMP");
        CountryEntity exportingCountry = country(2L);
        exportingCountry.setCountryCode("EXP");

        // Mock product category
        ProductCategoriesEntity productCategory = new ProductCategoriesEntity();
        productCategory.setCategoryCode(123);
        productCategory.setCategoryName("Test Category");

        // Mock updated entity (after save)
        TariffRateEntity updatedEntity = new TariffRateEntity();
        updatedEntity.setId(tariffRateId);
        updatedEntity.setTariffRate(new BigDecimal("15.5"));
        updatedEntity.setTariffType("AD_VALOREM");
        updatedEntity.setUnitQuantity(new BigDecimal("100.0"));
        updatedEntity.setRateUnit("KG");
        updatedEntity.setEffectiveDate(effectiveDate);
        updatedEntity.setExpiryDate(expiryDate);
        updatedEntity.setPreferentialTariff(true);
        updatedEntity.setImportingCountry(importingCountry);
        updatedEntity.setExportingCountry(exportingCountry);
        updatedEntity.setProductCategory(productCategory);
        updatedEntity.setCreatedAt(createdAt);
        updatedEntity.setUpdatedAt(LocalDateTime.now());

        // Setup mocks
        when(tariffRateRepository.findById(tariffRateId)).thenReturn(Optional.of(existingEntity));
        when(countryRepository.findByCountryCode("IMP")).thenReturn(Optional.of(importingCountry));
        when(countryRepository.findByCountryCode("EXP")).thenReturn(Optional.of(exportingCountry));
        when(productCategoriesRepository.findByCategoryCode(123)).thenReturn(Optional.of(productCategory));
        when(tariffRateRepository.save(any(TariffRateEntity.class))).thenReturn(updatedEntity);

        // Act
        GenericResponse<TariffRateDto> response = tariffRateService.updateTariffRate(dto, tariffRateId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatus());
        assertEquals("Successfully updated Tariff Rate", response.getMessage());
        assertNotNull(response.getData());

        // Verify all repository interactions
        verify(tariffRateRepository).findById(tariffRateId);
        verify(countryRepository).findByCountryCode("IMP");
        verify(countryRepository).findByCountryCode("EXP");
        verify(productCategoriesRepository).findByCategoryCode(123);

        // Verify save was called and capture the entity
        ArgumentCaptor<TariffRateEntity> entityCaptor = ArgumentCaptor.forClass(TariffRateEntity.class);
        verify(tariffRateRepository).save(entityCaptor.capture());

        TariffRateEntity savedEntity = entityCaptor.getValue();

        // Verify all fields were set correctly
        assertEquals(new BigDecimal("15.5"), savedEntity.getTariffRate());
        assertEquals("AD_VALOREM", savedEntity.getTariffType());
        assertEquals(new BigDecimal("100.0"), savedEntity.getUnitQuantity());
        assertEquals("KG", savedEntity.getRateUnit());
        assertEquals(effectiveDate, savedEntity.getEffectiveDate());
        assertEquals(expiryDate, savedEntity.getExpiryDate());
        assertEquals(true, savedEntity.getPreferentialTariff());
        assertEquals(importingCountry, savedEntity.getImportingCountry());
        assertEquals(exportingCountry, savedEntity.getExportingCountry());
        assertEquals(productCategory, savedEntity.getProductCategory());

        // Verify createdAt was NOT changed
        assertEquals(createdAt, savedEntity.getCreatedAt());

        // Verify updatedAt was set (should be recent)
        assertNotNull(savedEntity.getUpdatedAt());
        assertTrue(savedEntity.getUpdatedAt().isAfter(createdAt));
    }

    @Test
    @DisplayName("deleteTariffRate successfully deletes tariff rate")
    void deleteTariffRate_success() {
        // Arrange
        Long tariffRateId = 1L;
        when(tariffRateRepository.existsById(tariffRateId)).thenReturn(true);
        doNothing().when(tariffRateRepository).deleteById(tariffRateId);

        // Act
        GenericResponse<Void> response = tariffRateService.deleteTariffRate(tariffRateId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatus());
        assertEquals("Successfully deleted Tariff Rate", response.getMessage());
        assertNull(response.getData());

        // Verify deleteById was called
        verify(tariffRateRepository).existsById(tariffRateId);
        verify(tariffRateRepository).deleteById(tariffRateId);
    }

    @Test
    @DisplayName("deleteTariffRate returns NOT_FOUND when tariff rate does not exist")
    void deleteTariffRate_notFound() {
        // Arrange
        Long tariffRateId = 999L;
        when(tariffRateRepository.existsById(tariffRateId)).thenReturn(false);

        // Act
        GenericResponse<Void> response = tariffRateService.deleteTariffRate(tariffRateId);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatus());
        assertEquals("Tariff Rate not found with id: " + tariffRateId, response.getMessage());
        assertNull(response.getData());

        // Verify deleteById was NOT called
        verify(tariffRateRepository).existsById(tariffRateId);
        verify(tariffRateRepository, never()).deleteById(anyLong());
    }

    @Test
    @DisplayName("getLowestActiveTariff returns tariff rate when found")
    void getLowestActiveTariff_found() {
        // Arrange
        Long importingCountryId = 1L;
        Long exportingCountryId = 2L;
        Integer hsCode = 123456;
        BigDecimal initialPrice = new BigDecimal("100.00");
        LocalDate date = LocalDate.of(2024, 11, 6);

        TariffRateEntity tariffRate = new TariffRateEntity();
        tariffRate.setId(1L);
        tariffRate.setTariffRate(new BigDecimal("15.5"));
        tariffRate.setTariffType("AD_VALOREM");

        when(tariffRateRepository.findCurrentTariffRate(importingCountryId, exportingCountryId, hsCode, date))
                .thenReturn(Optional.of(tariffRate));

        // Act
        Optional<TariffRateEntity> result = tariffRateService.getLowestActiveTariff(
                importingCountryId, exportingCountryId, hsCode, initialPrice, date);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(tariffRate, result.get());
        assertEquals(1L, result.get().getId());
        assertEquals(new BigDecimal("15.5"), result.get().getTariffRate());

        // Verify repository method was called with correct parameters
        verify(tariffRateRepository).findCurrentTariffRate(importingCountryId, exportingCountryId, hsCode, date);
    }

    @Test
    @DisplayName("getLowestActiveTariff returns empty when no tariff rate found")
    void getLowestActiveTariff_notFound() {
        // Arrange
        Long importingCountryId = 1L;
        Long exportingCountryId = 2L;
        Integer hsCode = 123456;
        BigDecimal initialPrice = new BigDecimal("100.00");
        LocalDate date = LocalDate.of(2024, 11, 6);

        when(tariffRateRepository.findCurrentTariffRate(importingCountryId, exportingCountryId, hsCode, date))
                .thenReturn(Optional.empty());

        // Act
        Optional<TariffRateEntity> result = tariffRateService.getLowestActiveTariff(
                importingCountryId, exportingCountryId, hsCode, initialPrice, date);

        // Assert
        assertFalse(result.isPresent());
        assertTrue(result.isEmpty());

        // Verify repository method was called
        verify(tariffRateRepository).findCurrentTariffRate(importingCountryId, exportingCountryId, hsCode, date);
    }

    @Test
    @DisplayName("getLowestActiveTariff handles null initialPrice")
    void getLowestActiveTariff_nullInitialPrice() {
        // Arrange
        Long importingCountryId = 1L;
        Long exportingCountryId = 2L;
        Integer hsCode = 123456;
        BigDecimal initialPrice = null; // Testing with null
        LocalDate date = LocalDate.of(2024, 11, 6);

        TariffRateEntity tariffRate = new TariffRateEntity();
        tariffRate.setId(1L);

        when(tariffRateRepository.findCurrentTariffRate(importingCountryId, exportingCountryId, hsCode, date))
                .thenReturn(Optional.of(tariffRate));

        // Act
        Optional<TariffRateEntity> result = tariffRateService.getLowestActiveTariff(
                importingCountryId, exportingCountryId, hsCode, initialPrice, date);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(tariffRate, result.get());

        // Verify repository method was called (note: initialPrice is not used in the query)
        verify(tariffRateRepository).findCurrentTariffRate(importingCountryId, exportingCountryId, hsCode, date);
    }
}