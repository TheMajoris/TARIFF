package com.cs203.core.service.impl;

import com.cs203.core.dto.requests.LoginRequestDTO;
import com.cs203.core.dto.responses.GenericResponseDTO;
import com.cs203.core.entity.UserEntity;
import com.cs203.core.repository.UserRepository;
import com.cs203.core.utils.JwtUtil;
import com.nimbusds.jose.jwk.RSAKey;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceImplTest {

    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private JwtUtil jwtUtil;
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;

    private AuthServiceImpl authService;

    @BeforeEach
    void setUp() {
        authService = new AuthServiceImpl(authenticationManager, jwtUtil, userRepository);
    }

    @Test
    void loginValidCredentials_ReturnsSuccessResponse() {
        // Arrange
        LoginRequestDTO loginRequest = new LoginRequestDTO("cb@example.com", "password");

        UserEntity user = new UserEntity();
        user.setId(1L);

        when(userRepository.findByEmail("cb@example.com")).thenReturn(Optional.of(user));
        when(jwtUtil.generateToken(any(UserDetails.class))).thenReturn("fake-jwt-token");

        // Act
        GenericResponseDTO response = authService.login(loginRequest);

        // Assert
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        assertTrue(response.success());
        assertNotNull(response.timestamp());
    }

    @Test
    void getJwkSet_shouldReturnStringContainingRsaKeyInfo() throws NoSuchAlgorithmException {
        // Arrange
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(2048);
        KeyPair keyPair = keyGen.generateKeyPair();

        RSAKey rsaKey = new RSAKey.Builder((RSAPublicKey) keyPair.getPublic())
                .keyID("mock-key-id")
                .build();

        when(jwtUtil.getRsaKey()).thenReturn(rsaKey);

        // Act
        String jwkString = authService.getJwkSet();

        // Assert — not valid JSON, so just check content
        assertNotNull(jwkString);
        assertTrue(jwkString.contains("mock-key-id"));
        assertTrue(jwkString.contains("kty=RSA"));
        verify(jwtUtil, times(1)).getRsaKey();

        // Optional: sanity check it looks like a map-style string
        assertTrue(jwkString.startsWith("{"));
        assertTrue(jwkString.contains("keys=["));
    }
}