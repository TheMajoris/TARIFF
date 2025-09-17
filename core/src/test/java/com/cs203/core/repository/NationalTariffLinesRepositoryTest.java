package com.cs203.core.repository;

import com.cs203.core.entity.CountryEntity;
import com.cs203.core.entity.NationalTariffLinesEntity;
import com.cs203.core.entity.ProductCategoriesEntity;
import com.cs203.core.entity.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.TestPropertySource;

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
class NationalTariffLinesRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private NationalTariffLinesRepository nationalTariffLinesRepository;

    private CountryEntity usaCountry;
    private CountryEntity singaporeCountry;
    private ProductCategoriesEntity electronics;
    private ProductCategoriesEntity textiles;
    private UserEntity adminUser;
    private NationalTariffLinesEntity usaElectronics;
    private NationalTariffLinesEntity usaTextiles;
    private NationalTariffLinesEntity sgElectronics;
    private NationalTariffLinesEntity usaSubCategory;

    @BeforeEach
    void setUp() {
        // Create countries
        usaCountry = new CountryEntity("US", "United States", "North America", "USD");
        singaporeCountry = new CountryEntity("SG", "Singapore", "Asia", "SGD");

        // Create product categories
        electronics = new ProductCategoriesEntity(850110, "Electronic Equipment", "Consumer electronics", 5.5);
        electronics.setIsActive(true);

        textiles = new ProductCategoriesEntity(620300, "Textile Products", "Clothing materials", 12.0);
        textiles.setIsActive(true);

        // Create user
        adminUser = new UserEntity();
        adminUser.setUsername("admin");
        adminUser.setEmail("admin@test.com");
        adminUser.setPasswordHash("password");
        adminUser.setIsAdmin(true);
        adminUser.setFirstName("Admin");
        adminUser.setLastName("Test");
        adminUser.setEnabled(true);

        // Persist base entities first
        entityManager.persistAndFlush(usaCountry);
        entityManager.persistAndFlush(singaporeCountry);
        entityManager.persistAndFlush(electronics);
        entityManager.persistAndFlush(textiles);
        entityManager.persistAndFlush(adminUser);

        // Create national tariff lines
        usaElectronics = new NationalTariffLinesEntity();
        usaElectronics.setTariffLineCode("8501.10.10");
        usaElectronics.setDescription("Electric motors for vehicles");
        usaElectronics.setLevel(8);
        usaElectronics.setCountry(usaCountry);
        usaElectronics.setParentHsCode(electronics);
        usaElectronics.setCreatedBy(adminUser);

        usaTextiles = new NationalTariffLinesEntity();
        usaTextiles.setTariffLineCode("6203.00.20");
        usaTextiles.setDescription("Men's suits of wool");
        usaTextiles.setLevel(8);
        usaTextiles.setCountry(usaCountry);
        usaTextiles.setParentHsCode(textiles);
        usaTextiles.setCreatedBy(adminUser);

        sgElectronics = new NationalTariffLinesEntity();
        sgElectronics.setTariffLineCode("8501.10.00");
        sgElectronics.setDescription("Electric motors - Singapore classification");
        sgElectronics.setLevel(6);
        sgElectronics.setCountry(singaporeCountry);
        sgElectronics.setParentHsCode(electronics);
        sgElectronics.setCreatedBy(adminUser);

        usaSubCategory = new NationalTariffLinesEntity();
        usaSubCategory.setTariffLineCode("8501.10.15");
        usaSubCategory.setDescription("Electric motors subcategory");
        usaSubCategory.setLevel(10);
        usaSubCategory.setCountry(usaCountry);
        usaSubCategory.setParentHsCode(electronics);
        usaSubCategory.setCreatedBy(adminUser);

        // Persist national tariff lines
        entityManager.persistAndFlush(usaElectronics);
        entityManager.persistAndFlush(usaTextiles);
        entityManager.persistAndFlush(sgElectronics);
        entityManager.persistAndFlush(usaSubCategory);
    }

    @Test
    @DisplayName("Should find national tariff line by tariff line code")
    void shouldFindNationalTariffLineByTariffLineCode() {
        Optional<NationalTariffLinesEntity> result = nationalTariffLinesRepository
                .findByTariffLineCode("8501.10.10");

        assertTrue(result.isPresent());
        assertEquals("8501.10.10", result.get().getTariffLineCode());
        assertEquals("Electric motors for vehicles", result.get().getDescription());
        assertEquals(8, result.get().getLevel());
        assertEquals("US", result.get().getCountry().getCountryCode());
    }

    @Test
    @DisplayName("Should return empty when tariff line code not found")
    void shouldReturnEmptyWhenTariffLineCodeNotFound() {
        Optional<NationalTariffLinesEntity> result = nationalTariffLinesRepository
                .findByTariffLineCode("9999.99.99");

        assertFalse(result.isPresent());
    }

    @Test
    @DisplayName("Should find national tariffs by country entity")
    void shouldFindNationalTariffsByCountryEntity() {
        List<NationalTariffLinesEntity> usaTariffs = nationalTariffLinesRepository
                .findByCountry(usaCountry);

        assertEquals(3, usaTariffs.size());
        assertThat(usaTariffs)
                .extracting(NationalTariffLinesEntity::getTariffLineCode)
                .containsExactlyInAnyOrder("8501.10.10", "6203.00.20", "8501.10.15");

        // Verify all belong to USA
        usaTariffs.forEach(tariff -> assertEquals("US", tariff.getCountry().getCountryCode()));
    }

    @Test
    @DisplayName("Should find national tariffs by country ID")
    void shouldFindNationalTariffsByCountryId() {
        List<NationalTariffLinesEntity> usaTariffs = nationalTariffLinesRepository
                .findByCountryId(usaCountry.getId());

        assertEquals(3, usaTariffs.size());
        assertThat(usaTariffs)
                .extracting(NationalTariffLinesEntity::getTariffLineCode)
                .containsExactlyInAnyOrder("8501.10.10", "6203.00.20", "8501.10.15");
    }

    @Test
    @DisplayName("Should find national tariffs by parent HS code")
    void shouldFindNationalTariffsByParentHsCode() {
        List<NationalTariffLinesEntity> electronicsTariffs = nationalTariffLinesRepository
                .findByParentHsCode(electronics);

        assertEquals(3, electronicsTariffs.size());
        assertThat(electronicsTariffs)
                .extracting(NationalTariffLinesEntity::getTariffLineCode)
                .containsExactlyInAnyOrder("8501.10.10", "8501.10.00", "8501.10.15");

        // Verify all have electronics as parent
        electronicsTariffs.forEach(tariff ->
                assertEquals(850110, tariff.getParentHsCode().getCategoryCode()));
    }

    @Test
    @DisplayName("Should find national tariffs by level")
    void shouldFindNationalTariffsByLevel() {
        List<NationalTariffLinesEntity> level8Tariffs = nationalTariffLinesRepository
                .findByLevel(8);

        assertEquals(2, level8Tariffs.size());
        assertThat(level8Tariffs)
                .extracting(NationalTariffLinesEntity::getTariffLineCode)
                .containsExactlyInAnyOrder("8501.10.10", "6203.00.20");

        List<NationalTariffLinesEntity> level6Tariffs = nationalTariffLinesRepository
                .findByLevel(6);

        assertEquals(1, level6Tariffs.size());
        assertEquals("8501.10.00", level6Tariffs.get(0).getTariffLineCode());
    }

    @Test
    @DisplayName("Should find national tariffs by country and level")
    void shouldFindNationalTariffsByCountryAndLevel() {
        List<NationalTariffLinesEntity> usaLevel8Tariffs = nationalTariffLinesRepository
                .findByCountryAndLevel(usaCountry, 8);

        assertEquals(2, usaLevel8Tariffs.size());
        assertThat(usaLevel8Tariffs)
                .extracting(NationalTariffLinesEntity::getTariffLineCode)
                .containsExactlyInAnyOrder("8501.10.10", "6203.00.20");

        // Verify all are USA and level 8
        usaLevel8Tariffs.forEach(tariff -> {
            assertEquals("US", tariff.getCountry().getCountryCode());
            assertEquals(8, tariff.getLevel());
        });
    }

    @Test
    @DisplayName("Should find national tariffs by country ID and level")
    void shouldFindNationalTariffsByCountryIdAndLevel() {
        List<NationalTariffLinesEntity> usaLevel10Tariffs = nationalTariffLinesRepository
                .findByCountryIdAndLevel(usaCountry.getId(), 10);

        assertEquals(1, usaLevel10Tariffs.size());
        assertEquals("8501.10.15", usaLevel10Tariffs.get(0).getTariffLineCode());
        assertEquals(10, usaLevel10Tariffs.get(0).getLevel());
    }

    @Test
    @DisplayName("Should find tariff lines by partial tariff line code")
    void shouldFindTariffLinesByPartialTariffLineCode() {
        List<NationalTariffLinesEntity> tariffsWith8501 = nationalTariffLinesRepository
                .findByTariffLineCodeContaining("8501");

        assertEquals(3, tariffsWith8501.size());
        assertThat(tariffsWith8501)
                .extracting(NationalTariffLinesEntity::getTariffLineCode)
                .containsExactlyInAnyOrder("8501.10.10", "8501.10.00", "8501.10.15");

        List<NationalTariffLinesEntity> tariffsWithDot10 = nationalTariffLinesRepository
                .findByTariffLineCodeContaining(".10");

        assertEquals(3, tariffsWithDot10.size());
        assertThat(tariffsWithDot10)
                .extracting(NationalTariffLinesEntity::getTariffLineCode)
                .containsExactlyInAnyOrder("8501.10.10", "8501.10.00", "8501.10.15");
    }

    @Test
    @DisplayName("Should find tariff lines by country code using custom query")
    void shouldFindTariffLinesByCountryCodeUsingCustomQuery() {
        List<NationalTariffLinesEntity> usaTariffs = nationalTariffLinesRepository
                .findByCountryCode("US");

        assertEquals(3, usaTariffs.size());
        assertThat(usaTariffs)
                .extracting(NationalTariffLinesEntity::getTariffLineCode)
                .containsExactlyInAnyOrder("8501.10.10", "6203.00.20", "8501.10.15");

        List<NationalTariffLinesEntity> sgTariffs = nationalTariffLinesRepository
                .findByCountryCode("SG");

        assertEquals(1, sgTariffs.size());
        assertEquals("8501.10.00", sgTariffs.get(0).getTariffLineCode());
    }

    @Test
    @DisplayName("Should find tariff lines by HS code and country ID using custom query")
    void shouldFindTariffLinesByHsCodeAndCountryIdUsingCustomQuery() {
        List<NationalTariffLinesEntity> usaElectronicsTariffs = nationalTariffLinesRepository
                .findByHsCodeAndCountryId(850110, usaCountry.getId());

        assertEquals(2, usaElectronicsTariffs.size());
        assertThat(usaElectronicsTariffs)
                .extracting(NationalTariffLinesEntity::getTariffLineCode)
                .containsExactlyInAnyOrder("8501.10.10", "8501.10.15");

        List<NationalTariffLinesEntity> sgElectronicsTariffs = nationalTariffLinesRepository
                .findByHsCodeAndCountryId(850110, singaporeCountry.getId());

        assertEquals(1, sgElectronicsTariffs.size());
        assertEquals("8501.10.00", sgElectronicsTariffs.get(0).getTariffLineCode());
    }

    @Test
    @DisplayName("Should check if tariff line exists for country and code")
    void shouldCheckIfTariffLineExistsForCountryAndCode() {
        assertTrue(nationalTariffLinesRepository
                .existsByCountryAndTariffLineCode(usaCountry, "8501.10.10"));

        assertTrue(nationalTariffLinesRepository
                .existsByCountryAndTariffLineCode(singaporeCountry, "8501.10.00"));

        assertFalse(nationalTariffLinesRepository
                .existsByCountryAndTariffLineCode(usaCountry, "9999.99.99"));

        assertFalse(nationalTariffLinesRepository
                .existsByCountryAndTariffLineCode(singaporeCountry, "8501.10.10")); // USA code, not SG
    }

    @Test
    @DisplayName("Should save and find new national tariff line")
    void shouldSaveAndFindNewNationalTariffLine() {
        NationalTariffLinesEntity newTariff = new NationalTariffLinesEntity();
        newTariff.setTariffLineCode("8501.20.00");
        newTariff.setDescription("New electric motor category");
        newTariff.setLevel(6);
        newTariff.setCountry(usaCountry);
        newTariff.setParentHsCode(electronics);
        newTariff.setCreatedBy(adminUser);

        NationalTariffLinesEntity savedTariff = nationalTariffLinesRepository.save(newTariff);

        assertNotNull(savedTariff.getId());

        Optional<NationalTariffLinesEntity> foundTariff = nationalTariffLinesRepository
                .findByTariffLineCode("8501.20.00");
        assertTrue(foundTariff.isPresent());
        assertEquals("New electric motor category", foundTariff.get().getDescription());
        assertEquals(6, foundTariff.get().getLevel());
    }

    @Test
    @DisplayName("Should delete national tariff line")
    void shouldDeleteNationalTariffLine() {
        Long tariffId = usaElectronics.getId();
        assertTrue(nationalTariffLinesRepository.existsById(tariffId));

        nationalTariffLinesRepository.deleteById(tariffId);

        assertFalse(nationalTariffLinesRepository.existsById(tariffId));
        assertFalse(nationalTariffLinesRepository.findByTariffLineCode("8501.10.10").isPresent());
    }

    @Test
    @DisplayName("Should update national tariff line")
    void shouldUpdateNationalTariffLine() {
        NationalTariffLinesEntity tariffToUpdate = nationalTariffLinesRepository
                .findByTariffLineCode("8501.10.10").get();
        tariffToUpdate.setDescription("Updated electric motor description");
        tariffToUpdate.setLevel(9);

        NationalTariffLinesEntity updatedTariff = nationalTariffLinesRepository.save(tariffToUpdate);

        assertEquals("Updated electric motor description", updatedTariff.getDescription());
        assertEquals(9, updatedTariff.getLevel());

        // Verify the change persisted
        NationalTariffLinesEntity reloadedTariff = nationalTariffLinesRepository
                .findByTariffLineCode("8501.10.10").get();
        assertEquals("Updated electric motor description", reloadedTariff.getDescription());
        assertEquals(9, reloadedTariff.getLevel());
    }

    @Test
    @DisplayName("Should find all national tariff lines")
    void shouldFindAllNationalTariffLines() {
        List<NationalTariffLinesEntity> allTariffs = nationalTariffLinesRepository.findAll();

        assertEquals(4, allTariffs.size());
        assertThat(allTariffs)
                .extracting(NationalTariffLinesEntity::getTariffLineCode)
                .containsExactlyInAnyOrder("8501.10.10", "6203.00.20", "8501.10.00", "8501.10.15");
    }

    @Test
    @DisplayName("Should handle empty results for non-existent country code")
    void shouldHandleEmptyResultsForNonExistentCountryCode() {
        List<NationalTariffLinesEntity> result = nationalTariffLinesRepository
                .findByCountryCode("XX");

        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Should handle empty results for non-existent HS code and country combination")
    void shouldHandleEmptyResultsForNonExistentHsCodeAndCountryCombination() {
        List<NationalTariffLinesEntity> result = nationalTariffLinesRepository
                .findByHsCodeAndCountryId(999999, usaCountry.getId());

        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Should handle empty results for partial tariff line code search")
    void shouldHandleEmptyResultsForPartialTariffLineCodeSearch() {
        List<NationalTariffLinesEntity> result = nationalTariffLinesRepository
                .findByTariffLineCodeContaining("9999");

        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Should handle empty results for non-existent level")
    void shouldHandleEmptyResultsForNonExistentLevel() {
        List<NationalTariffLinesEntity> result = nationalTariffLinesRepository
                .findByLevel(99);

        assertTrue(result.isEmpty());
    }
}
