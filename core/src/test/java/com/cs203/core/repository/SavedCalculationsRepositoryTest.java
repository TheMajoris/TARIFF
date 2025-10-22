package com.cs203.core.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.TestPropertySource;

import com.cs203.core.entity.CountryEntity;
import com.cs203.core.entity.ProductCategoriesEntity;
import com.cs203.core.entity.SavedCalculationsEntity;
import com.cs203.core.entity.UserEntity;

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
public class SavedCalculationsRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private SavedCalculationsRepository savedCalculationsRepository;

    private UserEntity testUser1;
    private UserEntity testUser2;
    private CountryEntity importingCountry;
    private CountryEntity exportingCountry;
    private ProductCategoriesEntity productCategory;
    private SavedCalculationsEntity calculation1;
    private SavedCalculationsEntity calculation2;
    private SavedCalculationsEntity calculation3;

    @BeforeEach
    void setUp() {
        // Create test users
        testUser1 = new UserEntity();
        testUser1.setUsername("testuser1");
        testUser1.setEmail("test1@example.com");
        testUser1.setPasswordHash("hashedPassword1");
        testUser1.setIsAdmin(false);
        testUser1.setFirstName("Test");
        testUser1.setLastName("User1");
        testUser1 = entityManager.persistAndFlush(testUser1);

        testUser2 = new UserEntity();
        testUser2.setUsername("testuser2");
        testUser2.setEmail("test2@example.com");
        testUser2.setPasswordHash("hashedPassword2");
        testUser2.setIsAdmin(false);
        testUser2.setFirstName("Test");
        testUser2.setLastName("User2");
        testUser2 = entityManager.persistAndFlush(testUser2);

        // Create test countries
        importingCountry = new CountryEntity();
        importingCountry.setCountryCode("US");
        importingCountry.setCountryName("United States");
        importingCountry.setRegion("North America");
        importingCountry.setCurrencyCode("USD");
        importingCountry = entityManager.persistAndFlush(importingCountry);

        exportingCountry = new CountryEntity();
        exportingCountry.setCountryCode("SG");
        exportingCountry.setCountryName("Singapore");
        exportingCountry.setRegion("Asia");
        exportingCountry.setCurrencyCode("SGD");
        exportingCountry = entityManager.persistAndFlush(exportingCountry);

        // Create test product category
        productCategory = new ProductCategoriesEntity();
        productCategory.setCategoryCode(123456);
        productCategory.setCategoryName("Test Product Category");
        productCategory.setIsActive(true);
        productCategory = entityManager.persistAndFlush(productCategory);

        // Create test calculations
        calculation1 = new SavedCalculationsEntity();
        calculation1.setCalculationName("Test Calculation 1");
        calculation1.setUser(testUser1);
        calculation1.setImportingCountry(importingCountry);
        calculation1.setExportingCountry(exportingCountry);
        calculation1.setProductCategory(productCategory);
        calculation1.setProductValue(new BigDecimal("1000.00"));
        calculation1.setCurrencyCode("USD");
        calculation1.setTariffRate(new BigDecimal("10.5000"));
        calculation1.setTariffType("Ad Valorem");
        calculation1.setCalculatedTariffCost(new BigDecimal("105.00"));
        calculation1.setTotalCost(new BigDecimal("1105.00"));
        calculation1.setNotes("Test notes for calculation 1");
        calculation1 = entityManager.persistAndFlush(calculation1);

        calculation2 = new SavedCalculationsEntity();
        calculation2.setCalculationName("Test Calculation 2");
        calculation2.setUser(testUser1);
        calculation2.setImportingCountry(importingCountry);
        calculation2.setExportingCountry(exportingCountry);
        calculation2.setProductCategory(productCategory);
        calculation2.setProductValue(new BigDecimal("2000.00"));
        calculation2.setCurrencyCode("USD");
        calculation2.setTariffRate(new BigDecimal("15.0000"));
        calculation2.setTariffType("Specific");
        calculation2.setCalculatedTariffCost(new BigDecimal("300.00"));
        calculation2.setTotalCost(new BigDecimal("2300.00"));
        calculation2.setNotes("Test notes for calculation 2");
        calculation2 = entityManager.persistAndFlush(calculation2);

        calculation3 = new SavedCalculationsEntity();
        calculation3.setCalculationName("Different User Calculation");
        calculation3.setUser(testUser2);
        calculation3.setImportingCountry(importingCountry);
        calculation3.setExportingCountry(exportingCountry);
        calculation3.setProductCategory(productCategory);
        calculation3.setProductValue(new BigDecimal("500.00"));
        calculation3.setCurrencyCode("SGD");
        calculation3.setTariffRate(new BigDecimal("8.0000"));
        calculation3.setTariffType("Ad Valorem");
        calculation3.setCalculatedTariffCost(new BigDecimal("40.00"));
        calculation3.setTotalCost(new BigDecimal("540.00"));
        calculation3.setNotes("Test notes for calculation 3");
        calculation3 = entityManager.persistAndFlush(calculation3);

        entityManager.clear();
    }

    @Test
    void testFindByUser() {
        List<SavedCalculationsEntity> calculations = savedCalculationsRepository.findByUser(testUser1);

        assertEquals(2, calculations.size());
        assertThat(calculations).extracting(SavedCalculationsEntity::getCalculationName)
                .containsExactlyInAnyOrder("Test Calculation 1", "Test Calculation 2");
    }

    @Test
    void testFindByUserId() {
        List<SavedCalculationsEntity> calculations = savedCalculationsRepository.findByUserId(testUser1.getId());

        assertEquals(2, calculations.size());
        assertThat(calculations).extracting(SavedCalculationsEntity::getCalculationName)
                .containsExactlyInAnyOrder("Test Calculation 1", "Test Calculation 2");
    }

    @Test
    void testFindByCalculationNameAndUser_Found() {
        Optional<SavedCalculationsEntity> result = 
                savedCalculationsRepository.findByCalculationNameAndUser("Test Calculation 1", testUser1);

        assertTrue(result.isPresent());
        assertEquals("Test Calculation 1", result.get().getCalculationName());
        assertEquals(testUser1.getId(), result.get().getUser().getId());
    }

    @Test
    void testFindByCalculationNameAndUser_NotFound() {
        Optional<SavedCalculationsEntity> result = 
                savedCalculationsRepository.findByCalculationNameAndUser("Non-existent Calculation", testUser1);

        assertFalse(result.isPresent());
    }

    @Test
    void testFindByUserOrderByCreatedAtDesc() {
        List<SavedCalculationsEntity> calculations = 
                savedCalculationsRepository.findByUserOrderByCreatedAtDesc(testUser1);

        assertEquals(2, calculations.size());
        // Verify ordering (newest first)
        assertTrue(calculations.get(0).getCreatedAt().isAfter(calculations.get(1).getCreatedAt()) ||
                   calculations.get(0).getCreatedAt().equals(calculations.get(1).getCreatedAt()));
    }

    @Test
    void testFindByUserOrderByCalculationNameAsc() {
        List<SavedCalculationsEntity> calculations = 
                savedCalculationsRepository.findByUserOrderByCalculationNameAsc(testUser1);

        assertEquals(2, calculations.size());
        assertEquals("Test Calculation 1", calculations.get(0).getCalculationName());
        assertEquals("Test Calculation 2", calculations.get(1).getCalculationName());
    }

    @Test
    void testFindByImportingCountry() {
        List<SavedCalculationsEntity> calculations = 
                savedCalculationsRepository.findByImportingCountry(importingCountry);

        assertEquals(3, calculations.size());
    }

    @Test
    void testFindByExportingCountry() {
        List<SavedCalculationsEntity> calculations = 
                savedCalculationsRepository.findByExportingCountry(exportingCountry);

        assertEquals(3, calculations.size());
    }

    @Test
    void testFindByProductCategory() {
        List<SavedCalculationsEntity> calculations = 
                savedCalculationsRepository.findByProductCategory(productCategory);

        assertEquals(3, calculations.size());
    }

    @Test
    void testFindByCurrencyCode() {
        List<SavedCalculationsEntity> usdCalculations = 
                savedCalculationsRepository.findByCurrencyCode("USD");
        List<SavedCalculationsEntity> sgdCalculations = 
                savedCalculationsRepository.findByCurrencyCode("SGD");

        assertEquals(2, usdCalculations.size());
        assertEquals(1, sgdCalculations.size());
    }

    @Test
    void testFindByTariffType() {
        List<SavedCalculationsEntity> adValoremCalculations = 
                savedCalculationsRepository.findByTariffType("Ad Valorem");
        List<SavedCalculationsEntity> specificCalculations = 
                savedCalculationsRepository.findByTariffType("Specific");

        assertEquals(2, adValoremCalculations.size());
        assertEquals(1, specificCalculations.size());
    }

    @Test
    void testFindByUserAndCurrencyCode() {
        List<SavedCalculationsEntity> calculations = 
                savedCalculationsRepository.findByUserAndCurrencyCode(testUser1, "USD");

        assertEquals(2, calculations.size());
        assertThat(calculations).allMatch(calc -> calc.getCurrencyCode().equals("USD"));
    }

    @Test
    void testFindByUserAndTariffType() {
        List<SavedCalculationsEntity> calculations = 
                savedCalculationsRepository.findByUserAndTariffType(testUser1, "Ad Valorem");

        assertEquals(1, calculations.size());
        assertEquals("Test Calculation 1", calculations.get(0).getCalculationName());
    }

    @Test
    void testFindByProductValueGreaterThan() {
        List<SavedCalculationsEntity> calculations = 
                savedCalculationsRepository.findByProductValueGreaterThan(new BigDecimal("1500.00"));

        assertEquals(1, calculations.size());
        assertEquals("Test Calculation 2", calculations.get(0).getCalculationName());
    }

    @Test
    void testFindByTotalCostGreaterThan() {
        List<SavedCalculationsEntity> calculations = 
                savedCalculationsRepository.findByTotalCostGreaterThan(new BigDecimal("1000.00"));

        assertEquals(2, calculations.size());
        assertThat(calculations).extracting(SavedCalculationsEntity::getCalculationName)
                .containsExactlyInAnyOrder("Test Calculation 1", "Test Calculation 2");
    }

    @Test
    void testFindByCalculationNameContainingIgnoreCase() {
        List<SavedCalculationsEntity> calculations = 
                savedCalculationsRepository.findByCalculationNameContainingIgnoreCase("test");

        assertEquals(2, calculations.size());
    }

    @Test
    void testFindByUserAndCalculationNameContainingIgnoreCase() {
        List<SavedCalculationsEntity> calculations = 
                savedCalculationsRepository.findByUserAndCalculationNameContainingIgnoreCase(testUser1, "calculation");

        assertEquals(2, calculations.size());
        assertThat(calculations).allMatch(calc -> calc.getUser().getId().equals(testUser1.getId()));
    }

    @Test
    void testFindByNotesContainingIgnoreCase() {
        List<SavedCalculationsEntity> calculations = 
                savedCalculationsRepository.findByNotesContainingIgnoreCase("test notes");

        assertEquals(3, calculations.size());
    }

    @Test
    void testExistsByCalculationNameAndUser_True() {
        boolean exists = savedCalculationsRepository.existsByCalculationNameAndUser("Test Calculation 1", testUser1);

        assertTrue(exists);
    }

    @Test
    void testExistsByCalculationNameAndUser_False() {
        boolean exists = savedCalculationsRepository.existsByCalculationNameAndUser("Non-existent", testUser1);

        assertFalse(exists);
    }

    @Test
    void testCountByUser() {
        long count = savedCalculationsRepository.countByUser(testUser1);

        assertEquals(2, count);
    }

    @Test
    void testCountByCurrencyCode() {
        long usdCount = savedCalculationsRepository.countByCurrencyCode("USD");
        long sgdCount = savedCalculationsRepository.countByCurrencyCode("SGD");

        assertEquals(2, usdCount);
        assertEquals(1, sgdCount);
    }

    @Test
    void testCountByTariffType() {
        long adValoremCount = savedCalculationsRepository.countByTariffType("Ad Valorem");
        long specificCount = savedCalculationsRepository.countByTariffType("Specific");

        assertEquals(2, adValoremCount);
        assertEquals(1, specificCount);
    }

    @Test
    void testFindAllCurrencyCodes() {
        List<String> currencyCodes = savedCalculationsRepository.findAllCurrencyCodes();

        assertEquals(2, currencyCodes.size());
        assertThat(currencyCodes).containsExactly("SGD", "USD"); // Should be ordered
    }

    @Test
    void testFindAllTariffTypes() {
        List<String> tariffTypes = savedCalculationsRepository.findAllTariffTypes();

        assertEquals(2, tariffTypes.size());
        assertThat(tariffTypes).containsExactly("Ad Valorem", "Specific"); // Should be ordered
    }

    @Test
    void testSearchCalculations() {
        List<SavedCalculationsEntity> calculations = 
                savedCalculationsRepository.searchCalculations("test");

        assertEquals(3, calculations.size()); // Should find all calculations with "test" in various fields
    }

    @Test
    void testSearchCalculationsByUser() {
        List<SavedCalculationsEntity> calculations = 
                savedCalculationsRepository.searchCalculationsByUser(testUser1, "calculation");

        assertEquals(2, calculations.size());
        assertThat(calculations).allMatch(calc -> calc.getUser().getId().equals(testUser1.getId()));
    }

    @Test
    void testCalculateAverageProductValueByUser() {
        BigDecimal average = savedCalculationsRepository.calculateAverageProductValueByUser(testUser1);

        assertEquals(0, new BigDecimal("1500.00").compareTo(average)); // (1000 + 2000) / 2 = 1500
    }

    @Test
    void testCalculateAverageTotalCostByUser() {
        BigDecimal average = savedCalculationsRepository.calculateAverageTotalCostByUser(testUser1);

        assertEquals(0, new BigDecimal("1702.50").compareTo(average)); // (1105 + 2300) / 2 = 1702.5
    }

    @Test
    void testCalculateTotalCostSumByUser() {
        BigDecimal sum = savedCalculationsRepository.calculateTotalCostSumByUser(testUser1);

        assertEquals(0, new BigDecimal("3405.00").compareTo(sum)); // 1105 + 2300 = 3405
    }

    @Test
    void testFindTopCalculationsByTotalCost() {
        List<SavedCalculationsEntity> calculations = 
                savedCalculationsRepository.findTopCalculationsByTotalCost(testUser1);

        assertEquals(2, calculations.size());
        assertEquals("Test Calculation 2", calculations.get(0).getCalculationName()); // Highest total cost first
        assertEquals("Test Calculation 1", calculations.get(1).getCalculationName());
    }

    @Test
    void testFindAllByOrderByCreatedAtDesc() {
        List<SavedCalculationsEntity> calculations = 
                savedCalculationsRepository.findAllByOrderByCreatedAtDesc();

        assertEquals(3, calculations.size());
        // Verify descending order
        for (int i = 0; i < calculations.size() - 1; i++) {
            assertTrue(calculations.get(i).getCreatedAt().isAfter(calculations.get(i + 1).getCreatedAt()) ||
                       calculations.get(i).getCreatedAt().equals(calculations.get(i + 1).getCreatedAt()));
        }
    }

    @Test
    void testFindAllByOrderByCalculationNameAsc() {
        List<SavedCalculationsEntity> calculations = 
                savedCalculationsRepository.findAllByOrderByCalculationNameAsc();

        assertEquals(3, calculations.size());
        assertEquals("Different User Calculation", calculations.get(0).getCalculationName());
        assertEquals("Test Calculation 1", calculations.get(1).getCalculationName());
        assertEquals("Test Calculation 2", calculations.get(2).getCalculationName());
    }

    @Test
    void testFindAllByOrderByTotalCostDesc() {
        List<SavedCalculationsEntity> calculations = 
                savedCalculationsRepository.findAllByOrderByTotalCostDesc();

        assertEquals(3, calculations.size());
        assertEquals("Test Calculation 2", calculations.get(0).getCalculationName()); // 2300.00
        assertEquals("Test Calculation 1", calculations.get(1).getCalculationName()); // 1105.00
        assertEquals("Different User Calculation", calculations.get(2).getCalculationName()); // 540.00
    }
}
