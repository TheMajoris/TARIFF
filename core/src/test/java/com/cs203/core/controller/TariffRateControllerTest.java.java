package com.cs203.core.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.cs203.core.dto.CreateProductCategoriesDto;
import com.cs203.core.dto.CreateTariffRateDto;
import com.cs203.core.dto.GenericResponse;
import com.cs203.core.dto.ProductCategoriesDto;
import com.cs203.core.dto.TariffRateDto;
import com.cs203.core.dto.requests.TariffCalculatorRequestDto;
import com.cs203.core.exception.GlobalExceptionHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import com.cs203.core.service.TariffRateService;

class TariffRateControllerTest {
    private MockMvc mockMvc;
    private TariffRateService tariffRateService;
    private TariffRateController controller;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() throws Exception {
        tariffRateService = Mockito.mock(TariffRateService.class);
        controller = new TariffRateController();
        objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules(); // This helps with LocalDate serialization

        // Set up validator
        LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
        validator.afterPropertiesSet();

        // Inject mocked service into controller
        Field f = TariffRateController.class.getDeclaredField("tariffRateService");
        f.setAccessible(true);
        f.set(controller, tariffRateService);

        // Configure MockMvc with exception handling and validation support
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .setValidator(validator) // Add validation support
                .build();
    }

