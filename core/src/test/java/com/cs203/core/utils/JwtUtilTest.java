package com.cs203.core.utils;

import com.cs203.core.exception.JwtCreationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;

import java.time.Instant;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
        import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class JwtUtilTest {

    private JwtEncoder jwtEncoder;
    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        jwtEncoder = mock(JwtEncoder.class);
        // RSAKey is unused in generateToken, can pass null
        jwtUtil = new JwtUtil(jwtEncoder, null);

        // Manually set properties
        jwtUtil.expirationTime = 3600L;
        jwtUtil.issuer = "test-issuer";
    }

    @Test
    @DisplayName("Should generate token successfully")
    void testGenerateTokenSuccess() {
        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("testuser");

        Jwt mockJwt = mock(Jwt.class);
        when(mockJwt.getTokenValue()).thenReturn("mock-token");

        // Mock encoder
        when(jwtEncoder.encode(any(JwtEncoderParameters.class))).thenReturn(mockJwt);

        String token = jwtUtil.generateToken(userDetails);

        assertEquals("mock-token", token);
        verify(jwtEncoder, times(1)).encode(any(JwtEncoderParameters.class));
    }

    @Test
    @DisplayName("Should throw JwtCreationException if encoding fails")
    void testGenerateTokenThrowsException() {
        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("testuser");
        when(userDetails.getAuthorities()).thenReturn(List.of());

        // Simulate encoder throwing exception
        when(jwtEncoder.encode(any(JwtEncoderParameters.class))).thenThrow(new RuntimeException("fail"));

        JwtCreationException ex = assertThrows(JwtCreationException.class, () -> jwtUtil.generateToken(userDetails));
        assertTrue(ex.getMessage().contains("Wthelly sumn wrong!"));
    }
}
