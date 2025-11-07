package com.cs203.core.controller;

import com.cs203.core.dto.requests.CreateUserRequestDTO;
import com.cs203.core.dto.responses.GenericResponseDTO;
import com.cs203.core.service.impl.UserServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.ZonedDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserServiceImpl userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("POST /api/v1/users creates a new user successfully")
    void createUser_validRequest_returnsCreated() throws Exception {
        // Arrange
        CreateUserRequestDTO request = new CreateUserRequestDTO(
                "John",
                "Doe",
                "john.doe@example.com",
                "SecurePass123!",
                "johndoe",
                false
        );

        ZonedDateTime time = ZonedDateTime.now();

        GenericResponseDTO response = new GenericResponseDTO(
                true,
                "User created successfully",
                time
        );

        when(userService.createUser(any(CreateUserRequestDTO.class))).thenReturn(response);

        // Act & Assert
        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("User created successfully"))
                .andExpect(jsonPath("$.success").value(true));

        verify(userService, times(1)).createUser(any(CreateUserRequestDTO.class));
    }

    @Test
    @DisplayName("POST /api/v1/users with null firstName returns 400")
    void createUser_nullFirstName_returnsBadRequest() throws Exception {
        // Arrange
        String requestJson = """
                {
                    "firstName": null,
                    "lastName": "Doe",
                    "email": "john.doe@example.com",
                    "password": "SecurePass123!",
                    "username": "johndoe",
                    "isAdmin": false
                }
                """;

        // Act & Assert
        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest());

        verify(userService, never()).createUser(any(CreateUserRequestDTO.class));
    }

    @Test
    @DisplayName("POST /api/v1/users with empty firstName returns 400")
    void createUser_emptyFirstName_returnsBadRequest() throws Exception {
        // Arrange
        CreateUserRequestDTO request = new CreateUserRequestDTO(
                "",  // Empty first name
                "Doe",
                "john.doe@example.com",
                "SecurePass123!",
                "johndoe",
                false
        );

        // Act & Assert
        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        verify(userService, never()).createUser(any(CreateUserRequestDTO.class));
    }

    @Test
    @DisplayName("POST /api/v1/users with empty email returns 400")
    void createUser_emptyEmail_returnsBadRequest() throws Exception {
        // Arrange
        CreateUserRequestDTO request = new CreateUserRequestDTO(
                "John",
                "Doe",
                "",  // Empty email
                "SecurePass123!",
                "johndoe",
                false
        );

        // Act & Assert
        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        verify(userService, never()).createUser(any(CreateUserRequestDTO.class));
    }

    @Test
    @DisplayName("POST /api/v1/users with all null fields returns 400")
    void createUser_allNullFields_returnsBadRequest() throws Exception {
        // Arrange
        String requestJson = """
                {
                    "firstName": null,
                    "lastName": null,
                    "email": null,
                    "password": null,
                    "username": null,
                    "isAdmin": false
                }
                """;

        // Act & Assert
        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest());

        verify(userService, never()).createUser(any(CreateUserRequestDTO.class));
    }

    @Test
    @DisplayName("POST /api/v1/users creates admin user successfully")
    void createUser_adminUser_returnsCreated() throws Exception {
        // Arrange
        CreateUserRequestDTO request = new CreateUserRequestDTO(
                "Admin",
                "User",
                "admin@example.com",
                "AdminPass123!",
                "adminuser",
                true  // Admin user
        );

        GenericResponseDTO response = new GenericResponseDTO(
                true,
                "Admin user created successfully",
                ZonedDateTime.now()
        );

        when(userService.createUser(any(CreateUserRequestDTO.class))).thenReturn(response);

        // Act & Assert
        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Admin user created successfully"))
                .andExpect(jsonPath("$.success").value(true));

        verify(userService, times(1)).createUser(any(CreateUserRequestDTO.class));
    }
}