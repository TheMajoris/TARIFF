package com.cs203.core.service.impl;

import com.cs203.core.dto.CountryDto;
import com.cs203.core.entity.CountryEntity;
import com.cs203.core.repository.CountryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CountryServiceImplTest {

    @Mock
    private CountryRepository countryRepository;

    @InjectMocks
    private CountryServiceImpl countryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllCountries_withCountries() {
        // Arrange
        CountryEntity country1 = new CountryEntity();
        country1.setId(1L);
        country1.setCountryName("Singapore");
        country1.setCountryCode("SG");
        country1.setRegion("Asia");
        country1.setCurrencyCode("SGD");

        CountryEntity country2 = new CountryEntity();
        country2.setId(2L);
        country2.setCountryName("Japan");
        country2.setCountryCode("JP");
        country2.setRegion("Asia");
        country2.setCurrencyCode("JPY");

        when(countryRepository.findAllByOrderByCountryNameAsc())
                .thenReturn(Arrays.asList(country1, country2));

        // Act
        List<CountryDto> result = countryService.getAllCountries();

        // Assert
        assertEquals(2, result.size());
        CountryDto dto1 = result.get(0);
        assertNotNull(dto1);
        assertEquals("Singapore", dto1.getCountryName());
        assertEquals("SG", dto1.getCountryCode());
        assertEquals("Asia", dto1.getRegion());
        assertEquals("SGD", dto1.getCurrencyCode());
        assertNull(dto1.getCurrencyName()); // set explicitly null

        verify(countryRepository, times(1)).findAllByOrderByCountryNameAsc();
    }

    @Test
    void testGetAllCountries_emptyList() {
        // Arrange
        when(countryRepository.findAllByOrderByCountryNameAsc())
                .thenReturn(Collections.emptyList());

        // Act
        List<CountryDto> result = countryService.getAllCountries();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(countryRepository, times(1)).findAllByOrderByCountryNameAsc();
    }
}
