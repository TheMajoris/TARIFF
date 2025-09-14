package com.cs203.core.repository;

import com.cs203.core.entity.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.TestPropertySource;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest    
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(properties = {
    "spring.datasource.url=jdbc:postgresql://localhost:5432/tariff_db",
    "spring.datasource.username=admin",
    "spring.datasource.password=admin123",
    "spring.jpa.hibernate.ddl-auto=create-drop",
    "spring.jpa.show-sql=true"
})

public class CountryRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CountryRepository countryRepository;

    private CountryEntity usCountry;
    private CountryEntity sgCountry;
    private CountryEntity ukCountry;
    private CountryEntity caCountry;

    @BeforeEach
    void setUp() {
        // Create test data
        usCountry = new CountryEntity("US", "United States", "North America", "USD");
        sgCountry = new CountryEntity("SG", "Singapore", "Asia", "SGD");
        ukCountry = new CountryEntity("UK", "United Kingdom", "Europe", "GBP");
        caCountry = new CountryEntity("CA", "Canada", "North America", "CAD");

        // Persist test data
        entityManager.persistAndFlush(usCountry);
        entityManager.persistAndFlush(sgCountry);
        entityManager.persistAndFlush(ukCountry);
        entityManager.persistAndFlush(caCountry);
    }

    @Test
    void testFindByCountryCode_Found() {
        Optional<CountryEntity> result = countryRepository.findByCountryCode("US");
        
        assertTrue(result.isPresent());
        assertEquals("US", result.get().getCountryCode());
        assertEquals("United States", result.get().getCountryName());
    }

    @Test
    void testFindByCountryCode_NotFound() {
        Optional<CountryEntity> result = countryRepository.findByCountryCode("XX");
        
        assertFalse(result.isPresent());
    }

    @Test
    void testFindByCountryName_Found() {
        Optional<CountryEntity> result = countryRepository.findByCountryName("Singapore");
        
        assertTrue(result.isPresent());
        assertEquals("SG", result.get().getCountryCode());
        assertEquals("Singapore", result.get().getCountryName());
    }

    @Test
    void testFindByCountryName_NotFound() {
        Optional<CountryEntity> result = countryRepository.findByCountryName("Unknown Country");
        
        assertFalse(result.isPresent());
    }

    @Test
    void testFindByRegion() {
        List<CountryEntity> northAmericanCountries = countryRepository.findByRegion("North America");
        
        assertEquals(2, northAmericanCountries.size());
        assertThat(northAmericanCountries)
            .extracting(CountryEntity::getCountryCode)
            .containsExactlyInAnyOrder("US", "CA");
    }

    @Test
    void testFindByRegion_EmptyResult() {
        List<CountryEntity> result = countryRepository.findByRegion("Antarctica");
        
        assertTrue(result.isEmpty());
    }

    @Test
    void testFindByCurrencyCode() {
        List<CountryEntity> usdCountries = countryRepository.findByCurrencyCode("USD");
        
        assertEquals(1, usdCountries.size());
        assertEquals("US", usdCountries.get(0).getCountryCode());
    }

    @Test
    void testExistsByCountryCode() {
        assertTrue(countryRepository.existsByCountryCode("US"));
        assertFalse(countryRepository.existsByCountryCode("XX"));
    }

    @Test
    void testExistsByCountryName() {
        assertTrue(countryRepository.existsByCountryName("Singapore"));
        assertFalse(countryRepository.existsByCountryName("Unknown Country"));
    }

    @Test
    void testFindAllRegions() {
        List<String> regions = countryRepository.findAllRegions();
        
        assertEquals(3, regions.size());
        assertThat(regions).containsExactly("Asia", "Europe", "North America");
    }

    @Test
    void testFindAllCurrencyCodes() {
        List<String> currencyCodes = countryRepository.findAllCurrencyCodes();
        
        assertEquals(4, currencyCodes.size());
        assertThat(currencyCodes).containsExactly("CAD", "GBP", "SGD", "USD");
    }

    @Test
    void testFindByCurrencyCodeIn() {
        List<String> currencies = Arrays.asList("USD", "SGD", "EUR");
        List<CountryEntity> countries = countryRepository.findByCurrencyCodeIn(currencies);
        
        assertEquals(2, countries.size());
        assertThat(countries)
            .extracting(CountryEntity::getCurrencyCode)
            .containsExactlyInAnyOrder("USD", "SGD");
    }

    @Test
    void testSearchCountries_ByCountryCode() {
        List<CountryEntity> results = countryRepository.searchCountries("us");
        
        assertEquals(1, results.size());
        assertEquals("US", results.get(0).getCountryCode());
    }

    @Test
    void testSearchCountries_ByCountryName() {
        List<CountryEntity> results = countryRepository.searchCountries("singapore");
        
        assertEquals(1, results.size());
        assertEquals("SG", results.get(0).getCountryCode());
    }

    @Test
    void testSearchCountries_ByRegion() {
        List<CountryEntity> results = countryRepository.searchCountries("asia");
        
        assertEquals(1, results.size());
        assertEquals("SG", results.get(0).getCountryCode());
    }

    @Test
    void testSearchCountries_ByCurrencyCode() {
        List<CountryEntity> results = countryRepository.searchCountries("usd");
        
        assertEquals(1, results.size());
        assertEquals("US", results.get(0).getCountryCode());
    }

    @Test
    void testSearchCountries_NoMatch() {
        List<CountryEntity> results = countryRepository.searchCountries("xyz123");
        
        assertTrue(results.isEmpty());
    }

    @Test
    void testCountByRegion() {
        long count = countryRepository.countByRegion("North America");
        
        assertEquals(2, count);
    }

    @Test
    void testCountByRegion_ZeroResults() {
        long count = countryRepository.countByRegion("Antarctica");
        
        assertEquals(0, count);
    }

    @Test
    void testCountByCurrencyCode() {
        long count = countryRepository.countByCurrencyCode("USD");
        
        assertEquals(1, count);
    }

    @Test
    void testFindAllByOrderByCountryNameAsc() {
        List<CountryEntity> countries = countryRepository.findAllByOrderByCountryNameAsc();
        
        assertEquals(4, countries.size());
        assertThat(countries)
            .extracting(CountryEntity::getCountryName)
            .containsExactly("Canada", "Singapore", "United Kingdom", "United States");
    }

    @Test
    void testFindAllByOrderByRegionAscCountryNameAsc() {
        List<CountryEntity> countries = countryRepository.findAllByOrderByRegionAscCountryNameAsc();
        
        assertEquals(4, countries.size());
        
        // Should be ordered by region first, then by country name within region
        assertThat(countries.get(0).getRegion()).isEqualTo("Asia");
        assertThat(countries.get(0).getCountryName()).isEqualTo("Singapore");
        
        assertThat(countries.get(1).getRegion()).isEqualTo("Europe");
        assertThat(countries.get(1).getCountryName()).isEqualTo("United Kingdom");
        
        assertThat(countries.get(2).getRegion()).isEqualTo("North America");
        assertThat(countries.get(2).getCountryName()).isEqualTo("Canada");
        
        assertThat(countries.get(3).getRegion()).isEqualTo("North America");
        assertThat(countries.get(3).getCountryName()).isEqualTo("United States");
    }

    @Test
    void testSaveCountry() {
        CountryEntity newCountry = new CountryEntity("FR", "France", "Europe", "EUR");
        
        CountryEntity saved = countryRepository.save(newCountry);
        
        assertNotNull(saved.getId());
        assertEquals("FR", saved.getCountryCode());
        assertEquals("France", saved.getCountryName());
        assertEquals("Europe", saved.getRegion());
        assertEquals("EUR", saved.getCurrencyCode());
        
        // Verify it exists in database
        assertTrue(countryRepository.existsByCountryCode("FR"));
    }

    @Test
    void testDeleteCountry() {
        Long countryId = usCountry.getId();
        
        countryRepository.deleteById(countryId);
        
        assertFalse(countryRepository.existsByCountryCode("US"));
        Optional<CountryEntity> deleted = countryRepository.findById(countryId);
        assertFalse(deleted.isPresent());
    }

    @Test
    void testUpdateCountry() {
        usCountry.setRegion("Americas");
        CountryEntity updated = countryRepository.save(usCountry);
        
        assertEquals("Americas", updated.getRegion());
        
        // Verify the change persisted
        Optional<CountryEntity> found = countryRepository.findByCountryCode("US");
        assertTrue(found.isPresent());
        assertEquals("Americas", found.get().getRegion());
    }

    @Test
    void testFindAllRegions_WithNullValues() {
        // Add a country with null region
        CountryEntity countryWithNullRegion = new CountryEntity("XX", "Unknown", null, "XXX");
        entityManager.persistAndFlush(countryWithNullRegion);
        
        List<String> regions = countryRepository.findAllRegions();
        
        // Should not include null regions
        assertThat(regions).doesNotContain((String) null);
        assertEquals(3, regions.size()); // Only non-null regions
    }

    @Test
    void testFindAllCurrencyCodes_WithNullValues() {
        // Add a country with null currency
        CountryEntity countryWithNullCurrency = new CountryEntity("YY", "Test Country", "Test Region", null);
        entityManager.persistAndFlush(countryWithNullCurrency);
        
        List<String> currencies = countryRepository.findAllCurrencyCodes();
        
        // Should not include null currencies
        assertThat(currencies).doesNotContain((String) null);
        assertEquals(4, currencies.size()); // Only non-null currencies
    }
}
