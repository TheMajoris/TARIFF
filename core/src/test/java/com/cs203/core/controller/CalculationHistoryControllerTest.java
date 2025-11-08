
package com.cs203.core.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.cs203.core.dto.GenericResponse;
import com.cs203.core.dto.SavedCalculationDto;
import com.cs203.core.dto.requests.SaveCalculationRequestDTO;
import com.cs203.core.entity.UserEntity;
import com.cs203.core.exception.GlobalExceptionHandler;
import com.cs203.core.service.CalculationHistoryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

class CalculationHistoryControllerTest {
        private MockMvc mockMvc;
        private CalculationHistoryService calculationHistoryService;
        private CalculationHistoryController controller;
        private ObjectMapper objectMapper;
        private Authentication mockAuthentication;
        private UserEntity mockUser;

        @BeforeEach
        void setUp() throws Exception {
                calculationHistoryService = Mockito.mock(CalculationHistoryService.class);
                controller = new CalculationHistoryController();
                objectMapper = new ObjectMapper();
                objectMapper.findAndRegisterModules();

                // Set up validator
                LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
                validator.afterPropertiesSet();

                // Create mock authentication and user
                mockAuthentication = Mockito.mock(Authentication.class);
                mockUser = new UserEntity();
                mockUser.setId(1L);
                mockUser.setFirstName("John");
                mockUser.setLastName("Doe");
                mockUser.setEmail("john.doe@example.com");

                Mockito.when(mockAuthentication.getPrincipal()).thenReturn(mockUser);

                // Inject mocked service into controller
                Field f = CalculationHistoryController.class.getDeclaredField("calculationHistoryService");
                f.setAccessible(true);
                f.set(controller, calculationHistoryService);

                // Configure MockMvc with exception handling and validation support
                mockMvc = MockMvcBuilders.standaloneSetup(controller)
                                .setControllerAdvice(new GlobalExceptionHandler())
                                .setValidator(validator)
                                .build();
        }

