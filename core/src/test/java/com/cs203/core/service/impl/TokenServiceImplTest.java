package com.cs203.core.service.impl;

import com.cs203.core.dto.responses.GenericResponseDTO;
import com.cs203.core.entity.RefreshToken;
import com.cs203.core.entity.UserEntity;
import com.cs203.core.repository.TokenRepository;
import com.cs203.core.repository.UserRepository;
import com.cs203.core.service.impl.TokenServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TokenServiceImplTest {

    @Mock
    private TokenRepository tokenRepository;
    @Mock
    private UserRepository userRepository;

    private TokenServiceImpl tokenService;

    @BeforeEach
    void setUp() {
        tokenService = new TokenServiceImpl(tokenRepository, userRepository);
    }

    @Test
    void createRefreshToken_ValidEmail_ReturnsToken() {
        // Arrange
        String email = "cb@example.com";
        UserEntity user = new UserEntity();
        user.setEmail(email);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(tokenRepository.save(any(RefreshToken.class))).thenAnswer(i -> i.getArguments()[0]);

        // Act
        String token = tokenService.createRefreshToken(email);

        // Assert
        assertNotNull(token);
        verify(tokenRepository).save(any(RefreshToken.class));
    }

    @Test
    void createRefreshToken_InvalidEmail_ThrowsException() {
        // Arrange
        String email = "nonexistent@example.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UsernameNotFoundException.class, () -> tokenService.createRefreshToken(email));
    }

    @Test
    void logout_ValidToken_ReturnsSuccessResponse() {
        // Arrange
        UUID token = UUID.randomUUID();
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setExpiresAt(OffsetDateTime.now().plusDays(1));

        when(tokenRepository.findByTokenAndExpiresAtAfter(eq(token), any()))
                .thenReturn(Optional.of(refreshToken));

        // Act
        GenericResponseDTO response = tokenService.logout(token);

        // Assert
        assertTrue(response.success());
        verify(tokenRepository).save(any(RefreshToken.class));
    }

}
