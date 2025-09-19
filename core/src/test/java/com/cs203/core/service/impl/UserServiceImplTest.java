package com.cs203.core.service.impl;

import com.cs203.core.dto.requests.CreateUserRequestDTO;
import com.cs203.core.dto.responses.GenericResponseDTO;
import com.cs203.core.entity.UserEntity;
import com.cs203.core.repository.UserRepository;
import com.cs203.core.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private SecurityContext securityContext;
    @Mock
    private Authentication authentication;
    @Mock
    private Jwt jwt;

    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        userService = new UserServiceImpl(userRepository, passwordEncoder);
    }

    @Test
    void createUser_ValidRequest_ReturnsSuccessResponse() throws Exception {
        // Arr
        CreateUserRequestDTO request = new CreateUserRequestDTO(
                "Chimpanzini",
                "Bananini",
                "cb@example.com",
                "password",
                "Chimpanzini Bananini",
                false
        );
        SecurityContextHolder.setContext(securityContext);

        // Act
        GenericResponseDTO response = userService.createUser(request);

        // Assert
        verify(userRepository).save(any(UserEntity.class)); // verify interaction
        assertTrue(response.success());
        assertEquals("User creation successful.", response.message());
    }
}
