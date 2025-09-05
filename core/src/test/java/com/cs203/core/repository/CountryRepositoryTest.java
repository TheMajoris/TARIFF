package com.cs203.core.repository;

import com.cs203.core.entity.CountryEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.TestPropertySource;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@TestPropertySource(properties = {
    "spring.datasource.url=jdbc:h2:mem:testdb",
    "spring.jpa.hibernate.ddl-auto=create-drop",
    "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect"
})
@DisplayName("CountryRepository Tests")
class CountryRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CountryRepository repository;

    private CountryEntity singapore;
    private CountryEntity unitedStates;
    private CountryEntity malaysia;
    private CountryEntity canada;

    @BeforeEach
    void setUp() {
        // Create test countries
        singapore = new CountryEntity("SG", "Singapore", "Asia", "SGD");
        singapore = entityManager.persistAndFlush(singapore);

        unitedStates = new CountryEntity("US", "United States", "North America", "USD");
        unitedStates = entityManager.persistAndFlush(unitedStates);

        malaysia = new CountryEntity("MY", "Malaysia", "Asia", "MYR");
        malaysia = entityManager.persistAndFlush(malaysia);

        canada = new CountryEntity("CA", "Canada", "North America", "CAD");
        canada = entityManager.persistAndFlush(canada);

        entityManager.clear();
    }

    @Nested
    @DisplayName("Basic CRUD Operations")
    class BasicCrudTests {

        @Test
        @DisplayName("Should save and find country by ID")
        void shouldSaveAndFindCountryById() {
            Optional<CountryEntity> found = repository.findById(singapore.getId());
            
            assertTrue(found.isPresent());
            assertEquals("SG", found.get().getCountryCode());
            assertEquals("Singapore", found.get().getCountryName());
        }

        @Test
        @DisplayName("Should find all countries")
        void shouldFindAllCountries() {
            List<CountryEntity> countries = repository.findAll();
            
            assertTrue(countries.size() >= 4);
            assertTrue(countries.stream().anyMatch(c -> "SG".equals(c.getCountryCode())));
            assertTrue(countries.stream().anyMatch(c -> "US".equals(c.getCountryCode())));
        }

        @Test
        @DisplayName("Should delete country")
        void shouldDeleteCountry() {
            repository.delete(singapore);
            
            Optional<CountryEntity> found = repository.findByCountryCode("SG");
            assertFalse(found.isPresent());
        }

        @Test
        @DisplayName("Should update country")
        void shouldUpdateCountry() {
            singapore.setCountryName("Republic of Singapore");
            singapore.setRegion("Southeast Asia");
            
            CountryEntity updated = repository.save(singapore);
            
            assertEquals("Republic of Singapore", updated.getCountryName());
            assertEquals("Southeast Asia", updated.getRegion());
        }
    }

    @Nested
    @DisplayName("Find By Country Code")
    class FindByCountryCodeTests {

        @Test
        @DisplayName("Should find country by valid country code")
        void shouldFindCountryByValidCountryCode() {
            Optional<CountryEntity> found = repository.findByCountryCode("SG");
            
            assertTrue(found.isPresent());
            assertEquals("Singapore", found.get().getCountryName());
        }

        @Test
        @DisplayName("Should return empty for non-existent country code")
        void shouldReturnEmptyForNonExistentCountryCode() {
            Optional<CountryEntity> found = repository.findByCountryCode("XX");
            
            assertFalse(found.isPresent());
        }

        @Test
        @DisplayName("Should check if country code exists")
        void shouldCheckIfCountryCodeExists() {
            assertTrue(repository.existsByCountryCode("SG"));
            assertFalse(repository.existsByCountryCode("XX"));
        }

        @Test
        @DisplayName("Should be case sensitive for country code")
        void shouldBeCaseSensitiveForCountryCode() {
            assertTrue(repository.existsByCountryCode("SG"));
            assertFalse(repository.existsByCountryCode("sg"));
        }
    }

    @Nested
    @DisplayName("Find By Country Name")
    class FindByCountryNameTests {

        @Test
        @DisplayName("Should find country by exact name")
        void shouldFindCountryByExactName() {
            Optional<CountryEntity> found = repository.findByCountryName("Singapore");
            
            assertTrue(found.isPresent());
            assertEquals("SG", found.get().getCountryCode());
        }

        @Test
        @DisplayName("Should return empty for non-existent name")
        void shouldReturnEmptyForNonExistentName() {
            Optional<CountryEntity> found = repository.findByCountryName("Non-existent Country");
            
            assertFalse(found.isPresent());
        }

        @Test
        @DisplayName("Should check if country name exists")
        void shouldCheckIfCountryNameExists() {
            assertTrue(repository.existsByCountryName("Singapore"));
            assertFalse(repository.existsByCountryName("Non-existent Country"));
        }

        @Test
        @DisplayName("Should find countries by partial name (case insensitive)")
        void shouldFindCountriesByPartialNameCaseInsensitive() {
            List<CountryEntity> found = repository.findByCountryNameContainingIgnoreCase("SING");
            
            assertFalse(found.isEmpty());
            assertTrue(found.stream().anyMatch(c -> "SG".equals(c.getCountryCode())));
        }
    }

    @Nested
    @DisplayName("Find By Region")
    class FindByRegionTests {

        @Test
        @DisplayName("Should find countries by region")
        void shouldFindCountriesByRegion() {
            List<CountryEntity> asianCountries = repository.findByRegion("Asia");
            
            assertEquals(2, asianCountries.size());
            assertTrue(asianCountries.stream().anyMatch(c -> "SG".equals(c.getCountryCode())));
            assertTrue(asianCountries.stream().anyMatch(c -> "MY".equals(c.getCountryCode())));
        }

        @Test
        @DisplayName("Should find countries by region (case insensitive)")
        void shouldFindCountriesByRegionCaseInsensitive() {
            List<CountryEntity> asianCountries = repository.findByRegionIgnoreCase("ASIA");
            
            assertEquals(2, asianCountries.size());
            assertTrue(asianCountries.stream().anyMatch(c -> "SG".equals(c.getCountryCode())));
        }

        @Test
        @DisplayName("Should find countries by multiple regions")
        void shouldFindCountriesByMultipleRegions() {
            List<String> regions = Arrays.asList("Asia", "North America");
            List<CountryEntity> countries = repository.findByRegionIn(regions);
            
            assertEquals(4, countries.size());
        }

        @Test
        @DisplayName("Should count countries by region")
        void shouldCountCountriesByRegion() {
            long asianCount = repository.countByRegion("Asia");
            long northAmericanCount = repository.countByRegion("North America");
            
            assertEquals(2, asianCount);
            assertEquals(2, northAmericanCount);
        }

        @Test
        @DisplayName("Should return empty list for non-existent region")
        void shouldReturnEmptyListForNonExistentRegion() {
            List<CountryEntity> countries = repository.findByRegion("Antarctica");
            
            assertTrue(countries.isEmpty());
        }
    }

    @Nested
    @DisplayName("Find By Currency Code")
    class FindByCurrencyCodeTests {

        @Test
        @DisplayName("Should find countries by currency code")
        void shouldFindCountriesByCurrencyCode() {
            List<CountryEntity> usdCountries = repository.findByCurrencyCode("USD");
            
            assertEquals(1, usdCountries.size());
            assertEquals("US", usdCountries.get(0).getCountryCode());
        }

        @Test
        @DisplayName("Should find countries by multiple currency codes")
        void shouldFindCountriesByMultipleCurrencyCodes() {
            List<String> currencies = Arrays.asList("USD", "SGD");
            List<CountryEntity> countries = repository.findByCurrencyCodeIn(currencies);
            
            assertEquals(2, countries.size());
            assertTrue(countries.stream().anyMatch(c -> "US".equals(c.getCountryCode())));
            assertTrue(countries.stream().anyMatch(c -> "SG".equals(c.getCountryCode())));
        }

        @Test
        @DisplayName("Should count countries by currency code")
        void shouldCountCountriesByCurrencyCode() {
            long usdCount = repository.countByCurrencyCode("USD");
            long sgdCount = repository.countByCurrencyCode("SGD");
            
            assertEquals(1, usdCount);
            assertEquals(1, sgdCount);
        }

        @Test
        @DisplayName("Should handle non-existent currency code")
        void shouldHandleNonExistentCurrencyCode() {
            List<CountryEntity> countries = repository.findByCurrencyCode("XYZ");
            
            assertTrue(countries.isEmpty());
            assertEquals(0, repository.countByCurrencyCode("XYZ"));
        }
    }

    @Nested
    @DisplayName("Advanced Query Operations")
    class AdvancedQueryTests {

        @Test
        @DisplayName("Should find all unique regions")
        void shouldFindAllUniqueRegions() {
            List<String> regions = repository.findAllRegions();
            
            assertTrue(regions.size() >= 2);
            assertTrue(regions.contains("Asia"));
            assertTrue(regions.contains("North America"));
        }

        @Test
        @DisplayName("Should find all unique currency codes")
        void shouldFindAllUniqueCurrencyCodes() {
            List<String> currencies = repository.findAllCurrencyCodes();
            
            assertTrue(currencies.size() >= 4);
            assertTrue(currencies.contains("USD"));
            assertTrue(currencies.contains("SGD"));
            assertTrue(currencies.contains("MYR"));
            assertTrue(currencies.contains("CAD"));
        }

        @Test
        @DisplayName("Should search countries by any field")
        void shouldSearchCountriesByAnyField() {
            // Search by country code
            List<CountryEntity> codeResults = repository.searchCountries("SG");
            assertTrue(codeResults.stream().anyMatch(c -> "SG".equals(c.getCountryCode())));

            // Search by country name
            List<CountryEntity> nameResults = repository.searchCountries("Singapore");
            assertTrue(nameResults.stream().anyMatch(c -> "SG".equals(c.getCountryCode())));

            // Search by region
            List<CountryEntity> regionResults = repository.searchCountries("Asia");
            assertTrue(regionResults.size() >= 2);

            // Search by currency
            List<CountryEntity> currencyResults = repository.searchCountries("USD");
            assertTrue(currencyResults.stream().anyMatch(c -> "US".equals(c.getCountryCode())));
        }

        @Test
        @DisplayName("Should search countries case insensitively")
        void shouldSearchCountriesCaseInsensitively() {
            List<CountryEntity> results = repository.searchCountries("singapore");
            
            assertFalse(results.isEmpty());
            assertTrue(results.stream().anyMatch(c -> "SG".equals(c.getCountryCode())));
        }

        @Test
        @DisplayName("Should return empty list for non-matching search")
        void shouldReturnEmptyListForNonMatchingSearch() {
            List<CountryEntity> results = repository.searchCountries("NonExistentTerm");
            
            assertTrue(results.isEmpty());
        }
    }

    @Nested
    @DisplayName("Ordering and Sorting")
    class OrderingSortingTests {

        @Test
        @DisplayName("Should find all countries ordered by name")
        void shouldFindAllCountriesOrderedByName() {
            List<CountryEntity> countries = repository.findAllByOrderByCountryNameAsc();
            
            assertFalse(countries.isEmpty());
            // Verify ordering
            for (int i = 0; i < countries.size() - 1; i++) {
                assertTrue(countries.get(i).getCountryName().compareToIgnoreCase(
                    countries.get(i + 1).getCountryName()) <= 0);
            }
        }

        @Test
        @DisplayName("Should find all countries ordered by region then name")
        void shouldFindAllCountriesOrderedByRegionThenName() {
            List<CountryEntity> countries = repository.findAllByOrderByRegionAscCountryNameAsc();
            
            assertFalse(countries.isEmpty());
            // Verify primary ordering by region
            String previousRegion = null;
            String previousCountryName = null;
            
            for (CountryEntity country : countries) {
                if (previousRegion != null) {
                    int regionComparison = country.getRegion().compareToIgnoreCase(previousRegion);
                    if (regionComparison == 0) {
                        // Same region, check country name ordering
                        assertTrue(country.getCountryName().compareToIgnoreCase(previousCountryName) >= 0);
                    } else {
                        // Different region, should be greater
                        assertTrue(regionComparison >= 0);
                    }
                }
                previousRegion = country.getRegion();
                previousCountryName = country.getCountryName();
            }
        }
    }

    @Nested
    @DisplayName("Error Handling and Edge Cases")
    class ErrorHandlingTests {

        @Test
        @DisplayName("Should handle null search terms gracefully")
        void shouldHandleNullSearchTermsGracefully() {
            // These should not throw exceptions
            assertDoesNotThrow(() -> repository.findByCountryNameContainingIgnoreCase(null));
            assertDoesNotThrow(() -> repository.searchCountries(null));
        }

        @Test
        @DisplayName("Should handle empty search terms")
        void shouldHandleEmptySearchTerms() {
            List<CountryEntity> emptyNameSearch = repository.findByCountryNameContainingIgnoreCase("");
            List<CountryEntity> emptyGeneralSearch = repository.searchCountries("");
            
            // Empty searches should return all results
            assertFalse(emptyNameSearch.isEmpty());
            assertFalse(emptyGeneralSearch.isEmpty());
        }

        @Test
        @DisplayName("Should handle special characters in search")
        void shouldHandleSpecialCharactersInSearch() {
            assertDoesNotThrow(() -> repository.searchCountries("$%^&*()"));
            assertDoesNotThrow(() -> repository.findByCountryNameContainingIgnoreCase("'\"\\"));
        }

        @Test
        @DisplayName("Should handle very long search terms")
        void shouldHandleVeryLongSearchTerms() {
            String longTerm = "a".repeat(1000);
            assertDoesNotThrow(() -> repository.searchCountries(longTerm));
            
            List<CountryEntity> results = repository.searchCountries(longTerm);
            assertTrue(results.isEmpty()); // Should return empty, not error
        }
    }

    @Nested
    @DisplayName("Data Consistency Tests")
    class DataConsistencyTests {

        @Test
        @DisplayName("Should maintain referential integrity")
        void shouldMaintainReferentialIntegrity() {
            // Test that country codes are unique
            List<CountryEntity> allCountries = repository.findAll();
            long uniqueCodeCount = allCountries.stream()
                .map(CountryEntity::getCountryCode)
                .distinct()
                .count();
            
            assertEquals(allCountries.size(), uniqueCodeCount);
        }

        @Test
        @DisplayName("Should have consistent region data")
        void shouldHaveConsistentRegionData() {
            List<String> regions = repository.findAllRegions();
            
            for (String region : regions) {
                List<CountryEntity> countriesInRegion = repository.findByRegion(region);
                assertFalse(countriesInRegion.isEmpty());
                
                // All countries in this region should have the same region
                assertTrue(countriesInRegion.stream()
                    .allMatch(c -> region.equals(c.getRegion())));
            }
        }

        @Test
        @DisplayName("Should have consistent currency data")
        void shouldHaveConsistentCurrencyData() {
            List<String> currencies = repository.findAllCurrencyCodes();
            
            for (String currency : currencies) {
                List<CountryEntity> countriesWithCurrency = repository.findByCurrencyCode(currency);
                assertFalse(countriesWithCurrency.isEmpty());
                
                // All countries with this currency should have the same currency code
                assertTrue(countriesWithCurrency.stream()
                    .allMatch(c -> currency.equals(c.getCurrencyCode())));
            }
        }
    }
}
