package com.cs203.core.repository;

import com.cs203.core.entity.CountryEntity;
import com.cs203.core.entity.ProductCategoriesEntity;
import com.cs203.core.entity.TariffRateEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.TestPropertySource;

import java.math.BigDecimal;
import java.time.LocalDate;
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
        "spring.jpa.show-sql=false",
        "spring.sql.init.mode=never"
})
class TariffRateRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private TariffRateRepository tariffRateRepository;

    private CountryEntity usa;
    private CountryEntity singapore;
    private CountryEntity canada;
    private ProductCategoriesEntity electronicsCategory;
    private ProductCategoriesEntity textilesCategory;
    private TariffRateEntity currentRate;
    private TariffRateEntity expiredRate;
    private TariffRateEntity futureRate;

    @BeforeEach
    void setUp() {
        // Create countries
        usa = new CountryEntity();
        usa.setCountryCode("US");
        usa.setCountryName("United States");
        entityManager.persistAndFlush(usa);

        singapore = new CountryEntity();
        singapore.setCountryCode("SG");
        singapore.setCountryName("Singapore");
        entityManager.persistAndFlush(singapore);

        canada = new CountryEntity();
        canada.setCountryCode("CA");
        canada.setCountryName("Canada");
        entityManager.persistAndFlush(canada);

        // Create product categories
        electronicsCategory = new ProductCategoriesEntity();
        electronicsCategory.setCategoryCode(850110);
        electronicsCategory.setCategoryName("Electronics");
        entityManager.persistAndFlush(electronicsCategory);

        textilesCategory = new ProductCategoriesEntity();
        textilesCategory.setCategoryCode(620300);
        textilesCategory.setCategoryName("Textiles");
        entityManager.persistAndFlush(textilesCategory);

        // Create test tariff rates
        currentRate = new TariffRateEntity();
        currentRate.setImportingCountry(usa);
        currentRate.setExportingCountry(singapore);
        currentRate.setProductCategory(electronicsCategory);
        currentRate.setTariffRate(new BigDecimal("5.25"));
        currentRate.setTariffType("AD_VALOREM");
        currentRate.setRateUnit("PERCENT");
        currentRate.setEffectiveDate(LocalDate.now().minusDays(10));
        currentRate.setExpiryDate(LocalDate.now().plusDays(365));
        currentRate.setPreferentialTariff(false);
        entityManager.persistAndFlush(currentRate);

        expiredRate = new TariffRateEntity();
        expiredRate.setImportingCountry(usa);
        expiredRate.setExportingCountry(singapore);
        expiredRate.setProductCategory(electronicsCategory);
        expiredRate.setTariffRate(new BigDecimal("7.50"));
        expiredRate.setTariffType("AD_VALOREM");
        expiredRate.setRateUnit("PERCENT");
        expiredRate.setEffectiveDate(LocalDate.now().minusDays(100));
        expiredRate.setExpiryDate(LocalDate.now().minusDays(50));
        expiredRate.setPreferentialTariff(false);
        entityManager.persistAndFlush(expiredRate);

        futureRate = new TariffRateEntity();
        futureRate.setImportingCountry(usa);
        futureRate.setExportingCountry(singapore);
        futureRate.setProductCategory(electronicsCategory);
        futureRate.setTariffRate(new BigDecimal("4.00"));
        futureRate.setTariffType("AD_VALOREM");
        futureRate.setRateUnit("PERCENT");
        futureRate.setEffectiveDate(LocalDate.now().plusDays(30));
        futureRate.setExpiryDate(LocalDate.now().plusDays(400));
        futureRate.setPreferentialTariff(false);
        entityManager.persistAndFlush(futureRate);
    }

    @Test
    @DisplayName("Should find tariff rates by country pair and HS code")
    void shouldFindTariffRatesByCountryPairAndHsCode() {
        List<TariffRateEntity> rates = tariffRateRepository
                .findByImportingCountryIdAndExportingCountryIdAndHsCode(
                        usa.getId(), singapore.getId(), 850110);

        assertEquals(3, rates.size());
        assertThat(rates)
                .extracting(TariffRateEntity::getTariffRate)
                .contains(
                        new BigDecimal("5.25"),
                        new BigDecimal("7.50"),
                        new BigDecimal("4.00"));
    }

    @Test
    @DisplayName("Should find current active tariff rate")
    void shouldFindCurrentActiveTariffRate() {
        LocalDate currentDate = LocalDate.now();

        Optional<TariffRateEntity> result = tariffRateRepository
                .findCurrentTariffRate(usa.getId(), singapore.getId(), 850110, currentDate);

        assertTrue(result.isPresent());
        // Use compareTo for BigDecimal comparison to handle precision
        assertEquals(0, new BigDecimal("5.25").compareTo(result.get().getTariffRate()));
        assertTrue(result.get().isCurrentlyActive());
    }

    @Test
    @DisplayName("Should find tariff rates by importing country")
    void shouldFindTariffRatesByImportingCountry() {
        List<TariffRateEntity> rates = tariffRateRepository
                .findByImportingCountryId(usa.getId());

        assertEquals(3, rates.size());
        rates.forEach(rate -> assertEquals(usa.getId(), rate.getImportingCountryId()));
    }

    @Test
    @DisplayName("Should find tariff rates by exporting country")
    void shouldFindTariffRatesByExportingCountry() {
        List<TariffRateEntity> rates = tariffRateRepository
                .findByExportingCountryId(singapore.getId());

        assertEquals(3, rates.size());
        rates.forEach(rate -> assertEquals(singapore.getId(), rate.getExportingCountryId()));
    }

    @Test
    @DisplayName("Should find tariff rates by HS code")
    void shouldFindTariffRatesByHsCode() {
        List<TariffRateEntity> rates = tariffRateRepository
                .findByHsCode(850110);

        assertEquals(3, rates.size());
        rates.forEach(rate -> assertEquals(850110, rate.getHsCode()));
    }

    @Test
    @DisplayName("Should find tariff rates by tariff type")
    void shouldFindTariffRatesByTariffType() {
        List<TariffRateEntity> rates = tariffRateRepository
                .findByTariffType("AD_VALOREM");

        assertEquals(3, rates.size());
        rates.forEach(rate -> assertEquals("AD_VALOREM", rate.getTariffType()));
    }

    @Test
    @DisplayName("Should find active tariff rates")
    void shouldFindActiveTariffRates() {
        LocalDate currentDate = LocalDate.now();

        List<TariffRateEntity> activeRates = tariffRateRepository
                .findActiveRates(currentDate);

        assertEquals(1, activeRates.size()); // Only current rate is active
        assertTrue(activeRates.get(0).isCurrentlyActive());
    }

    @Test
    @DisplayName("Should find expired tariff rates")
    void shouldFindExpiredTariffRates() {
        LocalDate currentDate = LocalDate.now();

        List<TariffRateEntity> expiredRates = tariffRateRepository
                .findExpiredRates(currentDate);

        assertEquals(1, expiredRates.size());
        assertEquals(0, new BigDecimal("7.50").compareTo(expiredRates.get(0).getTariffRate()));
        assertTrue(expiredRates.get(0).isExpired());
    }

    @Test
    @DisplayName("Should find future tariff rates")
    void shouldFindFutureTariffRates() {
        LocalDate currentDate = LocalDate.now();

        List<TariffRateEntity> futureRates = tariffRateRepository
                .findFutureRates(currentDate);

        assertEquals(1, futureRates.size());
        assertEquals(0, new BigDecimal("4.00").compareTo(futureRates.get(0).getTariffRate()));
        assertFalse(futureRates.get(0).isEffective());
    }

    @Test
    @DisplayName("Should save and find new tariff rate")
    void shouldSaveAndFindNewTariffRate() {
        TariffRateEntity newRate = new TariffRateEntity();
        newRate.setImportingCountry(usa);
        newRate.setExportingCountry(canada);
        newRate.setProductCategory(textilesCategory);
        newRate.setTariffRate(new BigDecimal("8.75"));
        newRate.setTariffType("AD_VALOREM");
        newRate.setRateUnit("PERCENT");
        newRate.setEffectiveDate(LocalDate.now().minusDays(10));
        newRate.setExpiryDate(LocalDate.now().plusDays(100));
        newRate.setPreferentialTariff(false);

        TariffRateEntity savedRate = tariffRateRepository.save(newRate);

        assertNotNull(savedRate.getId());

        List<TariffRateEntity> foundRates = tariffRateRepository
                .findByImportingCountryIdAndExportingCountryIdAndHsCode(
                        usa.getId(), canada.getId(), 620300);
        assertEquals(1, foundRates.size());
        assertEquals(0, new BigDecimal("8.75").compareTo(foundRates.get(0).getTariffRate()));
    }

    @Test
    @DisplayName("Should count tariff rates by importing country")
    void shouldCountTariffRatesByImportingCountry() {
        long count = tariffRateRepository.countByImportingCountryId(usa.getId());

        assertEquals(3, count);
    }

    @Test
    @DisplayName("Should check if tariff rate exists")
    void shouldCheckIfTariffRateExists() {
        assertTrue(tariffRateRepository
                .existsByImportingCountryIdAndExportingCountryIdAndHsCode(
                        usa.getId(), singapore.getId(), 850110));

        assertFalse(tariffRateRepository
                .existsByImportingCountryIdAndExportingCountryIdAndHsCode(
                        999L, 999L, 999999));
    }

    @Test
    @DisplayName("Should find competitive rates for product")
    void shouldFindCompetitiveRatesForProduct() {
        LocalDate currentDate = LocalDate.now();

        List<TariffRateEntity> competitiveRates = tariffRateRepository
                .findCompetitiveRatesForProduct(usa.getId(), 850110, currentDate);

        assertEquals(1, competitiveRates.size()); // Only current active rate
        assertEquals(0, new BigDecimal("5.25").compareTo(competitiveRates.get(0).getTariffRate()));
    }

    @Test
    @DisplayName("Should delete tariff rate")
    void shouldDeleteTariffRate() {
        Long rateId = currentRate.getId();
        assertTrue(tariffRateRepository.existsById(rateId));

        tariffRateRepository.deleteById(rateId);

        assertFalse(tariffRateRepository.existsById(rateId));
    }

    @Test
    @DisplayName("Should update tariff rate")
    void shouldUpdateTariffRate() {
        TariffRateEntity rateToUpdate = tariffRateRepository.findById(currentRate.getId()).get();
        rateToUpdate.setTariffRate(new BigDecimal("6.00"));
        rateToUpdate.setTariffType("SPECIFIC");
        rateToUpdate.setRateUnit("USD_PER_UNIT");

        TariffRateEntity updatedRate = tariffRateRepository.save(rateToUpdate);

        assertEquals(0, new BigDecimal("6.00").compareTo(updatedRate.getTariffRate()));
        assertEquals("SPECIFIC", updatedRate.getTariffType());
        assertEquals("USD_PER_UNIT", updatedRate.getRateUnit());
    }

    @Test
    @DisplayName("Should find all tariff rates")
    void shouldFindAllTariffRates() {
        List<TariffRateEntity> allRates = tariffRateRepository.findAll();

        assertEquals(3, allRates.size());
    }
}
