package com.cs203.core.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("NationalTariffLines Tests")
class NationalTariffLinesTest {

    private CountryEntity testCountry;
    private ProductCategoriesEntity testCategory;
    private UserEntity testUser;

    @BeforeEach
    void setUp() {
        testCountry = new CountryEntity("US", "United States", "North America", "USD");
        testCountry.setId(1L);
        testCategory = new ProductCategoriesEntity(850110, "Electric Motors", "Electric motors description", 5.5);
        testCategory.setId(1L);
        testUser = new UserEntity(1L, "testuser", "test@example.com", "hashedpassword", false, "Test", "User", true, null);
        testUser.setId(1L);
    }

    @Test
    @DisplayName("Should create valid NationalTariffLines")
    void shouldCreateValidNationalTariffLines() {
        NationalTariffLinesEntity tariffLine = new NationalTariffLinesEntity(
                testCountry,
                "8501.10.10",
                "DC motors not exceeding 37.5 W",
                testCategory,
                8
        );

        assertEquals(testCountry, tariffLine.getCountry());
        assertEquals("8501.10.10", tariffLine.getTariffLineCode());
        assertEquals("DC motors not exceeding 37.5 W", tariffLine.getDescription());
        assertEquals(testCategory, tariffLine.getParentHsCode());
        assertEquals(8, tariffLine.getLevel());
    }

    @Test
    @DisplayName("Should handle relationships correctly")
    void shouldHandleRelationshipsCorrectly() {
        CountryEntity canadaCountry = new CountryEntity("CA", "Canada", "North America", "CAD");
        canadaCountry.setId(2L);
        ProductCategoriesEntity otherCategory = new ProductCategoriesEntity(850120, "Electric Generators", "Generator description", 7.0);
        otherCategory.setId(2L);
        UserEntity otherUser = new UserEntity(2L, "adminuser", "admin@example.com", "adminpassword", true, "Admin", "User", true, null);
        otherUser.setId(2L);
        NationalTariffLinesEntity tariffLine = new NationalTariffLinesEntity(
                canadaCountry,
                "8501.20.10",
                "Canadian specific tariff line",
                otherCategory,
                7
        );
        tariffLine.setCreatedBy(testUser);
        tariffLine.setUpdatedBy(otherUser);
        assertEquals(canadaCountry, tariffLine.getCountry());
        assertEquals(otherCategory, tariffLine.getParentHsCode());
        assertEquals(testUser, tariffLine.getCreatedBy());
        assertEquals(otherUser, tariffLine.getUpdatedBy());
    }
}
