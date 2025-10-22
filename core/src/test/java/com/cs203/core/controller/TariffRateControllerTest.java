package com.cs203.core.controller;

import com.cs203.core.dto.CreateTariffRateDto;
import com.cs203.core.dto.GenericResponse;
import com.cs203.core.dto.ProductCategoriesDto;
import com.cs203.core.dto.TariffRateDto;
import com.cs203.core.entity.TariffRateEntity;
import com.cs203.core.exception.GlobalExceptionHandler;
import com.cs203.core.service.TariffRateService;
import com.cs203.core.strategy.TariffCalculationStrategy;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TariffRateControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private TariffRateService tariffRateService;
    private TariffRateController controller;
    private ObjectMapper objectMapper;
    @Mock
    private TariffCalculationStrategy adValoremStrategy;
    @Mock
    private TariffCalculationStrategy specificStrategy;

    @BeforeEach
    void setUp() throws Exception {
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
    @DisplayName("POST /api/v1/tariff-rate creates new tariff rate")
    void createTariffRate_returnsCreatedTariffRate() throws Exception {
        CreateTariffRateDto createDto = new CreateTariffRateDto();
        createDto.setTariffRate(new BigDecimal("0.1"));
        createDto.setTariffType("ad-valorem");
        createDto.setRateUnit("percent");
        createDto.setEffectiveDate(LocalDate.now());
        createDto.setImportingCountryCode("US");
        createDto.setExportingCountryCode("CN");
        createDto.setPreferentialTariff(false);
        createDto.setHsCode(123123);

        // Create response DTO
        ProductCategoriesDto productCategoryResponse = new ProductCategoriesDto();
        productCategoryResponse.setCategoryCode(123123);
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

        // Mock the service response
        GenericResponse<TariffRateDto> response = new GenericResponse<>(HttpStatus.CREATED, "Tariff rate created", createdRate);
        Mockito.when(tariffRateService.createTariffRate(any(CreateTariffRateDto.class))).thenReturn(response);

        mockMvc.perform(post("/api/v1/tariff-rate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.tariffRate").value("0.1"))
                .andExpect(jsonPath("$.data.tariffType").value("ad-valorem"))
                .andExpect(jsonPath("$.data.preferentialTariff").value(false))
                .andExpect(jsonPath("$.data.productCategory.categoryCode").value(123123));
    }

    @Test
    @DisplayName("PUT /api/v1/tariff-rate/{id} updates existing tariff rate")
    void updateTariffRate_returnsUpdatedTariffRate() throws Exception {
        TariffRateDto updateDto = new TariffRateDto();
        updateDto.setId(1L);
        updateDto.setTariffRate(new BigDecimal("0.15"));
        updateDto.setTariffType("ad-valorem");
        updateDto.setRateUnit("percent");
        updateDto.setEffectiveDate(LocalDate.now());
        updateDto.setImportingCountryCode("US");
        updateDto.setExportingCountryCode("CN");

        // Create response DTO with associated product category
        ProductCategoriesDto productCategory = new ProductCategoriesDto();
        productCategory.setCategoryCode(123123);
        productCategory.setCategoryName("Test Category");
        productCategory.setDescription("Test Description");
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
        TariffRateDto updateDto = new TariffRateDto();
        updateDto.setId(999L);
        updateDto.setTariffRate(new BigDecimal("0.15"));
        updateDto.setTariffType("ad-valorem");
        updateDto.setRateUnit("percent");
        updateDto.setEffectiveDate(LocalDate.now());
        updateDto.setImportingCountryCode("US");
        updateDto.setExportingCountryCode("CN");

        // Add product category info
        ProductCategoriesDto productCategory = new ProductCategoriesDto();
        productCategory.setCategoryCode(123123);
        productCategory.setCategoryName("Test Category");
        productCategory.setDescription("Test Description");
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
    @DisplayName("POST /api/v1/tariff-rate/calculation returns TariffCalculatorResponseDto with tariffRate, tariffCost, and finalPrice")
    void post_calculate_returnsTariffCalculatorResponseDto() throws Exception {
        // Create mock entity with all required fields
        LocalDate date = LocalDate.of(2025, 1, 1);
        TariffRateEntity mockEntity = new TariffRateEntity();
        mockEntity.setTariffRate(new BigDecimal("0.1"));
        mockEntity.setRateUnit("ad valorem");

        // Mock getLowestActiveTariff to return the entity
        Mockito.when(tariffRateService.getLowestActiveTariff(
                        eq(1L), eq(2L), eq(123), eq(new BigDecimal("100.00")), eq(date)))
                .thenReturn(Optional.of(mockEntity));

        // Mock getFinalPrice
        Mockito.when(tariffRateService.getFinalPrice(
                        eq(1L), eq(2L), eq(123),
                        eq(new BigDecimal("100.00")), eq(new BigDecimal("1.0")), eq(date)))
                .thenReturn(new BigDecimal("110.00"));

        // Mock getTariffCost
        Mockito.when(tariffRateService.getTariffCost(
                        eq(new BigDecimal("110.00")), eq(new BigDecimal("100.00"))))
                .thenReturn(new BigDecimal("10.00"));

        String requestJson = "{\"importingCountryId\":1,\"exportingCountryId\":2,\"hsCode\":123,\"initialPrice\":100.00,\"quantity\":1.0,\"date\":\"2025-01-01\"}";

        mockMvc.perform(post("/api/v1/tariff-rate/calculation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tariffRate").value(0.1))
                .andExpect(jsonPath("$.finalPrice").value(110.00))
                .andExpect(jsonPath("$.tariffCost").value(10.00));
    }

    @Test
    @DisplayName("POST /api/v1/tariff-rate/calculation with zero tariff rate returns zero tariff cost")
    void post_calculate_zeroTariffRate_returnsZeroTariffCost() throws Exception {
        LocalDate date = LocalDate.of(2025, 1, 1);
        TariffRateEntity mockEntity = new TariffRateEntity();
        mockEntity.setTariffRate(BigDecimal.ZERO);
        mockEntity.setRateUnit("ad valorem");

        Mockito.when(tariffRateService.getLowestActiveTariff(
                        eq(1L), eq(2L), eq(123), eq(new BigDecimal("100.00")), eq(date)))
                .thenReturn(Optional.of(mockEntity));

        Mockito.when(tariffRateService.getFinalPrice(
                        eq(1L), eq(2L), eq(123),
                        eq(new BigDecimal("100.00")), eq(new BigDecimal("1.0")), eq(date)))
                .thenReturn(new BigDecimal("100.00"));

        Mockito.when(tariffRateService.getTariffCost(
                        eq(new BigDecimal("100.00")), eq(new BigDecimal("100.00"))))
                .thenReturn(BigDecimal.ZERO);

        String requestJson = "{\"importingCountryId\":1,\"exportingCountryId\":2,\"hsCode\":123,\"initialPrice\":100.00,\"quantity\":1.0,\"date\":\"2025-01-01\"}";

        mockMvc.perform(post("/api/v1/tariff-rate/calculation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tariffRate").value(0))
                .andExpect(jsonPath("$.tariffCost").value(0))
                .andExpect(jsonPath("$.finalPrice").value(100.00));
    }

    @Test
    @DisplayName("POST /api/v1/tariff-rate returns 400 for invalid input")
    void createTariffRate_returnsBadRequest_whenInputInvalid() throws Exception {
        CreateTariffRateDto invalidDto = new CreateTariffRateDto();
        // Missing required fields will trigger validation

        mockMvc.perform(post("/api/v1/tariff-rate")
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
    @DisplayName("POST /api/v1/tariff-rate returns 400 for invalid tariff rate")
    void createTariffRate_returnsBadRequest_whenTariffRateInvalid() throws Exception {
        CreateTariffRateDto invalidDto = new CreateTariffRateDto();
        invalidDto.setTariffRate(new BigDecimal("1000.0")); // Above maximum allowed value
        invalidDto.setTariffType("ad-valorem");
        invalidDto.setRateUnit("percent");
        invalidDto.setEffectiveDate(LocalDate.now());
        invalidDto.setImportingCountryCode("US");
        invalidDto.setExportingCountryCode("CN");
        invalidDto.setPreferentialTariff(false);
        invalidDto.setHsCode(123);

        mockMvc.perform(post("/api/v1/tariff-rate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.message").value("Data may be invalid. Please check your inputs."));
    }

    @Test
    @DisplayName("POST /api/v1/tariff-rate/calculation returns 400 for invalid input")
    void calculate_returnsBadRequest_whenInputInvalid() throws Exception {
        String invalidJson = "{\"hsCode\":123}"; // Missing required fields

        mockMvc.perform(post("/api/v1/tariff-rate/calculation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.message").value("Data may be invalid. Please check your inputs."));
    }

    @Test
    @DisplayName("POST /api/v1/tariff-rate/calculation returns 400 for negative price")
    void calculate_returnsBadRequest_whenPriceIsNegative() throws Exception {
        String requestJson = "{\"importingCountryId\":1,\"exportingCountryId\":2,\"hsCode\":123,\"initialPrice\":-100.00,\"quantity\":1.0\"date\":\"2025-01-01\"}";

        mockMvc.perform(post("/api/v1/tariff-rate/calculation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.message").value("Data may be invalid. Please check your inputs."));
    }
}