    @Test
    @DisplayName("GET /api/v1/tariff-rate returns all tariff rates")
    void getAllTariffRates_returnsListOfTariffRates() throws Exception {
        // Create test data
        TariffRateDto rate1 = new TariffRateDto();
        rate1.setId(1L);
        rate1.setTariffRate(new BigDecimal("0.1"));
        rate1.setTariffType("ad-valorem");
        rate1.setRateUnit("percent");
        rate1.setEffectiveDate(LocalDate.now());
        rate1.setImportingCountryCode("US");
        rate1.setExportingCountryCode("CN");

        TariffRateDto rate2 = new TariffRateDto();
        rate2.setId(2L);
        rate2.setTariffRate(new BigDecimal("0.20"));
        rate2.setTariffType("ad-valorem");
        rate2.setRateUnit("percent");
        rate2.setEffectiveDate(LocalDate.now());
        rate2.setImportingCountryCode("US");
        rate2.setExportingCountryCode("JP");

        List<TariffRateDto> rates = Arrays.asList(rate1, rate2);

        Mockito.when(tariffRateService.getAllTariffRates()).thenReturn(rates);

        mockMvc.perform(get("/api/v1/tariff-rate")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].tariffRate").value("0.1"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].tariffRate").value("0.2"));
    }

    @Test
    @DisplayName("GET /api/v1/tariff-rate/{id} returns tariff rate by id")
    void getTariffRateById_returnsMatchingTariffRate() throws Exception {
        TariffRateDto rate = new TariffRateDto();
        rate.setId(1L);
        rate.setTariffRate(new BigDecimal("0.1"));
        rate.setTariffType("ad-valorem");
        rate.setRateUnit("percent");
        rate.setEffectiveDate(LocalDate.now());
        rate.setImportingCountryCode("US");
        rate.setExportingCountryCode("CN");

        GenericResponse<TariffRateDto> response = new GenericResponse<>(HttpStatus.OK, "Tariff rate found", rate);

        Mockito.when(tariffRateService.getTariffRateById(1L)).thenReturn(response);

        mockMvc.perform(get("/api/v1/tariff-rate/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.message").value("Tariff rate found"))
                .andExpect(jsonPath("$.data.tariffRate").value("0.1"));
    }

    @Test
    @DisplayName("GET /api/v1/tariff-rate/{id} returns 404 when tariff rate not found")
    void getTariffRateById_returnsNotFound_whenTariffRateDoesNotExist() throws Exception {
        GenericResponse<TariffRateDto> response = new GenericResponse<>(HttpStatus.NOT_FOUND, "Tariff rate not found",
                null);

        Mockito.when(tariffRateService.getTariffRateById(999L)).thenReturn(response);

        mockMvc.perform(get("/api/v1/tariff-rate/999")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Tariff rate not found"))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    @DisplayName("POST /api/v1/tariff-rate/create creates new tariff rate")
    void createTariffRate_returnsCreatedTariffRate() throws Exception {
        // Create a product category DTO
        CreateProductCategoriesDto productCategory = new CreateProductCategoriesDto();
        productCategory.setCategoryCode(123);
        productCategory.setCategoryName("Test Category");
        productCategory.setDescription("Test Description");

        CreateTariffRateDto createDto = new CreateTariffRateDto();
        createDto.setTariffRate(new BigDecimal("0.1"));
        createDto.setTariffType("ad-valorem");
        createDto.setRateUnit("percent");
        createDto.setEffectiveDate(LocalDate.now());
        createDto.setImportingCountryCode("US");
        createDto.setExportingCountryCode("CN");
        createDto.setPreferentialTariff(false);
        createDto.setProductCategory(productCategory);

        // Create product category DTO for response
        ProductCategoriesDto productCategoryResponse = new ProductCategoriesDto();
        productCategoryResponse.setCategoryCode(123);
        productCategoryResponse.setCategoryName("Test Category");
        productCategoryResponse.setDescription("Test Description");

        TariffRateDto createdRate = new TariffRateDto();
        createdRate.setId(1L);
        createdRate.setTariffRate(createDto.getTariffRate());
        createdRate.setTariffType(createDto.getTariffType());
        createdRate.setRateUnit(createDto.getRateUnit());
        createdRate.setEffectiveDate(createDto.getEffectiveDate());
        createdRate.setImportingCountryCode(createDto.getImportingCountryCode());
        createdRate.setExportingCountryCode(createDto.getExportingCountryCode());
        createdRate.setPreferentialTariff(createDto.getPreferentialTariff());
        createdRate.setProductCategory(productCategoryResponse);

        Mockito.when(tariffRateService.createTariffRate(any(CreateTariffRateDto.class))).thenReturn(createdRate);

        mockMvc.perform(post("/api/v1/tariff-rate/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.tariffRate").value("0.1"))
                .andExpect(jsonPath("$.tariffType").value("ad-valorem"));
    }

    @Test
    @DisplayName("PUT /api/v1/tariff-rate/{id} updates existing tariff rate")
    void updateTariffRate_returnsUpdatedTariffRate() throws Exception {
        // Create product category DTO for request
        ProductCategoriesDto productCategory = new ProductCategoriesDto();
        productCategory.setCategoryCode(123);
        productCategory.setCategoryName("Test Category");
        productCategory.setDescription("Test Description");

        TariffRateDto updateDto = new TariffRateDto();
        updateDto.setId(1L);
        updateDto.setTariffRate(new BigDecimal("0.15"));
        updateDto.setTariffType("ad-valorem");
        updateDto.setRateUnit("percent");
        updateDto.setEffectiveDate(LocalDate.now());
        updateDto.setImportingCountryCode("US");
        updateDto.setExportingCountryCode("CN");
        updateDto.setProductCategory(productCategory);

        GenericResponse<TariffRateDto> response = new GenericResponse<>(HttpStatus.OK, "Tariff rate updated",
                updateDto);

        Mockito.when(tariffRateService.updateTariffRate(any(TariffRateDto.class), eq(1L))).thenReturn(response);

        mockMvc.perform(put("/api/v1/tariff-rate/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.tariffRate").value("0.15"))
                .andExpect(jsonPath("$.message").value("Tariff rate updated"));
    }

    @Test
    @DisplayName("PUT /api/v1/tariff-rate/{id} returns 404 when tariff rate not found")
    void updateTariffRate_returnsNotFound_whenTariffRateDoesNotExist() throws Exception {
        // Create product category DTO for request
        ProductCategoriesDto productCategory = new ProductCategoriesDto();
        productCategory.setCategoryCode(123);
        productCategory.setCategoryName("Test Category");
        productCategory.setDescription("Test Description");

        TariffRateDto updateDto = new TariffRateDto();
        updateDto.setId(999L);
        updateDto.setTariffRate(new BigDecimal("0.15"));
        updateDto.setTariffType("ad-valorem");
        updateDto.setRateUnit("percent");
        updateDto.setEffectiveDate(LocalDate.now());
        updateDto.setImportingCountryCode("US");
        updateDto.setExportingCountryCode("CN");
        updateDto.setProductCategory(productCategory);

        GenericResponse<TariffRateDto> response = new GenericResponse<>(HttpStatus.NOT_FOUND, "Tariff rate not found",
                null);

        Mockito.when(tariffRateService.updateTariffRate(any(TariffRateDto.class), eq(999L))).thenReturn(response);

        mockMvc.perform(put("/api/v1/tariff-rate/999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Tariff rate not found"));
    }

    @Test
    @DisplayName("DELETE /api/v1/tariff-rate/{id} deletes tariff rate")
    void deleteTariffRate_returnsSuccess() throws Exception {
        GenericResponse<Void> response = new GenericResponse<>(HttpStatus.OK, "Tariff rate deleted", null);

        Mockito.when(tariffRateService.deleteTariffRate(1L)).thenReturn(response);

        mockMvc.perform(delete("/api/v1/tariff-rate/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Tariff rate deleted"));
    }

    @Test
    @DisplayName("DELETE /api/v1/tariff-rate/{id} returns 404 when tariff rate not found")
    void deleteTariffRate_returnsNotFound_whenTariffRateDoesNotExist() throws Exception {
        GenericResponse<Void> response = new GenericResponse<>(HttpStatus.NOT_FOUND, "Tariff rate not found", null);

        Mockito.when(tariffRateService.deleteTariffRate(999L)).thenReturn(response);

        mockMvc.perform(delete("/api/v1/tariff-rate/999")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Tariff rate not found"));
    }

    @Test
    @DisplayName("POST /api/v1/tariff-rate/calculate returns TariffCalculatorResponseDto with tariffRate, tariffCost, and finalPrice")
    void post_calculate_returnsTariffCalculatorResponseDto() throws Exception {
        // Mock the service methods to return expected values
        LocalDate date = LocalDate.of(2025, 1, 1);
        Mockito.when(tariffRateService.getLowestActiveTariffRate(eq(1L), eq(2L), eq(123), eq(new BigDecimal("100.00")),
                eq(date)))
                .thenReturn(new BigDecimal("0.1"));
        Mockito.when(tariffRateService.getFinalPrice(eq(1L), eq(2L), eq(123), eq(new BigDecimal("100.00")), eq(date)))
                .thenReturn(new BigDecimal("110.00"));
        Mockito.when(tariffRateService.getTariffCost(eq(new BigDecimal("110.00")), eq(new BigDecimal("100.00"))))
                .thenReturn(new BigDecimal("10.00"));

        String requestJson = "{\"importingCountryId\":1,\"exportingCountryId\":2,\"hsCode\":123,\"initialPrice\":100.00,\"date\":\"2025-01-01\"}";

        mockMvc.perform(post("/api/v1/tariff-rate/calculate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tariffRate").value(0.1))
                .andExpect(jsonPath("$.tariffCost").value(10.00))
                .andExpect(jsonPath("$.finalPrice").value(110.00));
    }

    @Test
    @DisplayName("POST /api/v1/tariff-rate/calculate with zero tariff rate returns zero tariff cost")
    void post_calculate_zeroTariffRate_returnsZeroTariffCost() throws Exception {
        // Mock the service methods to return expected values for zero tariff rate
        LocalDate date = LocalDate.of(2025, 1, 1);
        Mockito.when(tariffRateService.getLowestActiveTariffRate(eq(1L), eq(2L), eq(123), eq(new BigDecimal("100.00")),
                eq(date)))
                .thenReturn(BigDecimal.ZERO);
        Mockito.when(tariffRateService.getFinalPrice(eq(1L), eq(2L), eq(123), eq(new BigDecimal("100.00")), eq(date)))
                .thenReturn(new BigDecimal("100.00"));
        Mockito.when(tariffRateService.getTariffCost(eq(new BigDecimal("100.00")), eq(new BigDecimal("100.00"))))
                .thenReturn(BigDecimal.ZERO);

        String requestJson = "{\"importingCountryId\":1,\"exportingCountryId\":2,\"hsCode\":123,\"initialPrice\":100.00,\"date\":\"2025-01-01\"}";

        mockMvc.perform(post("/api/v1/tariff-rate/calculate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tariffRate").value(0))
                .andExpect(jsonPath("$.tariffCost").value(0))
                .andExpect(jsonPath("$.finalPrice").value(100.00));
    }

    @Test
    @DisplayName("POST /api/v1/tariff-rate/create returns 400 for invalid input")
    void createTariffRate_returnsBadRequest_whenInputInvalid() throws Exception {
        CreateTariffRateDto invalidDto = new CreateTariffRateDto();
        // Missing required fields will trigger validation

        mockMvc.perform(post("/api/v1/tariff-rate/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.message").value("Data may be invalid. Please check your inputs."));
    }

    @Test
    @DisplayName("PUT /api/v1/tariff-rate/{id} returns 400 for invalid input")
    void updateTariffRate_returnsBadRequest_whenInputInvalid() throws Exception {
        TariffRateDto invalidDto = new TariffRateDto();
        // Missing required fields will trigger validation
        invalidDto.setId(1L); // Set ID but leave other required fields empty

        mockMvc.perform(put("/api/v1/tariff-rate/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.message").value("Data may be invalid. Please check your inputs."));
    }

    @Test
    @DisplayName("POST /api/v1/tariff-rate/create returns 400 for invalid tariff rate")
    void createTariffRate_returnsBadRequest_whenTariffRateInvalid() throws Exception {
        CreateProductCategoriesDto productCategory = new CreateProductCategoriesDto();
        productCategory.setCategoryCode(123);
        productCategory.setCategoryName("Test Category");
        productCategory.setDescription("Test Description");

        CreateTariffRateDto invalidDto = new CreateTariffRateDto();
        invalidDto.setTariffRate(new BigDecimal("1000.0")); // Above maximum allowed value
        invalidDto.setTariffType("ad-valorem");
        invalidDto.setRateUnit("percent");
        invalidDto.setEffectiveDate(LocalDate.now());
        invalidDto.setImportingCountryCode("US");
        invalidDto.setExportingCountryCode("CN");
        invalidDto.setPreferentialTariff(false);
        invalidDto.setProductCategory(productCategory);

        mockMvc.perform(post("/api/v1/tariff-rate/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.message").value("Data may be invalid. Please check your inputs."));
    }

    @Test
    @DisplayName("POST /api/v1/tariff-rate/calculate returns 400 for invalid input")
    void calculate_returnsBadRequest_whenInputInvalid() throws Exception {
        String invalidJson = "{\"hsCode\":123}"; // Missing required fields

        mockMvc.perform(post("/api/v1/tariff-rate/calculate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.message").value("Data may be invalid. Please check your inputs."));
    }

    @Test
    @DisplayName("POST /api/v1/tariff-rate/calculate returns 400 for negative price")
    void calculate_returnsBadRequest_whenPriceIsNegative() throws Exception {
        String requestJson = "{\"importingCountryId\":1,\"exportingCountryId\":2,\"hsCode\":123,\"initialPrice\":-100.00,\"date\":\"2025-01-01\"}";

        mockMvc.perform(post("/api/v1/tariff-rate/calculate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.message").value("Data may be invalid. Please check your inputs."));
    }
}
