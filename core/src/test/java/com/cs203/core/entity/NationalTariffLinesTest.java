package com.cs203.core.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@DisplayName("NationalTariffLines Tests")
class NationalTariffLinesTest {

    private CountryEntity testCountry;
    private ProductCategoriesEntity testCategory;
    private UserEntity testUser;

    @BeforeEach
    void setUp() {
        testCountry = new CountryEntity("US", "United States", "North America", "USD");
        testCategory = new ProductCategoriesEntity(850110, "Electric Motors", "Electric motors description");

        testUser = new UserEntity();
        testUser.setUsername("testuser");
        testUser.setEmail("test@example.com");
        testUser.setPasswordHash("hashedpassword");
        testUser.setIsAdmin(false);
        testUser.setFirstName("Test");
        testUser.setLastName("User");
        testUser.setEnabled(true);
    }

    @Test
    @DisplayName("Should create valid NationalTariffLines")
    void shouldCreateValidNationalTariffLines() {
        NationalTariffLinesEntity tariffLine = new NationalTariffLinesEntity(
                testCountry,
                "8501.10.10",
                "DC motors not exceeding 37.5 W",
                testCategory,
                8);

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
        ProductCategoriesEntity otherCategory = new ProductCategoriesEntity(850120, "Electric Generators",
                "Generator description");

        UserEntity otherUser = new UserEntity();
        otherUser.setUsername("adminuser");
        otherUser.setEmail("admin@example.com");
        otherUser.setPasswordHash("adminpassword");
        otherUser.setIsAdmin(true);
        otherUser.setFirstName("Admin");
        otherUser.setLastName("User");
        otherUser.setEnabled(true);

        NationalTariffLinesEntity tariffLine = new NationalTariffLinesEntity(
                canadaCountry,
                "8501.20.10",
                "Canadian specific tariff line",
                otherCategory,
                7);
        tariffLine.setCreatedBy(testUser);
        tariffLine.setUpdatedBy(otherUser);
        assertEquals(canadaCountry, tariffLine.getCountry());
        assertEquals(otherCategory, tariffLine.getParentHsCode());
        assertEquals(testUser, tariffLine.getCreatedBy());
        assertEquals(otherUser, tariffLine.getUpdatedBy());
    }
}
