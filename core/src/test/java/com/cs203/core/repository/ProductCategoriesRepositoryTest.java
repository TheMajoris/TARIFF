package com.cs203.core.repository;

import com.cs203.core.entity.ProductCategoriesEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.beans.factory.annotation.Autowired;
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
        "spring.jpa.show-sql=false",
        "spring.sql.init.mode=never"
})

class ProductCategoriesRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ProductCategoriesRepository productCategoriesRepository;

    private ProductCategoriesEntity electronics;
    private ProductCategoriesEntity textiles;
    private ProductCategoriesEntity machinery;
    private ProductCategoriesEntity inactiveCategory;

    @BeforeEach
    void setUp() {
        // Create test data
        electronics = new ProductCategoriesEntity(850110, "Electronic Equipment", "Consumer electronics and components",
                5.5);
        electronics.setIsActive(true);

        textiles = new ProductCategoriesEntity(620300, "Textile Products", "Clothing and fabric materials", 12.0);
        textiles.setIsActive(true);

        machinery = new ProductCategoriesEntity(847010, "Industrial Machinery", "Heavy machinery and equipment", 0.0);
        machinery.setIsActive(true);

        inactiveCategory = new ProductCategoriesEntity(999999, "Obsolete Category", "No longer in use", 15.0);
        inactiveCategory.setIsActive(false);

        // Persist test data
        entityManager.persistAndFlush(electronics);
        entityManager.persistAndFlush(textiles);
        entityManager.persistAndFlush(machinery);
        entityManager.persistAndFlush(inactiveCategory);
    }

    @Test
    @DisplayName("Should find product category by category code")
    void shouldFindProductCategoryByCategoryCode() {
        Optional<ProductCategoriesEntity> result = productCategoriesRepository.findByCategoryCode(850110);

        assertTrue(result.isPresent());
        assertEquals(850110, result.get().getCategoryCode());
        assertEquals("Electronic Equipment", result.get().getCategoryName());
        assertEquals(5.5, result.get().getTariffBaseRate());
    }

    @Test
    @DisplayName("Should return empty when category code not found")
    void shouldReturnEmptyWhenCategoryCodeNotFound() {
        Optional<ProductCategoriesEntity> result = productCategoriesRepository.findByCategoryCode(999998);

        assertFalse(result.isPresent());
    }

    @Test
    @DisplayName("Should find product category by category name")
    void shouldFindProductCategoryByCategoryName() {
        Optional<ProductCategoriesEntity> result = productCategoriesRepository.findByCategoryName("Textile Products");

        assertTrue(result.isPresent());
        assertEquals(620300, result.get().getCategoryCode());
        assertEquals("Textile Products", result.get().getCategoryName());
        assertTrue(result.get().getIsActive());
    }

    @Test
    @DisplayName("Should return empty when category name not found")
    void shouldReturnEmptyWhenCategoryNameNotFound() {
        Optional<ProductCategoriesEntity> result = productCategoriesRepository
                .findByCategoryName("Non-existent Category");

        assertFalse(result.isPresent());
    }

    @Test
    @DisplayName("Should check if category code exists")
    void shouldCheckIfCategoryCodeExists() {
        assertTrue(productCategoriesRepository.existsByCategoryCode(850110));
        assertFalse(productCategoriesRepository.existsByCategoryCode(999998));
    }

    @Test
    @DisplayName("Should check if category name exists")
    void shouldCheckIfCategoryNameExists() {
        assertTrue(productCategoriesRepository.existsByCategoryName("Electronic Equipment"));
        assertFalse(productCategoriesRepository.existsByCategoryName("Non-existent Category"));
    }

    @Test
    @DisplayName("Should find all active categories")
    void shouldFindAllActiveCategories() {
        List<ProductCategoriesEntity> activeCategories = productCategoriesRepository.findByIsActiveTrue();

        assertEquals(3, activeCategories.size());
        assertThat(activeCategories)
                .extracting(ProductCategoriesEntity::getCategoryName)
                .containsExactlyInAnyOrder("Electronic Equipment", "Textile Products", "Industrial Machinery");

        // Verify all are active
        activeCategories.forEach(category -> assertTrue(category.getIsActive()));
    }

    @Test
    @DisplayName("Should find all inactive categories")
    void shouldFindAllInactiveCategories() {
        List<ProductCategoriesEntity> inactiveCategories = productCategoriesRepository.findByIsActiveFalse();

        assertEquals(1, inactiveCategories.size());
        assertEquals("Obsolete Category", inactiveCategories.get(0).getCategoryName());
        assertFalse(inactiveCategories.get(0).getIsActive());
    }

    @Test
    @DisplayName("Should find categories by partial name containing (case insensitive)")
    void shouldFindCategoriesByPartialNameContaining() {
        List<ProductCategoriesEntity> categoriesWithEquipment = productCategoriesRepository
                .findByCategoryNameContainingIgnoreCase("EQUIPMENT");

        assertEquals(1, categoriesWithEquipment.size());
        assertEquals("Electronic Equipment", categoriesWithEquipment.get(0).getCategoryName());

        List<ProductCategoriesEntity> categoriesWithProduct = productCategoriesRepository
                .findByCategoryNameContainingIgnoreCase("product");

        assertEquals(1, categoriesWithProduct.size());
        assertEquals("Textile Products", categoriesWithProduct.get(0).getCategoryName());
    }

    @Test
    @DisplayName("Should find categories by tariff rate range")
    void shouldFindCategoriesByTariffRateRange() {
        List<ProductCategoriesEntity> midRangeCategories = productCategoriesRepository
                .findByTariffBaseRateBetween(1.0, 10.0);

        assertEquals(1, midRangeCategories.size());
        assertEquals("Electronic Equipment", midRangeCategories.get(0).getCategoryName());
        assertEquals(5.5, midRangeCategories.get(0).getTariffBaseRate());
    }

    @Test
    @DisplayName("Should find categories with tariff rate greater than specified value")
    void shouldFindCategoriesWithTariffRateGreaterThan() {
        List<ProductCategoriesEntity> highTariffCategories = productCategoriesRepository
                .findByTariffBaseRateGreaterThan(10.0);

        assertEquals(2, highTariffCategories.size());
        assertThat(highTariffCategories)
                .extracting(ProductCategoriesEntity::getCategoryName)
                .containsExactlyInAnyOrder("Textile Products", "Obsolete Category");
    }

    @Test
    @DisplayName("Should find categories with tariff rate less than specified value")
    void shouldFindCategoriesWithTariffRateLessThan() {
        List<ProductCategoriesEntity> lowTariffCategories = productCategoriesRepository
                .findByTariffBaseRateLessThan(1.0);

        assertEquals(1, lowTariffCategories.size());
        assertEquals("Industrial Machinery", lowTariffCategories.get(0).getCategoryName());
        assertEquals(0.0, lowTariffCategories.get(0).getTariffBaseRate());
    }

    @Test
    @DisplayName("Should find active categories ordered by name")
    void shouldFindActiveCategoriesOrderedByName() {
        List<ProductCategoriesEntity> orderedCategories = productCategoriesRepository
                .findByIsActiveTrueOrderByCategoryNameAsc();

        assertEquals(3, orderedCategories.size());
        assertEquals("Electronic Equipment", orderedCategories.get(0).getCategoryName());
        assertEquals("Industrial Machinery", orderedCategories.get(1).getCategoryName());
        assertEquals("Textile Products", orderedCategories.get(2).getCategoryName());
    }

    @Test
    @DisplayName("Should find active categories ordered by tariff rate")
    void shouldFindActiveCategoriesOrderedByTariffRate() {
        List<ProductCategoriesEntity> orderedCategories = productCategoriesRepository
                .findByIsActiveTrueOrderByTariffBaseRateAsc();

        assertEquals(3, orderedCategories.size());
        assertEquals("Industrial Machinery", orderedCategories.get(0).getCategoryName()); // 0.0
        assertEquals("Electronic Equipment", orderedCategories.get(1).getCategoryName()); // 5.5
        assertEquals("Textile Products", orderedCategories.get(2).getCategoryName()); // 12.0
    }

    @Test
    @DisplayName("Should count active categories")
    void shouldCountActiveCategories() {
        long activeCount = productCategoriesRepository.countByIsActiveTrue();

        assertEquals(3, activeCount);
    }

    @Test
    @DisplayName("Should count inactive categories")
    void shouldCountInactiveCategories() {
        long inactiveCount = productCategoriesRepository.countByIsActiveFalse();

        assertEquals(1, inactiveCount);
    }

    @Test
    @DisplayName("Should find all categories ordered by name")
    void shouldFindAllCategoriesOrderedByName() {
        List<ProductCategoriesEntity> orderedCategories = productCategoriesRepository
                .findAllByOrderByCategoryNameAsc();

        assertEquals(4, orderedCategories.size());
        assertEquals("Electronic Equipment", orderedCategories.get(0).getCategoryName());
        assertEquals("Industrial Machinery", orderedCategories.get(1).getCategoryName());
        assertEquals("Obsolete Category", orderedCategories.get(2).getCategoryName());
        assertEquals("Textile Products", orderedCategories.get(3).getCategoryName());
    }

    @Test
    @DisplayName("Should find all categories ordered by tariff rate")
    void shouldFindAllCategoriesOrderedByTariffRate() {
        List<ProductCategoriesEntity> orderedCategories = productCategoriesRepository
                .findAllByOrderByTariffBaseRateAsc();

        assertEquals(4, orderedCategories.size());
        assertEquals("Industrial Machinery", orderedCategories.get(0).getCategoryName()); // 0.0
        assertEquals("Electronic Equipment", orderedCategories.get(1).getCategoryName()); // 5.5
        assertEquals("Textile Products", orderedCategories.get(2).getCategoryName()); // 12.0
        assertEquals("Obsolete Category", orderedCategories.get(3).getCategoryName()); // 15.0
    }

    @Test
    @DisplayName("Should save and find new category")
    void shouldSaveAndFindNewCategory() {
        ProductCategoriesEntity newCategory = new ProductCategoriesEntity(123456, "Test Category", "Test description",
                7.5);
        newCategory.setIsActive(true);

        ProductCategoriesEntity savedCategory = productCategoriesRepository.save(newCategory);

        assertNotNull(savedCategory.getId());

        Optional<ProductCategoriesEntity> foundCategory = productCategoriesRepository.findByCategoryCode(123456);
        assertTrue(foundCategory.isPresent());
        assertEquals("Test Category", foundCategory.get().getCategoryName());
        assertEquals(7.5, foundCategory.get().getTariffBaseRate());
    }

    @Test
    @DisplayName("Should delete category")
    void shouldDeleteCategory() {
        Long categoryId = electronics.getId();
        assertTrue(productCategoriesRepository.existsById(categoryId));

        productCategoriesRepository.deleteById(categoryId);

        assertFalse(productCategoriesRepository.existsById(categoryId));
        assertFalse(productCategoriesRepository.findByCategoryCode(850110).isPresent());
    }

    @Test
    @DisplayName("Should update category")
    void shouldUpdateCategory() {
        ProductCategoriesEntity categoryToUpdate = productCategoriesRepository.findByCategoryCode(850110).get();
        categoryToUpdate.setCategoryName("Updated Electronics");
        categoryToUpdate.setTariffBaseRate(6.0);

        ProductCategoriesEntity updatedCategory = productCategoriesRepository.save(categoryToUpdate);

        assertEquals("Updated Electronics", updatedCategory.getCategoryName());
        assertEquals(6.0, updatedCategory.getTariffBaseRate());

        // Verify the change persisted
        ProductCategoriesEntity reloadedCategory = productCategoriesRepository.findByCategoryCode(850110).get();
        assertEquals("Updated Electronics", reloadedCategory.getCategoryName());
        assertEquals(6.0, reloadedCategory.getTariffBaseRate());
    }

    @Test
    @DisplayName("Should find all categories")
    void shouldFindAllCategories() {
        List<ProductCategoriesEntity> allCategories = productCategoriesRepository.findAll();

        assertEquals(4, allCategories.size());
        assertThat(allCategories)
                .extracting(ProductCategoriesEntity::getCategoryCode)
                .containsExactlyInAnyOrder(850110, 620300, 847010, 999999);
    }

    @Test
    @DisplayName("Should handle empty results for partial name search")
    void shouldHandleEmptyResultsForPartialNameSearch() {
        List<ProductCategoriesEntity> result = productCategoriesRepository
                .findByCategoryNameContainingIgnoreCase("NonExistentTerm");

        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Should handle empty results for tariff rate range")
    void shouldHandleEmptyResultsForTariffRateRange() {
        List<ProductCategoriesEntity> result = productCategoriesRepository
                .findByTariffBaseRateBetween(100.0, 200.0);

        assertTrue(result.isEmpty());
    }
}
