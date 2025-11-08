package com.cs203.core.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import com.cs203.core.dto.GenericResponse;
import com.cs203.core.dto.SavedCalculationDto;
import com.cs203.core.dto.requests.SaveCalculationRequestDTO;
import com.cs203.core.entity.CountryEntity;
import com.cs203.core.entity.ProductCategoriesEntity;
import com.cs203.core.entity.SavedCalculationsEntity;
import com.cs203.core.entity.UserEntity;
import com.cs203.core.repository.CountryRepository;
import com.cs203.core.repository.ProductCategoriesRepository;
import com.cs203.core.repository.SavedCalculationsRepository;
import com.cs203.core.repository.UserRepository;

import jakarta.persistence.EntityNotFoundException;

@ExtendWith(MockitoExtension.class)
class CalculationHistoryImplTest {

    @Mock
    private SavedCalculationsRepository savedCalculationsRepository;

    @Mock
    private CountryRepository countryRepository;

    @Mock
    private ProductCategoriesRepository productCategoriesRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CalculationHistoryImpl calculationHistoryService;

    private SaveCalculationRequestDTO validRequestDto;
    private UserEntity testUser;
    private CountryEntity importingCountry;
    private CountryEntity exportingCountry;
    private ProductCategoriesEntity productCategory;
    private SavedCalculationsEntity savedEntity;

    @BeforeEach
    void setUp() {
        // Create valid request DTO
        validRequestDto = new SaveCalculationRequestDTO(
                "Test Calculation",
                new BigDecimal("1000.00"),
                new BigDecimal("1000.00"),
                "USD",
                new BigDecimal("0.10"),
                "ad-valorem",
                null,
                null,
                new BigDecimal("100.00"),
                new BigDecimal("1100.00"),
                "Test notes",
                "US",
                "CN",
                123);

        // Create test user
        testUser = new UserEntity();
        testUser.setId(1L);
        testUser.setFirstName("John");
        testUser.setLastName("Doe");
        testUser.setEmail("john.doe@example.com");

        // Create test countries
        importingCountry = new CountryEntity();
        importingCountry.setId(1L);
        importingCountry.setCountryCode("US");
        importingCountry.setCountryName("United States");

        exportingCountry = new CountryEntity();
        exportingCountry.setId(2L);
        exportingCountry.setCountryCode("CN");
        exportingCountry.setCountryName("China");

        // Create test product category
        productCategory = new ProductCategoriesEntity();
        productCategory.setId(1L);
        productCategory.setCategoryCode(123);
        productCategory.setCategoryName("Test Category");
        productCategory.setDescription("Test Description");

        // Create test saved entity
        savedEntity = new SavedCalculationsEntity();
        savedEntity.setId(1L);
        savedEntity.setCalculationName("Test Calculation");
        savedEntity.setProductValue(new BigDecimal("1000.00"));
        savedEntity.setProductQuantity(new BigDecimal("1000.00"));
        savedEntity.setCurrencyCode("USD");
        savedEntity.setTariffRate(new BigDecimal("0.10"));
        savedEntity.setTariffType("ad-valorem");
        savedEntity.setUnitQuantity(null);
        savedEntity.setRateUnit(null);
        savedEntity.setCalculatedTariffCost(new BigDecimal("100.00"));
        savedEntity.setTotalCost(new BigDecimal("1100.00"));
        savedEntity.setNotes("Test notes");
        savedEntity.setCreatedAt(LocalDateTime.now());
        savedEntity.setUpdatedAt(LocalDateTime.now());
        savedEntity.setUser(testUser);
        savedEntity.setImportingCountry(importingCountry);
        savedEntity.setExportingCountry(exportingCountry);
        savedEntity.setProductCategory(productCategory);
    }

    @Test
    @DisplayName("saveCalculation should save calculation successfully")
    void saveCalculation_shouldSaveCalculationSuccessfully() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(countryRepository.findByCountryCode("US")).thenReturn(Optional.of(importingCountry));
        when(countryRepository.findByCountryCode("CN")).thenReturn(Optional.of(exportingCountry));
        when(productCategoriesRepository.findByCategoryCode(123)).thenReturn(Optional.of(productCategory));
        when(savedCalculationsRepository.save(any(SavedCalculationsEntity.class))).thenReturn(savedEntity);

