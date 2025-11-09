package com.cs203.core.service.impl;

import com.cs203.core.dto.CreateNationalTariffLinesDto;
import com.cs203.core.dto.GenericResponse;
import com.cs203.core.dto.NationalTariffLinesDto;
import com.cs203.core.dto.ProductCategoriesDto;
import com.cs203.core.entity.CountryEntity;
import com.cs203.core.entity.NationalTariffLinesEntity;
import com.cs203.core.entity.ProductCategoriesEntity;
import com.cs203.core.entity.UserEntity;
import com.cs203.core.repository.CountryRepository;
import com.cs203.core.repository.NationalTariffLinesRepository;
import com.cs203.core.repository.ProductCategoriesRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NationalTariffLinesServiceImplTest {

    @Mock
    private NationalTariffLinesRepository nationalTariffLinesRepository;

    @Mock
    private CountryRepository countryRepository;

    @Mock
    private ProductCategoriesRepository productCategoriesRepository;

    @InjectMocks
    private NationalTariffLinesServiceImpl service;

    private CountryEntity countryEntity;
    private ProductCategoriesEntity productCategoriesEntity;
    private NationalTariffLinesEntity nationalTariffLinesEntity;
    private UserEntity userEntity;

    @BeforeEach
    void setUp() {
        // Setup Country Entity
        countryEntity = new CountryEntity();
        countryEntity.setCountryCode("US");
        countryEntity.setCountryName("United States");

        // Setup User Entity
        userEntity = new UserEntity();
        userEntity.setId(1L);

        // Setup Product Categories Entity
        productCategoriesEntity = new ProductCategoriesEntity();
        productCategoriesEntity.setId(1L);
        productCategoriesEntity.setCategoryCode(101010);
        productCategoriesEntity.setCategoryName("Telephone sets");
        productCategoriesEntity.setDescription("Telephone equipment");
        productCategoriesEntity.setIsActive(true);

        // Setup National Tariff Lines Entity
        nationalTariffLinesEntity = new NationalTariffLinesEntity();
        nationalTariffLinesEntity.setId(1L);
        nationalTariffLinesEntity.setCountry(countryEntity);
        nationalTariffLinesEntity.setTariffLineCode("8517.12.00");
        nationalTariffLinesEntity.setDescription("Telephones for cellular networks");
        nationalTariffLinesEntity.setParentHsCode(productCategoriesEntity);
        nationalTariffLinesEntity.setLevel(3);
        nationalTariffLinesEntity.setCreatedBy(userEntity);
        nationalTariffLinesEntity.setUpdatedBy(userEntity);
    }

    @Test
    void createNationalTariffLines_Success() {
        // Arrange
        CreateNationalTariffLinesDto createDto = new CreateNationalTariffLinesDto();
        createDto.setCountryCode("US");
        createDto.setTariffLineCode("8517.12.00");
        createDto.setDescription("Telephones for cellular networks");
        createDto.setParentHsCode(101010);
        createDto.setLevel(3);

        when(countryRepository.findByCountryCode("US")).thenReturn(Optional.of(countryEntity));
        when(productCategoriesRepository.findByCategoryCode(101010)).thenReturn(Optional.of(productCategoriesEntity));
        when(nationalTariffLinesRepository.save(any(NationalTariffLinesEntity.class)))
                .thenReturn(nationalTariffLinesEntity);

        // Act
        NationalTariffLinesDto result = service.createNationalTariffLines(createDto);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("US", result.getCountryCode());
        assertEquals("8517.12.00", result.getTariffLineCode());
        assertEquals("Telephones for cellular networks", result.getDescription());
        assertEquals(3, result.getLevel());
        verify(nationalTariffLinesRepository, times(1)).save(any(NationalTariffLinesEntity.class));
    }

    @Test
    void createNationalTariffLines_WithNullParentHsCode_Success() {
        // Arrange
        CreateNationalTariffLinesDto createDto = new CreateNationalTariffLinesDto();
        createDto.setCountryCode("US");
        createDto.setTariffLineCode("8517.12.00");
        createDto.setDescription("Telephones for cellular networks");
        createDto.setParentHsCode(null);
        createDto.setLevel(1);

        NationalTariffLinesEntity entityWithoutParent = new NationalTariffLinesEntity();
        entityWithoutParent.setId(2L);
        entityWithoutParent.setCountry(countryEntity);
        entityWithoutParent.setTariffLineCode("8517.12.00");
        entityWithoutParent.setDescription("Telephones for cellular networks");
        entityWithoutParent.setParentHsCode(null);
        entityWithoutParent.setLevel(1);
        entityWithoutParent.setCreatedBy(userEntity);
        entityWithoutParent.setUpdatedBy(userEntity);

        when(countryRepository.findByCountryCode("US")).thenReturn(Optional.of(countryEntity));
        when(nationalTariffLinesRepository.save(any(NationalTariffLinesEntity.class)))
                .thenReturn(entityWithoutParent);

        // Act
        NationalTariffLinesDto result = service.createNationalTariffLines(createDto);

        // Assert
        assertNotNull(result);
        assertNull(result.getParentHsCode());
        verify(productCategoriesRepository, never()).findByCategoryCode(anyInt());
    }

    @Test
    void createNationalTariffLines_CountryNotFound_ThrowsException() {
        // Arrange
        CreateNationalTariffLinesDto createDto = new CreateNationalTariffLinesDto();
        createDto.setCountryCode("XX");
        createDto.setTariffLineCode("8517.12.00");

        when(countryRepository.findByCountryCode("XX")).thenReturn(Optional.empty());

        // Act & Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            service.createNationalTariffLines(createDto);
        });

        assertEquals("Country not found with code: XX", exception.getMessage());
        verify(nationalTariffLinesRepository, never()).save(any());
    }

    @Test
    void createNationalTariffLines_ProductCategoryNotFound_ThrowsException() {
        // Arrange
        CreateNationalTariffLinesDto createDto = new CreateNationalTariffLinesDto();
        createDto.setCountryCode("US");
        createDto.setTariffLineCode("8517.12.00");
        createDto.setParentHsCode(9999);

        when(countryRepository.findByCountryCode("US")).thenReturn(Optional.of(countryEntity));
        when(productCategoriesRepository.findByCategoryCode(9999)).thenReturn(Optional.empty());

        // Act & Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            service.createNationalTariffLines(createDto);
        });

        assertEquals("Product Category not found with code: 9999", exception.getMessage());
        verify(nationalTariffLinesRepository, never()).save(any());
    }

    @Test
    void updateNationalTariffLines_Success() {
        // Arrange
        Long tariffLineId = 1L;
        NationalTariffLinesDto updateDto = new NationalTariffLinesDto();
        updateDto.setCountryCode("US");
        updateDto.setTariffLineCode("8517.13.00");
        updateDto.setDescription("Updated description");
        updateDto.setLevel(4);

        ProductCategoriesDto parentDto = new ProductCategoriesDto();
        parentDto.setCategoryCode(101010);
        updateDto.setParentHsCode(parentDto);

        when(nationalTariffLinesRepository.findById(tariffLineId)).thenReturn(Optional.of(nationalTariffLinesEntity));
        when(countryRepository.findByCountryCode("US")).thenReturn(Optional.of(countryEntity));
        when(productCategoriesRepository.findByCategoryCode(101010)).thenReturn(Optional.of(productCategoriesEntity));
        when(nationalTariffLinesRepository.save(any(NationalTariffLinesEntity.class)))
                .thenReturn(nationalTariffLinesEntity);

        // Act
        GenericResponse<NationalTariffLinesDto> response = service.updateNationalTariffLines(updateDto, tariffLineId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatus());
        assertEquals("Successfully updated National Tariff Lines", response.getMessage());
        assertNotNull(response.getData());
        verify(nationalTariffLinesRepository, times(1)).save(any(NationalTariffLinesEntity.class));
    }

    @Test
    void updateNationalTariffLines_TariffLineNotFound_ReturnsNotFound() {
        // Arrange
        Long tariffLineId = 999L;
        NationalTariffLinesDto updateDto = new NationalTariffLinesDto();

        when(nationalTariffLinesRepository.findById(tariffLineId)).thenReturn(Optional.empty());

        // Act
        GenericResponse<NationalTariffLinesDto> response = service.updateNationalTariffLines(updateDto, tariffLineId);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatus());
        assertEquals("National Tariff Line not found with id: 999", response.getMessage());
        assertNull(response.getData());
        verify(nationalTariffLinesRepository, never()).save(any());
    }

    @Test
    void updateNationalTariffLines_CountryNotFound_ReturnsNotFound() {
        // Arrange
        Long tariffLineId = 1L;
        NationalTariffLinesDto updateDto = new NationalTariffLinesDto();
        updateDto.setCountryCode("XX");

        when(nationalTariffLinesRepository.findById(tariffLineId)).thenReturn(Optional.of(nationalTariffLinesEntity));
        when(countryRepository.findByCountryCode("XX")).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> service.updateNationalTariffLines(updateDto, tariffLineId)
        );

        assertEquals("Country not found with code: XX", exception.getMessage());
        verify(nationalTariffLinesRepository, never()).save(any());
    }

    @Test
    void updateNationalTariffLines_ParentHsCodeNotFound_ReturnsNotFound() {
        // Arrange
        Long tariffLineId = 1L;
        NationalTariffLinesDto updateDto = new NationalTariffLinesDto();
        updateDto.setCountryCode("US");

        ProductCategoriesDto parentDto = new ProductCategoriesDto();
        parentDto.setCategoryCode(101010);
        updateDto.setParentHsCode(parentDto);

        when(nationalTariffLinesRepository.findById(tariffLineId)).thenReturn(Optional.of(nationalTariffLinesEntity));
        when(countryRepository.findByCountryCode("US")).thenReturn(Optional.of(countryEntity));
        when(productCategoriesRepository.findByCategoryCode(101010)).thenReturn(null);

        // Act
        GenericResponse<NationalTariffLinesDto> response = service.updateNationalTariffLines(updateDto, tariffLineId);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatus());
        assertEquals("Parent HS Code not found with code: 101010", response.getMessage());
        assertNull(response.getData());
        verify(nationalTariffLinesRepository, never()).save(any());
    }

    @Test
    void deleteNationalTariffLines_Success() {
        // Arrange
        Long tariffLineId = 1L;

        when(nationalTariffLinesRepository.existsById(tariffLineId)).thenReturn(true);
        doNothing().when(nationalTariffLinesRepository).deleteById(tariffLineId);

        // Act
        GenericResponse<Void> response = service.deleteNationalTariffLines(tariffLineId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatus());
        assertEquals("Successfully deleted National Tariff Line", response.getMessage());
        assertNull(response.getData());
        verify(nationalTariffLinesRepository, times(1)).deleteById(tariffLineId);
    }

    @Test
    void deleteNationalTariffLines_NotFound_ReturnsNotFound() {
        // Arrange
        Long tariffLineId = 999L;

        when(nationalTariffLinesRepository.existsById(tariffLineId)).thenReturn(false);

        // Act
        GenericResponse<Void> response = service.deleteNationalTariffLines(tariffLineId);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatus());
        assertEquals("National Tariff Line not found with id: 999", response.getMessage());
        assertNull(response.getData());
        verify(nationalTariffLinesRepository, never()).deleteById(anyLong());
    }

    @Test
    void convertToDto_WithParentHsCode_Success() {
        // This is tested implicitly through createNationalTariffLines_Success
        // but we can verify the conversion logic explicitly

        // Arrange - already set up in @BeforeEach
        when(nationalTariffLinesRepository.save(any(NationalTariffLinesEntity.class)))
                .thenReturn(nationalTariffLinesEntity);
        when(countryRepository.findByCountryCode("US")).thenReturn(Optional.of(countryEntity));
        when(productCategoriesRepository.findByCategoryCode(101010)).thenReturn(Optional.of(productCategoriesEntity));

        CreateNationalTariffLinesDto createDto = new CreateNationalTariffLinesDto();
        createDto.setCountryCode("US");
        createDto.setTariffLineCode("8517.12.00");
        createDto.setDescription("Test");
        createDto.setParentHsCode(101010);
        createDto.setLevel(3);

        // Act
        NationalTariffLinesDto result = service.createNationalTariffLines(createDto);

        // Assert
        assertNotNull(result.getParentHsCode());
        assertEquals(101010, result.getParentHsCode().getCategoryCode());
        assertEquals("Telephone sets", result.getParentHsCode().getCategoryName());
    }

    @Test
    void convertToDto_WithoutParentHsCode_Success() {
        // Arrange
        NationalTariffLinesEntity entityWithoutParent = new NationalTariffLinesEntity();
        entityWithoutParent.setId(2L);
        entityWithoutParent.setCountry(countryEntity);
        entityWithoutParent.setTariffLineCode("8517.00.00");
        entityWithoutParent.setDescription("Test without parent");
        entityWithoutParent.setParentHsCode(null);
        entityWithoutParent.setLevel(1);
        entityWithoutParent.setCreatedBy(userEntity);
        entityWithoutParent.setUpdatedBy(userEntity);

        when(countryRepository.findByCountryCode("US")).thenReturn(Optional.of(countryEntity));
        when(nationalTariffLinesRepository.save(any(NationalTariffLinesEntity.class)))
                .thenReturn(entityWithoutParent);

        CreateNationalTariffLinesDto createDto = new CreateNationalTariffLinesDto();
        createDto.setCountryCode("US");
        createDto.setTariffLineCode("8517.00.00");
        createDto.setDescription("Test without parent");
        createDto.setParentHsCode(null);
        createDto.setLevel(1);

        // Act
        NationalTariffLinesDto result = service.createNationalTariffLines(createDto);

        // Assert
        assertNull(result.getParentHsCode());
    }
}