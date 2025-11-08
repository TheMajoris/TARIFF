package com.cs203.core.controller;

import com.cs203.core.dto.CreateProductCategoriesDto;
import com.cs203.core.dto.GenericResponse;
import com.cs203.core.dto.ProductCategoriesDto;
import com.cs203.core.exception.GlobalExceptionHandler;
import com.cs203.core.service.ProductCategoriesService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ProductCategoriesControllerTest {
    private MockMvc mockMvc;
    private ProductCategoriesService productCategoriesService;
    private ProductCategoriesController controller;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() throws Exception {
        productCategoriesService = Mockito.mock(ProductCategoriesService.class);
        controller = new ProductCategoriesController();
        objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();

        // Set up validator
        LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
        validator.afterPropertiesSet();

        // Inject mocked service into controller
        Field f = ProductCategoriesController.class.getDeclaredField("productCategoriesService");
        f.setAccessible(true);
        f.set(controller, productCategoriesService);

        // Configure MockMvc
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .setValidator(validator)
                .build();
    }

    @Test
    @DisplayName("GET /api/v1/product-categories/all returns all product categories")
    void getAllProductCategories_returnsListOfCategories() throws Exception {
        // Create test data
        ProductCategoriesDto category1 = new ProductCategoriesDto();
        category1.setId(1L);
        category1.setCategoryCode(101010);
        category1.setDescription("Live horses, asses, mules and hinnies");

        ProductCategoriesDto category2 = new ProductCategoriesDto();
        category2.setId(2L);
        category2.setCategoryCode(101020);
        category2.setDescription("Live bovine animals");

        List<ProductCategoriesDto> categories = Arrays.asList(category1, category2);

        Mockito.when(productCategoriesService.getProductCategories()).thenReturn(categories);

        mockMvc.perform(get("/api/v1/product-categories/all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].categoryCode").value(101010))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].categoryCode").value(101020));
    }

    @Test
    @DisplayName("GET /api/v1/product-categories with pagination returns paginated product categories")
    void getProductCategories_returnsPaginatedCategories() throws Exception {
        // Create test data
        ProductCategoriesDto category = new ProductCategoriesDto();
        category.setId(1L);
        category.setCategoryCode(101010);
        category.setDescription("Live horses, asses, mules and hinnies");

        List<ProductCategoriesDto> categories = Arrays.asList(category);
        PageRequest pageRequest = PageRequest.of(0, 40, Sort.by("id").ascending()); // pageNo is 0-based
        Page<ProductCategoriesDto> page = new PageImpl<>(categories, pageRequest, categories.size());

        Mockito.when(productCategoriesService.getProductCategories(eq(pageRequest))).thenReturn(page);

        mockMvc.perform(get("/api/v1/product-categories")
                        .param("page", "0") // Changed from pageNo to page, and 1 to 0 since it's 0-based
                        .param("size", "40")
                        .param("sortBy", "id")
                        .param("sortDirection", "ascending")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[0].categoryCode").value(101010));
    }

    @Test
    @DisplayName("GET /api/v1/product-categories/{id} returns product category by id")
    void getProductCategoryById_returnsMatchingCategory() throws Exception {
        ProductCategoriesDto category = new ProductCategoriesDto();
        category.setId(1L);
        category.setCategoryCode(101010);
        category.setDescription("Live horses, asses, mules and hinnies");

        GenericResponse<ProductCategoriesDto> response = new GenericResponse<>(
                HttpStatus.OK,
                "Product category retrieved successfully",
                category);

        Mockito.when(productCategoriesService.getProductCategoriesById(1L)).thenReturn(response);

        mockMvc.perform(get("/api/v1/product-categories/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Product category retrieved successfully"))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.categoryCode").value(101010));
    }

    @Test
    @DisplayName("POST /api/v1/product-categories creates new product category")
    void createProductCategory_returnsCreatedCategory() throws Exception {
        CreateProductCategoriesDto createDto = new CreateProductCategoriesDto();
        createDto.setCategoryCode(101010);
        createDto.setCategoryName("Live horses, asses, mules and hinnies");
        createDto.setIsActive(true);

        ProductCategoriesDto createdCategory = new ProductCategoriesDto();
        createdCategory.setId(1L);
        createdCategory.setCategoryCode(101010);
        createdCategory.setDescription("Live horses, asses, mules and hinnies");

        GenericResponse<ProductCategoriesDto> response = new GenericResponse<>(
                HttpStatus.CREATED,
                "Product category created successfully",
                createdCategory);

        Mockito.when(productCategoriesService.createProductCategories(any(CreateProductCategoriesDto.class)))
                .thenReturn(response);

        mockMvc.perform(post("/api/v1/product-categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Product category created successfully"))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.categoryCode").value(101010));
    }

    @Test
    @DisplayName("PUT /api/v1/product-categories/{id} updates existing product category")
    void updateProductCategory_returnsUpdatedCategory() throws Exception {
        ProductCategoriesDto updateDto = new ProductCategoriesDto();
        updateDto.setId(1L);
        updateDto.setCategoryCode(101010);
        updateDto.setCategoryName("Updated description");
        updateDto.setIsActive(true);

        GenericResponse<ProductCategoriesDto> response = new GenericResponse<>(
                HttpStatus.OK,
                "Product category updated successfully",
                updateDto);

        Mockito.when(productCategoriesService.updateProductCategories(eq(1L), any(ProductCategoriesDto.class)))
                .thenReturn(response);

        mockMvc.perform(put("/api/v1/product-categories/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Product category updated successfully"))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.categoryName").value("Updated description"));
    }

    @Test
    @DisplayName("DELETE /api/v1/product-categories/{id} deletes product category without cascade")
    void deleteProductCategory_returnsSuccessMessage() throws Exception {
        GenericResponse<Void> response = new GenericResponse<>(
                HttpStatus.OK,
                "Product category deleted successfully",
                null);

        Mockito.when(productCategoriesService.deleteProductCategories(1L)).thenReturn(response);

        mockMvc.perform(delete("/api/v1/product-categories/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Product category deleted successfully"));
    }

    @Test
    @DisplayName("DELETE /api/v1/product-categories/{id}?forceDelete=true deletes product category with cascade")
    void deleteProductCategoryWithCascade_returnsSuccessMessage() throws Exception {
        GenericResponse<Void> response = new GenericResponse<>(
                HttpStatus.OK,
                "Product category and related entities deleted successfully",
                null);

        Mockito.when(productCategoriesService.forceDeleteProductCategories(1L)).thenReturn(response);

        mockMvc.perform(delete("/api/v1/product-categories/1")
                        .param("forceDelete", "true")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message")
                        .value("Product category and related entities deleted successfully"));
    }

    @Test
    @DisplayName("DELETE /api/v1/product-categories/{id} returns conflict when has relations")
    void deleteProductCategoryWithRelations_returnsConflict() throws Exception {
        GenericResponse<Void> response = new GenericResponse<>(
                HttpStatus.CONFLICT,
                "Product category has 2 related tariff rates. Set request params flag to true to cascade delete",
                null);

        Mockito.when(productCategoriesService.deleteProductCategories(1L)).thenReturn(response);

        mockMvc.perform(delete("/api/v1/product-categories/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value(
                        "Product category has 2 related tariff rates. Set request params flag to true to cascade delete"));
    }

    @Test
    @DisplayName("GET /api/v1/product-categories with descending sort returns paginated categories")
    void getProductCategories_returnsPaginatedCategories_descending() throws Exception {
        // Arrange - test data
        ProductCategoriesDto category = new ProductCategoriesDto();
        category.setId(1L);
        category.setCategoryCode(101010);
        category.setDescription("Live horses, asses, mules and hinnies");

        List<ProductCategoriesDto> categories = Arrays.asList(category);

        // Descending sort
        PageRequest pageRequest = PageRequest.of(0, 40, Sort.by("id").descending());
        Page<ProductCategoriesDto> page = new PageImpl<>(categories, pageRequest, categories.size());

        Mockito.when(productCategoriesService.getProductCategories(eq(pageRequest))).thenReturn(page);

        // Act & Assert
        mockMvc.perform(get("/api/v1/product-categories")
                        .param("pageNo", "0")   // use correct parameter names
                        .param("size", "40")
                        .param("sortBy", "id")
                        .param("sortDirection", "descending") // triggers else branch
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[0].categoryCode").value(101010));

        // Optional: verify service call
        verify(productCategoriesService, times(1)).getProductCategories(eq(pageRequest));
    }

}