package com.cs203.core.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.cs203.core.dto.requests.CreateUserRequestDTO;
import com.cs203.core.entity.UserEntity;
import com.cs203.core.exception.DuplicateEmailException;
import com.cs203.core.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserService Duplicate Email Tests")
class UserServiceImplDuplicateEmailTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    private CreateUserRequestDTO createUserRequestDTO;

    @BeforeEach
    void setUp() {
        createUserRequestDTO = new CreateUserRequestDTO(
                "John",
                "Doe",
                "john@example.com",
                "password123",
                "johndoe",
                false
        );
    }

    @Test
    @DisplayName("Should throw DuplicateEmailException when email already exists")
    void createUser_EmailAlreadyExists_ThrowsDuplicateEmailException() {
        // Arrange
        when(userRepository.existsByEmail("john@example.com")).thenReturn(true);

        // Act & Assert
        DuplicateEmailException exception = assertThrows(
                DuplicateEmailException.class,
                () -> userService.createUser(createUserRequestDTO)
        );

        assertEquals("Email already exists: john@example.com", exception.getMessage());
        verify(userRepository, never()).save(any(UserEntity.class));
    }

    @Test
    @DisplayName("Should create user successfully when email does not exist")
    void createUser_EmailDoesNotExist_CreatesUserSuccessfully() {
        // Arrange
        when(userRepository.existsByEmail("john@example.com")).thenReturn(false);
        when(passwordEncoder.encode("password123")).thenReturn("hashedpassword");

        // Act
        var result = userService.createUser(createUserRequestDTO);

        // Assert
        assertNotNull(result);
        assertTrue(result.success());
        assertEquals("User creation successful.", result.message());
        verify(userRepository).save(any(UserEntity.class));
    }
}
