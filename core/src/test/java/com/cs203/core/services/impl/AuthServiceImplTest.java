package com.cs203.core.services.impl;

import com.cs203.core.dto.requests.LoginRequestDTO;
import com.cs203.core.dto.responses.GenericResponseDTO;
import com.cs203.core.repository.UserRepository;
import com.cs203.core.service.impl.AuthServiceImpl;
import com.cs203.core.utils.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class AuthServiceImplTest {

    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private JwtUtil jwtUtil;
    @Mock
    private UserRepository userRepository;

    private AuthServiceImpl authService;

    @BeforeEach
    void setUp() {
        authService = new AuthServiceImpl(authenticationManager, jwtUtil, userRepository);
    }

    @Test
    void loginValidCredentials_ReturnsSuccessResponse() {
        // Arrange
        LoginRequestDTO loginRequest = new LoginRequestDTO("test@example.com", "password");

        // Act
        GenericResponseDTO response = authService.login(loginRequest);

        // Assert
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        assertTrue(response.success());
        assertNotNull(response.timestamp());
    }

}
