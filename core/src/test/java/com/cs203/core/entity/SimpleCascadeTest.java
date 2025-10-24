package com.cs203.core.entity;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@DisplayName("Simple Cascade Tests")
class SimpleCascadeTest {

    @Autowired
    private TestEntityManager entityManager;

    @Test
    @Transactional
    @Rollback
    @DisplayName("Should update ProductCategoriesEntity and cascade to children")
    void shouldUpdateProductCategoriesEntityWithCascade() {
        // Create entities with proper validation
        UserEntity testUser = new UserEntity();
        testUser.setUsername("cascadeuser");
        testUser.setPasswordHash("password123");
        testUser.setEmail("cascade@test.com");
        testUser.setFirstName("Cascade");
        testUser.setLastName("User");

        CountryEntity importingCountry = new CountryEntity("T1", "Test Country 1", "Test Region", "TC1");
        CountryEntity exportingCountry = new CountryEntity("T2", "Test Country 2", "Test Region", "TC2");
        ProductCategoriesEntity productCategory = new ProductCategoriesEntity(850110, "DC Motors",
                "Electric motors description", true);

        // Persist base entities
        entityManager.persistAndFlush(testUser);
        entityManager.persistAndFlush(importingCountry);
        entityManager.persistAndFlush(exportingCountry);
        entityManager.persistAndFlush(productCategory);

        // Create child entities that reference ProductCategoriesEntity
        SavedCalculationsEntity calculation = new SavedCalculationsEntity(
                "Test Calculation",
                testUser,
                importingCountry,
                exportingCountry,
                productCategory,
                new BigDecimal("1000.00"),
                "TC1",
                new BigDecimal("0.10"),
                "MFN",
                new BigDecimal("100.00"),
                new BigDecimal("1100.00"),
                "Test calculation notes");

        TariffRateEntity tariffRate = new TariffRateEntity(
                importingCountry,
                exportingCountry,
                productCategory,
                new BigDecimal("0.15"),
                "MFN",
                "PER_UNIT",
                LocalDate.now(),
                LocalDate.now().plusYears(1),
                false);

        NationalTariffLinesEntity tariffLine = new NationalTariffLinesEntity(
                importingCountry,
                "8501.10.10",
                "DC motors not exceeding 37.5 W",
                productCategory,
                8);
        tariffLine.setCreatedBy(testUser);
        tariffLine.setUpdatedBy(testUser);

        // Persist child entities
        entityManager.persistAndFlush(calculation);
        entityManager.persistAndFlush(tariffRate);
        entityManager.persistAndFlush(tariffLine);

        // Verify initial state
        assertEquals("DC Motors", productCategory.getCategoryName());
        assertEquals("Electric motors description", productCategory.getDescription());

        // Update ProductCategoriesEntity
        productCategory.setCategoryName("Updated DC Motors");
        productCategory.setDescription("Updated electric motors description");
        productCategory.setIsActive(false);

        entityManager.flush();

        // Clear the persistence context to force refresh from database
        entityManager.clear();

        // Retrieve entities again and verify updates were cascaded
        ProductCategoriesEntity updatedCategory = entityManager.find(ProductCategoriesEntity.class,
                productCategory.getId());
        SavedCalculationsEntity updatedCalculation = entityManager.find(SavedCalculationsEntity.class,
                calculation.getId());
        TariffRateEntity updatedTariffRate = entityManager.find(TariffRateEntity.class, tariffRate.getId());
        NationalTariffLinesEntity updatedTariffLine = entityManager.find(NationalTariffLinesEntity.class,
                tariffLine.getId());

        // Verify the parent entity was updated
        assertEquals("Updated DC Motors", updatedCategory.getCategoryName());
        assertEquals("Updated electric motors description", updatedCategory.getDescription());
        assertEquals(false, updatedCategory.getIsActive());

        // Verify child entities still reference the updated parent correctly
        assertEquals(updatedCategory.getCategoryCode(), updatedCalculation.getProductCategory().getCategoryCode());
        assertEquals(updatedCategory.getCategoryCode(), updatedTariffRate.getHsCode());
        assertEquals(updatedCategory.getCategoryCode(), updatedTariffLine.getParentHsCode().getCategoryCode());

        // Verify relationships are maintained
        assertNotNull(updatedCalculation.getProductCategory());
        assertNotNull(updatedTariffRate.getProductCategory());
        assertNotNull(updatedTariffLine.getParentHsCode());
    }

    @Test
    @Transactional
    @Rollback
    @DisplayName("Should update UserEntity and maintain child relationships")
    void shouldUpdateUserEntityWithCascade() {
        // Create entities
        UserEntity testUser = new UserEntity();
        testUser.setUsername("usertest");
        testUser.setPasswordHash("password123");
        testUser.setEmail("user@test.com");
        testUser.setFirstName("Test");
        testUser.setLastName("User");

        CountryEntity country = new CountryEntity("T3", "Test Country 3", "Test Region", "TC3");
        ProductCategoriesEntity productCategory = new ProductCategoriesEntity(850120, "AC Motors",
                "AC motors description", true);

        entityManager.persistAndFlush(testUser);
        entityManager.persistAndFlush(country);
        entityManager.persistAndFlush(productCategory);

        // Create child entity referencing the user
        SavedCalculationsEntity calculation = new SavedCalculationsEntity(
                "User Test Calculation",
                testUser,
                country,
                country,
                productCategory,
                new BigDecimal("500.00"),
                "TC3",
                new BigDecimal("0.05"),
                "MFN",
                new BigDecimal("25.00"),
                new BigDecimal("525.00"),
                "User test calculation");

        entityManager.persistAndFlush(calculation);

        // Update user
        testUser.setFirstName("Updated");
        testUser.setLastName("TestUser");
        testUser.setEmail("updated@test.com");

        entityManager.flush();
        entityManager.clear();

        // Verify updates
        UserEntity updatedUser = entityManager.find(UserEntity.class, testUser.getId());
        SavedCalculationsEntity updatedCalculation = entityManager.find(SavedCalculationsEntity.class,
                calculation.getId());

        assertEquals("Updated", updatedUser.getFirstName());
        assertEquals("TestUser", updatedUser.getLastName());
        assertEquals("updated@test.com", updatedUser.getEmail());

        // Verify relationship is maintained
        assertEquals(updatedUser.getId(), updatedCalculation.getUser().getId());
    }
}