        @Test
        @DisplayName("POST /api/v1/calculation-history saves ad valorem calculation successfully")
        void saveCalculation_ad_valorem_returnsCreatedResponse() throws Exception {
                // Create test request
                SaveCalculationRequestDTO requestDto = new SaveCalculationRequestDTO(
                                "Test Calculation",
                                new BigDecimal("1000.00"),
                                new BigDecimal("1000.00"),
                                "USD",
                                new BigDecimal("0.10"),
                                "ad-valorem",
                                null,
                                null,
                                new BigDecimal("100.00"),
                                new BigDecimal("1100.00"),
                                "Test notes",
                                "US",
                                "CN",
                                123456);

                // Create expected response
                SavedCalculationDto savedDto = new SavedCalculationDto();
                savedDto.setId(1L);
                savedDto.setCalculationName("Test Calculation");
                savedDto.setProductValue(new BigDecimal("1000.00"));
                savedDto.setProductQuantity(new BigDecimal("1000.00"));
                savedDto.setCurrencyCode("USD");
                savedDto.setTariffRate(new BigDecimal("0.10"));
                savedDto.setTariffType("ad-valorem");
                savedDto.setCalculatedTariffCost(new BigDecimal("100.00"));
                savedDto.setTotalCost(new BigDecimal("1100.00"));
                savedDto.setNotes("Test notes");
                savedDto.setCreatedAt(LocalDateTime.now());
                savedDto.setUpdatedAt(LocalDateTime.now());
                savedDto.setUserId(1L);
                savedDto.setImportingCountryCode("US");
                savedDto.setExportingCountryCode("CN");
                savedDto.setProductCategoryCode(123456);

                GenericResponse<SavedCalculationDto> response = new GenericResponse<>(
                                HttpStatus.CREATED,
                                "Calculation saved successfully",
                                savedDto);

                Mockito.when(calculationHistoryService.saveCalculation(any(SaveCalculationRequestDTO.class), eq(1L)))
                                .thenReturn(response);

                mockMvc.perform(post("/api/v1/calculation-history")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(requestDto))
                                .principal(mockAuthentication))
                                .andExpect(status().isCreated())
                                .andExpect(jsonPath("$.data.id").value(1))
                                .andExpect(jsonPath("$.data.calculationName").value("Test Calculation"))
                                .andExpect(jsonPath("$.data.productValue").value(1000.00))
                                .andExpect(jsonPath("$.data.productQuantity").value(1000.00))
                                .andExpect(jsonPath("$.data.currencyCode").value("USD"))
                                .andExpect(jsonPath("$.data.tariffRate").value(0.10))
                                .andExpect(jsonPath("$.data.tariffType").value("ad-valorem"))
                                .andExpect(jsonPath("$.data.calculatedTariffCost").value(100.00))
                                .andExpect(jsonPath("$.data.totalCost").value(1100.00))
                                .andExpect(jsonPath("$.data.notes").value("Test notes"))
                                .andExpect(jsonPath("$.data.userId").value(1))
                                .andExpect(jsonPath("$.data.importingCountryCode").value("US"))
                                .andExpect(jsonPath("$.data.exportingCountryCode").value("CN"))
                                .andExpect(jsonPath("$.data.productCategoryCode").value(123456))
                                .andExpect(jsonPath("$.message").value("Calculation saved successfully"));
        }

        @Test
        @DisplayName("POST /api/v1/calculation-history saves specific calculation successfully")
        void saveCalculation_specific_returnsCreatedResponse() throws Exception {
                // Create test request
                SaveCalculationRequestDTO requestDto = new SaveCalculationRequestDTO(
                                "Test Calculation",
                                new BigDecimal("1000.00"),
                                new BigDecimal("1000.00"),
                                "USD",
                                new BigDecimal("0.10"),
                                "specific",
                                new BigDecimal("1"),
                                "kg",
                                new BigDecimal("100.00"),
                                new BigDecimal("1100.00"),
                                "Test notes",
                                "US",
                                "CN",
                                123456);

                // Create expected response
                SavedCalculationDto savedDto = new SavedCalculationDto();
                savedDto.setId(1L);
                savedDto.setCalculationName("Test Calculation");
                savedDto.setProductValue(new BigDecimal("1000.00"));
                savedDto.setProductQuantity(new BigDecimal("1000.00"));
                savedDto.setCurrencyCode("USD");
                savedDto.setTariffRate(new BigDecimal("0.10"));
                savedDto.setTariffType("specific");
                savedDto.setUnitQuantity(new BigDecimal(1));
                savedDto.setRateUnit("kg");
                savedDto.setCalculatedTariffCost(new BigDecimal("100.00"));
                savedDto.setTotalCost(new BigDecimal("1100.00"));
                savedDto.setNotes("Test notes");
                savedDto.setCreatedAt(LocalDateTime.now());
                savedDto.setUpdatedAt(LocalDateTime.now());
                savedDto.setUserId(1L);
                savedDto.setImportingCountryCode("US");
                savedDto.setExportingCountryCode("CN");
                savedDto.setProductCategoryCode(123456);

                GenericResponse<SavedCalculationDto> response = new GenericResponse<>(
                                HttpStatus.CREATED,
                                "Calculation saved successfully",
                                savedDto);

                Mockito.when(calculationHistoryService.saveCalculation(any(SaveCalculationRequestDTO.class), eq(1L)))
                                .thenReturn(response);

                mockMvc.perform(post("/api/v1/calculation-history")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(requestDto))
                                .principal(mockAuthentication))
                                .andExpect(status().isCreated())
                                .andExpect(jsonPath("$.data.id").value(1))
                                .andExpect(jsonPath("$.data.calculationName").value("Test Calculation"))
                                .andExpect(jsonPath("$.data.productValue").value(1000.00))
                                .andExpect(jsonPath("$.data.productQuantity").value(1000.00))
                                .andExpect(jsonPath("$.data.currencyCode").value("USD"))
                                .andExpect(jsonPath("$.data.tariffRate").value(0.10))
                                .andExpect(jsonPath("$.data.tariffType").value("specific"))
                                .andExpect(jsonPath("$.data.unitQuantity").value(1))
                                .andExpect(jsonPath("$.data.rateUnit").value("kg"))
                                .andExpect(jsonPath("$.data.calculatedTariffCost").value(100.00))
                                .andExpect(jsonPath("$.data.totalCost").value(1100.00))
                                .andExpect(jsonPath("$.data.notes").value("Test notes"))
                                .andExpect(jsonPath("$.data.userId").value(1))
                                .andExpect(jsonPath("$.data.importingCountryCode").value("US"))
                                .andExpect(jsonPath("$.data.exportingCountryCode").value("CN"))
                                .andExpect(jsonPath("$.data.productCategoryCode").value(123456))
                                .andExpect(jsonPath("$.message").value("Calculation saved successfully"));
        }

        @Test
        @DisplayName("GET /api/v1/calculation-history returns user's calculations")
        void getCalculationsByUserId_returnsListOfCalculations() throws Exception {
                // Create test data
                SavedCalculationDto calculation1 = new SavedCalculationDto();
                calculation1.setId(1L);
                calculation1.setCalculationName("Calculation 1");
                calculation1.setProductValue(new BigDecimal("1000.00"));
                calculation1.setProductQuantity(new BigDecimal("1000.00"));
                calculation1.setCurrencyCode("USD");
                calculation1.setTariffRate(new BigDecimal("0.10"));
                calculation1.setTariffType("ad-valorem");
                calculation1.setUnitQuantity(null);
                calculation1.setRateUnit(null);
                calculation1.setCalculatedTariffCost(new BigDecimal("100.00"));
                calculation1.setTotalCost(new BigDecimal("1100.00"));
                calculation1.setNotes("Notes 1");
                calculation1.setCreatedAt(LocalDateTime.now());
                calculation1.setUpdatedAt(LocalDateTime.now());
                calculation1.setUserId(1L);
                calculation1.setImportingCountryCode("US");
                calculation1.setExportingCountryCode("CN");
                calculation1.setProductCategoryCode(123455);

                SavedCalculationDto calculation2 = new SavedCalculationDto();
                calculation2.setId(2L);
                calculation2.setCalculationName("Calculation 2");
                calculation2.setProductValue(new BigDecimal("2000.00"));
                calculation2.setProductQuantity(new BigDecimal("2000.00"));
                calculation2.setCurrencyCode("EUR");
                calculation2.setTariffRate(new BigDecimal("0.15"));
                calculation2.setTariffType("specific");
                calculation1.setUnitQuantity(new BigDecimal("1"));
                calculation1.setRateUnit("kg");
                calculation2.setCalculatedTariffCost(new BigDecimal("300.00"));
                calculation2.setTotalCost(new BigDecimal("2300.00"));
                calculation2.setNotes("Notes 2");
                calculation2.setCreatedAt(LocalDateTime.now());
                calculation2.setUpdatedAt(LocalDateTime.now());
                calculation2.setUserId(1L);
                calculation2.setImportingCountryCode("DE");
                calculation2.setExportingCountryCode("FR");
                calculation2.setProductCategoryCode(123456);

                List<SavedCalculationDto> calculations = Arrays.asList(calculation1, calculation2);

                GenericResponse<List<SavedCalculationDto>> response = new GenericResponse<>(
                                HttpStatus.OK,
                                "Calculations retrieved successfully",
                                calculations);

                Mockito.when(calculationHistoryService.getCalculationsByUserId(1L)).thenReturn(response);

                mockMvc.perform(get("/api/v1/calculation-history")
                                .contentType(MediaType.APPLICATION_JSON)
                                .principal(mockAuthentication))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.data").isArray())
                                .andExpect(jsonPath("$.data.length()").value(2))
                                .andExpect(jsonPath("$.data[0].id").value(1))
                                .andExpect(jsonPath("$.data[0].calculationName").value("Calculation 1"))
                                .andExpect(jsonPath("$.data[0].productValue").value(1000.00))
                                .andExpect(jsonPath("$.data[0].productQuantity").value(1000.00))
                                .andExpect(jsonPath("$.data[0].currencyCode").value("USD"))
                                .andExpect(jsonPath("$.data[1].id").value(2))
                                .andExpect(jsonPath("$.data[1].calculationName").value("Calculation 2"))
                                .andExpect(jsonPath("$.data[1].productValue").value(2000.00))
                                .andExpect(jsonPath("$.data[1].productQuantity").value(2000.00))
                                .andExpect(jsonPath("$.data[1].currencyCode").value("EUR"))
                                .andExpect(jsonPath("$.message").value("Calculations retrieved successfully"));
        }

        @Test
        @DisplayName("GET /api/v1/calculation-history returns empty list when no calculations exist")
        void getCalculationsByUserId_returnsEmptyList_whenNoCalculationsExist() throws Exception {
                List<SavedCalculationDto> emptyList = Arrays.asList();

                GenericResponse<List<SavedCalculationDto>> response = new GenericResponse<>(
                                HttpStatus.OK,
                                "No calculations found",
                                emptyList);

                Mockito.when(calculationHistoryService.getCalculationsByUserId(1L)).thenReturn(response);

                mockMvc.perform(get("/api/v1/calculation-history")
                                .contentType(MediaType.APPLICATION_JSON)
                                .principal(mockAuthentication))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.data").isArray())
                                .andExpect(jsonPath("$.data.length()").value(0))
                                .andExpect(jsonPath("$.message").value("No calculations found"));
        }

        @Test
        @DisplayName("POST /api/v1/calculation-history returns 400 for invalid input")
        void saveCalculation_returnsBadRequest_whenInputInvalid() throws Exception {
                // Test with missing required fields
                String invalidJson = "{}";

                mockMvc.perform(post("/api/v1/calculation-history")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(invalidJson)
                                .principal(mockAuthentication))
                                .andExpect(status().isBadRequest())
                                .andExpect(jsonPath("$.message").exists());
        }

        @Test
        @DisplayName("POST - Returns 400 when calculationName is blank")
        void saveCalculation_withBlankName_returnsBadRequest() throws Exception {
                SaveCalculationRequestDTO requestDto = new SaveCalculationRequestDTO(
                                "", // blank name
                                new BigDecimal("1000.00"),
                                new BigDecimal("1000.00"),
                                "USD",
                                new BigDecimal("0.10"),
                                "ad-valorem",
                                null,
                                null,
                                new BigDecimal("100.00"),
                                new BigDecimal("1100.00"),
                                "Test notes",
                                "US",
                                "CN",
                                123456);

                mockMvc.perform(post("/api/v1/calculation-history")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(requestDto))
                                .principal(mockAuthentication))
                                .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("POST - Returns 400 when productValue is null")
        void saveCalculation_withNullProductValue_returnsBadRequest() throws Exception {
                SaveCalculationRequestDTO requestDto = new SaveCalculationRequestDTO(
                                "Test Calculation",
                                null, // null value
                                new BigDecimal("1000.00"),
                                "USD",
                                new BigDecimal("0.10"),
                                "ad-valorem",
                                null,
                                null,
                                new BigDecimal("100.00"),
                                new BigDecimal("1100.00"),
                                "Test notes",
                                "US",
                                "CN",
                                123456);

                mockMvc.perform(post("/api/v1/calculation-history")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(requestDto))
                                .principal(mockAuthentication))
                                .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("POST - Returns 400 when productQuantity is null")
        void saveCalculation_withNullProductQuantity_returnsBadRequest() throws Exception {
                SaveCalculationRequestDTO requestDto = new SaveCalculationRequestDTO(
                                "Test Calculation",
                                new BigDecimal("1000.00"),
                                null, // null value
                                "USD",
                                new BigDecimal("0.10"),
                                "ad-valorem",
                                null,
                                null,
                                new BigDecimal("100.00"),
                                new BigDecimal("1100.00"),
                                "Test notes",
                                "US",
                                "CN",
                                123456);

                mockMvc.perform(post("/api/v1/calculation-history")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(requestDto))
                                .principal(mockAuthentication))
                                .andExpect(status().isBadRequest());
        }
}