        // Act
        GenericResponse<SavedCalculationDto> response = calculationHistoryService.saveCalculation(validRequestDto, 1L);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatus());
        assertEquals("Calculation saved successfully", response.getMessage());
        assertNotNull(response.getData());

        SavedCalculationDto savedDto = response.getData();
        assertEquals(1L, savedDto.getId());
        assertEquals("Test Calculation", savedDto.getCalculationName());
        assertEquals(new BigDecimal("1000.00"), savedDto.getProductValue());
        assertEquals(new BigDecimal("1000.00"), savedDto.getProductQuantity());
        assertEquals("USD", savedDto.getCurrencyCode());
        assertEquals(new BigDecimal("0.10"), savedDto.getTariffRate());
        assertEquals("ad-valorem", savedDto.getTariffType());
        assertEquals(null, savedDto.getUnitQuantity());
        assertEquals(null, savedDto.getRateUnit());
        assertEquals(new BigDecimal("100.00"), savedDto.getCalculatedTariffCost());
        assertEquals(new BigDecimal("1100.00"), savedDto.getTotalCost());
        assertEquals("Test notes", savedDto.getNotes());
        assertEquals(1L, savedDto.getUserId());
        assertEquals("US", savedDto.getImportingCountryCode());
        assertEquals("CN", savedDto.getExportingCountryCode());
        assertEquals(123, savedDto.getProductCategoryCode());

        // Verify repository interactions
        verify(userRepository).findById(1L);
        verify(countryRepository).findByCountryCode("US");
        verify(countryRepository).findByCountryCode("CN");
        verify(productCategoriesRepository).findByCategoryCode(123);
        verify(savedCalculationsRepository).save(any(SavedCalculationsEntity.class));
    }

    @Test
    @DisplayName("saveCalculation should throw EntityNotFoundException when user not found")
    void saveCalculation_shouldThrowException_whenUserNotFound() {
        // Arrange
        when(productCategoriesRepository.findByCategoryCode(123)).thenReturn(Optional.of(productCategory));
        when(countryRepository.findByCountryCode("US")).thenReturn(Optional.of(importingCountry));
        when(countryRepository.findByCountryCode("CN")).thenReturn(Optional.of(exportingCountry));
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            calculationHistoryService.saveCalculation(validRequestDto, 1L);
        });

        assertEquals("User not found with id: 1", exception.getMessage());
        verify(productCategoriesRepository).findByCategoryCode(123);
        verify(countryRepository).findByCountryCode("US");
        verify(countryRepository).findByCountryCode("CN");
        verify(userRepository).findById(1L);
        verifyNoInteractions(savedCalculationsRepository);
    }

    @Test
    @DisplayName("saveCalculation should throw EntityNotFoundException when importing country not found")
    void saveCalculation_shouldThrowException_whenImportingCountryNotFound() {
        // Arrange
        when(productCategoriesRepository.findByCategoryCode(123)).thenReturn(Optional.of(productCategory));
        when(countryRepository.findByCountryCode("US")).thenReturn(Optional.empty());

        // Act & Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            calculationHistoryService.saveCalculation(validRequestDto, 1L);
        });

        assertEquals("Country not found with code: US", exception.getMessage());
        verify(productCategoriesRepository).findByCategoryCode(123);
        verify(countryRepository).findByCountryCode("US");
        verifyNoInteractions(userRepository, savedCalculationsRepository);
    }

    @Test
    @DisplayName("saveCalculation should throw EntityNotFoundException when exporting country not found")
    void saveCalculation_shouldThrowException_whenExportingCountryNotFound() {
        // Arrange
        when(productCategoriesRepository.findByCategoryCode(123)).thenReturn(Optional.of(productCategory));
        when(countryRepository.findByCountryCode("US")).thenReturn(Optional.of(importingCountry));
        when(countryRepository.findByCountryCode("CN")).thenReturn(Optional.empty());

        // Act & Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            calculationHistoryService.saveCalculation(validRequestDto, 1L);
        });

        assertEquals("Country not found with code: CN", exception.getMessage());
        verify(productCategoriesRepository).findByCategoryCode(123);
        verify(countryRepository).findByCountryCode("US");
        verify(countryRepository).findByCountryCode("CN");
        verifyNoInteractions(userRepository, savedCalculationsRepository);
    }

    @Test
    @DisplayName("saveCalculation should throw EntityNotFoundException when product category not found")
    void saveCalculation_shouldThrowException_whenProductCategoryNotFound() {
        // Arrange
        when(productCategoriesRepository.findByCategoryCode(123)).thenReturn(Optional.empty());

        // Act & Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            calculationHistoryService.saveCalculation(validRequestDto, 1L);
        });

        assertEquals("Product category not found with code: 123", exception.getMessage());
        verify(productCategoriesRepository).findByCategoryCode(123);
        verifyNoInteractions(countryRepository, userRepository, savedCalculationsRepository);
    }

    @Test
    @DisplayName("getCalculationsByUserId should return list of calculations")
    void getCalculationsByUserId_shouldReturnListOfCalculations() {
        // Arrange
        SavedCalculationsEntity entity1 = new SavedCalculationsEntity();
        entity1.setId(1L);
        entity1.setCalculationName("Calculation 1");
        entity1.setProductValue(new BigDecimal("1000.00"));
        entity1.setProductQuantity(new BigDecimal("1000.00"));
        entity1.setCurrencyCode("USD");
        entity1.setTariffRate(new BigDecimal("0.10"));
        entity1.setTariffType("ad-valorem");
        entity1.setUnitQuantity(null);
        entity1.setRateUnit(null);
        entity1.setCalculatedTariffCost(new BigDecimal("100.00"));
        entity1.setTotalCost(new BigDecimal("1100.00"));
        entity1.setNotes("Notes 1");
        entity1.setCreatedAt(LocalDateTime.now());
        entity1.setUpdatedAt(LocalDateTime.now());
        entity1.setUser(testUser);
        entity1.setImportingCountry(importingCountry);
        entity1.setExportingCountry(exportingCountry);
        entity1.setProductCategory(productCategory);

        SavedCalculationsEntity entity2 = new SavedCalculationsEntity();
        entity2.setId(2L);
        entity2.setCalculationName("Calculation 2");
        entity2.setProductValue(new BigDecimal("2000.00"));
        entity2.setProductQuantity(new BigDecimal("2000.00"));
        entity2.setCurrencyCode("EUR");
        entity2.setTariffRate(new BigDecimal("0.15"));
        entity2.setTariffType("specific");
        entity2.setUnitQuantity(new BigDecimal("1"));
        entity2.setRateUnit("kg");
        entity2.setCalculatedTariffCost(new BigDecimal("300.00"));
        entity2.setTotalCost(new BigDecimal("2300.00"));
        entity2.setNotes("Notes 2");
        entity2.setCreatedAt(LocalDateTime.now());
        entity2.setUpdatedAt(LocalDateTime.now());
        entity2.setUser(testUser);
        entity2.setImportingCountry(importingCountry);
        entity2.setExportingCountry(exportingCountry);
        entity2.setProductCategory(productCategory);

        List<SavedCalculationsEntity> entities = Arrays.asList(entity1, entity2);
        when(savedCalculationsRepository.findByUserId(1L)).thenReturn(entities);

        // Act
        GenericResponse<List<SavedCalculationDto>> response = calculationHistoryService.getCalculationsByUserId(1L);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatus());
        assertNotNull(response.getData());
        assertEquals(2, response.getData().size());

        SavedCalculationDto dto1 = response.getData().get(0);
        assertEquals(1L, dto1.getId());
        assertEquals("Calculation 1", dto1.getCalculationName());
        assertEquals(new BigDecimal("1000.00"), dto1.getProductValue());
        assertEquals(new BigDecimal("1000.00"), dto1.getProductQuantity());
        assertEquals("USD", dto1.getCurrencyCode());
        assertEquals(new BigDecimal("0.10"), dto1.getTariffRate());
        assertEquals("ad-valorem", dto1.getTariffType());
        assertEquals(null, dto1.getUnitQuantity());
        assertEquals(null, dto1.getRateUnit());
        assertEquals(new BigDecimal("100.00"), dto1.getCalculatedTariffCost());
        assertEquals(new BigDecimal("1100.00"), dto1.getTotalCost());
        assertEquals("Notes 1", dto1.getNotes());
        assertEquals(1L, dto1.getUserId());
        assertEquals("US", dto1.getImportingCountryCode());
        assertEquals("CN", dto1.getExportingCountryCode());
        assertEquals(123, dto1.getProductCategoryCode());

        SavedCalculationDto dto2 = response.getData().get(1);
        assertEquals(2L, dto2.getId());
        assertEquals("Calculation 2", dto2.getCalculationName());
        assertEquals(new BigDecimal("2000.00"), dto2.getProductValue());
        assertEquals(new BigDecimal("2000.00"), dto2.getProductQuantity());
        assertEquals("EUR", dto2.getCurrencyCode());
        assertEquals(new BigDecimal("0.15"), dto2.getTariffRate());
        assertEquals("specific", dto2.getTariffType());
        assertEquals(new BigDecimal("1"), dto2.getUnitQuantity());
        assertEquals("kg", dto2.getRateUnit());
        assertEquals(new BigDecimal("300.00"), dto2.getCalculatedTariffCost());
        assertEquals(new BigDecimal("2300.00"), dto2.getTotalCost());
        assertEquals("Notes 2", dto2.getNotes());
        assertEquals(1L, dto2.getUserId());
        assertEquals("US", dto2.getImportingCountryCode());
        assertEquals("CN", dto2.getExportingCountryCode());
        assertEquals(123, dto2.getProductCategoryCode());

        verify(savedCalculationsRepository).findByUserId(1L);
    }

    @Test
    @DisplayName("getCalculationsByUserId should return empty list when no calculations exist")
    void getCalculationsByUserId_shouldReturnEmptyList_whenNoCalculationsExist() {
        // Arrange
        when(savedCalculationsRepository.findByUserId(1L)).thenReturn(Arrays.asList());

        // Act
        GenericResponse<List<SavedCalculationDto>> response = calculationHistoryService.getCalculationsByUserId(1L);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatus());
        assertNotNull(response.getData());
        assertEquals(0, response.getData().size());

        verify(savedCalculationsRepository).findByUserId(1L);
    }

    @Test
    @DisplayName("saveCalculation should handle null notes correctly")
    void saveCalculation_shouldHandleNullNotes() {
        // Arrange
        SaveCalculationRequestDTO requestWithNullNotes = new SaveCalculationRequestDTO(
                "Test Calculation",
                new BigDecimal("1000.00"),
                new BigDecimal("1000.00"),
                "USD",
                new BigDecimal("0.10"),
                "ad-valorem",
                null,
                null,
                new BigDecimal("100.00"),
                new BigDecimal("1100.00"),
                null, // null notes
                "US",
                "CN",
                123);

        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(countryRepository.findByCountryCode("US")).thenReturn(Optional.of(importingCountry));
        when(countryRepository.findByCountryCode("CN")).thenReturn(Optional.of(exportingCountry));
        when(productCategoriesRepository.findByCategoryCode(123)).thenReturn(Optional.of(productCategory));
        when(savedCalculationsRepository.save(any(SavedCalculationsEntity.class))).thenReturn(savedEntity);

        // Act
        GenericResponse<SavedCalculationDto> response = calculationHistoryService.saveCalculation(requestWithNullNotes,
                1L);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatus());
        assertNotNull(response.getData());

        verify(savedCalculationsRepository).save(any(SavedCalculationsEntity.class));
    }

    @Test
    @DisplayName("saveCalculation should set timestamps correctly")
    void saveCalculation_shouldSetTimestampsCorrectly() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(countryRepository.findByCountryCode("US")).thenReturn(Optional.of(importingCountry));
        when(countryRepository.findByCountryCode("CN")).thenReturn(Optional.of(exportingCountry));
        when(productCategoriesRepository.findByCategoryCode(123)).thenReturn(Optional.of(productCategory));
        when(savedCalculationsRepository.save(any(SavedCalculationsEntity.class))).thenReturn(savedEntity);

        // Act
        calculationHistoryService.saveCalculation(validRequestDto, 1L);

        // Assert
        verify(savedCalculationsRepository).save(argThat(entity -> {
            assertNotNull(entity.getCreatedAt());
            assertNotNull(entity.getUpdatedAt());
            return true;
        }));
    }

    @Test
    @DisplayName("saveCalculation should set all entity fields correctly")
    void saveCalculation_shouldSetAllEntityFieldsCorrectly() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(countryRepository.findByCountryCode("US")).thenReturn(Optional.of(importingCountry));
        when(countryRepository.findByCountryCode("CN")).thenReturn(Optional.of(exportingCountry));
        when(productCategoriesRepository.findByCategoryCode(123)).thenReturn(Optional.of(productCategory));
        when(savedCalculationsRepository.save(any(SavedCalculationsEntity.class))).thenReturn(savedEntity);

        // Act
        calculationHistoryService.saveCalculation(validRequestDto, 1L);

        // Assert
        verify(savedCalculationsRepository).save(argThat(entity -> {
            assertEquals("Test Calculation", entity.getCalculationName());
            assertEquals(new BigDecimal("1000.00"), entity.getProductValue());
            assertEquals(new BigDecimal("1000.00"), entity.getProductQuantity());
            assertEquals("USD", entity.getCurrencyCode());
            assertEquals(new BigDecimal("0.10"), entity.getTariffRate());
            assertEquals("ad-valorem", entity.getTariffType());
            assertEquals(null, entity.getUnitQuantity());
            assertEquals(null, entity.getRateUnit());
            assertEquals(new BigDecimal("100.00"), entity.getCalculatedTariffCost());
            assertEquals(new BigDecimal("1100.00"), entity.getTotalCost());
            assertEquals("Test notes", entity.getNotes());
            assertEquals(testUser, entity.getUser());
            assertEquals(importingCountry, entity.getImportingCountry());
            assertEquals(exportingCountry, entity.getExportingCountry());
            assertEquals(productCategory, entity.getProductCategory());
            return true;
        }));
    }

    @Test
    @DisplayName("getCalculationsByUserId should handle empty result gracefully")
    void getCalculationsByUserId_shouldHandleEmptyResultGracefully() {
        // Arrange
        when(savedCalculationsRepository.findByUserId(1L)).thenReturn(Arrays.asList());

        // Act
        GenericResponse<List<SavedCalculationDto>> response = calculationHistoryService.getCalculationsByUserId(1L);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatus());
        assertNotNull(response.getData());
        assertTrue(response.getData().isEmpty());
    }

    @Test
    @DisplayName("saveCalculation should handle BigDecimal precision correctly")
    void saveCalculation_shouldHandleBigDecimalPrecisionCorrectly() {
        // Arrange
        SaveCalculationRequestDTO requestWithPreciseValues = new SaveCalculationRequestDTO(
                "Precise Calculation",
                new BigDecimal("1234.5678"),
                new BigDecimal("1234.5678"),
                "USD",
                new BigDecimal("0.1234"),
                "ad-valorem",
                null,
                null,
                new BigDecimal("152.3456"),
                new BigDecimal("1386.9134"),
                "Precise notes",
                "US",
                "CN",
                123);

        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(countryRepository.findByCountryCode("US")).thenReturn(Optional.of(importingCountry));
        when(countryRepository.findByCountryCode("CN")).thenReturn(Optional.of(exportingCountry));
        when(productCategoriesRepository.findByCategoryCode(123)).thenReturn(Optional.of(productCategory));
        when(savedCalculationsRepository.save(any(SavedCalculationsEntity.class))).thenReturn(savedEntity);

        // Act
        GenericResponse<SavedCalculationDto> response = calculationHistoryService
                .saveCalculation(requestWithPreciseValues, 1L);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatus());

        verify(savedCalculationsRepository).save(argThat(entity -> {
            assertEquals(new BigDecimal("1234.5678"), entity.getProductValue());
            assertEquals(new BigDecimal("1234.5678"), entity.getProductQuantity());
            assertEquals(new BigDecimal("0.1234"), entity.getTariffRate());
            assertEquals(new BigDecimal("152.3456"), entity.getCalculatedTariffCost());
            assertEquals(new BigDecimal("1386.9134"), entity.getTotalCost());
            return true;
        }));
    }

    @Test
    @DisplayName("getCalculationsByUserId should only return calculations for specific user")
    void getCalculationsByUserId_shouldOnlyReturnUserSpecificCalculations() {
        // Arrange
        UserEntity otherUser = new UserEntity();
        otherUser.setId(2L);

        SavedCalculationsEntity userCalc = new SavedCalculationsEntity();
        userCalc.setId(1L);
        userCalc.setCalculationName("User 1 Calculation");
        userCalc.setProductValue(new BigDecimal("1000.00"));
        userCalc.setProductQuantity(new BigDecimal("1000.00"));
        userCalc.setCurrencyCode("USD");
        userCalc.setTariffRate(new BigDecimal("0.10"));
        userCalc.setTariffType("ad-valorem");
        userCalc.setUnitQuantity(null);
        userCalc.setRateUnit(null);
        userCalc.setCalculatedTariffCost(new BigDecimal("100.00"));
        userCalc.setTotalCost(new BigDecimal("1100.00"));
        userCalc.setNotes("Test notes");
        userCalc.setCreatedAt(LocalDateTime.now());
        userCalc.setUpdatedAt(LocalDateTime.now());
        userCalc.setUser(testUser); // userId = 1
        userCalc.setImportingCountry(importingCountry);
        userCalc.setExportingCountry(exportingCountry);
        userCalc.setProductCategory(productCategory);

        when(savedCalculationsRepository.findByUserId(1L))
                .thenReturn(Arrays.asList(userCalc));

        // Act
        GenericResponse<List<SavedCalculationDto>> response = calculationHistoryService.getCalculationsByUserId(1L);

        // Assert
        assertNotNull(response);
        assertNotNull(response.getData());
        assertEquals(1, response.getData().size());
        assertTrue(response.getData().stream()
                .allMatch(dto -> dto.getUserId().equals(1L)));

        // Verify repository was called with correct userId
        verify(savedCalculationsRepository).findByUserId(1L);
    }

}
