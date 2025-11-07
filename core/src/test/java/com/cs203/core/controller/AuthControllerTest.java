package com.cs203.core.controller;

import com.cs203.core.dto.requests.LoginRequestDTO;
import com.cs203.core.dto.responses.GenericResponseDTO;
import com.cs203.core.dto.responses.RefreshLoginResponseDTO;
import com.cs203.core.entity.RefreshToken;
import com.cs203.core.entity.UserEntity;
import com.cs203.core.exception.InvalidTokenException;
import com.cs203.core.service.AuthService;
import com.cs203.core.service.TokenService;
import com.cs203.core.utils.CookieUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private AuthService authService;
    @Mock
    private CookieUtil cookieUtil;
    @Mock
    private TokenService tokenService;

    @InjectMocks
    private AuthController authController;

    private LoginRequestDTO loginRequestDTO;
    private RefreshLoginResponseDTO refreshLoginResponseDTO;

    @BeforeEach
    void setUp() {
        loginRequestDTO = new LoginRequestDTO("test@example.com", "password123");
        refreshLoginResponseDTO = new RefreshLoginResponseDTO(
                "userid1", "John Doe", "NOT_ADMIN", "jwt-token");
    }

    // ---------- /login ----------

    @Test
    void login_shouldReturnOkResponseWithCookies() {
        // Arrange
        GenericResponseDTO response = new GenericResponseDTO(true, refreshLoginResponseDTO, ZonedDateTime.now());
        when(authService.login(loginRequestDTO)).thenReturn(response);
        when(tokenService.createRefreshToken(loginRequestDTO.email())).thenReturn("refresh-token-id");
        when(cookieUtil.buildRefreshToken("refresh-token-id"))
                .thenReturn(List.of(ResponseCookie.from("refreshToken", "refresh-token-id").build()));
        when(cookieUtil.buildAccessToken("jwt-token"))
                .thenReturn(ResponseCookie.from("accessToken", "jwt-token").build());

        // Act
        ResponseEntity<GenericResponseDTO> result = authController.login(loginRequestDTO);

        // Assert
        assertNotNull(result);
        assertEquals(200, result.getStatusCodeValue());
        assertTrue(result.getHeaders().containsKey(HttpHeaders.SET_COOKIE));
        verify(authService).login(loginRequestDTO);
        verify(tokenService).createRefreshToken(loginRequestDTO.email());
        verify(cookieUtil).buildRefreshToken("refresh-token-id");
        verify(cookieUtil).buildAccessToken("jwt-token");
        assertEquals(response, result.getBody());
    }

    // ---------- /logout ----------

    @Test
    void logout_shouldReturnOkResponseWithInvalidatedCookies() {
        // Arrange
        String refreshTokenId = UUID.randomUUID().toString();
        List<ResponseCookie> invalidRefreshCookies =
                List.of(ResponseCookie.from("refreshToken", "deleted").build());
        ResponseCookie invalidAccessCookie =
                ResponseCookie.from("accessToken", "deleted").build();

        when(cookieUtil.buildInvalidRefreshToken(refreshTokenId)).thenReturn(invalidRefreshCookies);
        when(cookieUtil.buildInvalidAccessToken("access-token")).thenReturn(invalidAccessCookie);

        GenericResponseDTO response = new GenericResponseDTO(true, "Logged out", ZonedDateTime.now());
        when(tokenService.logout(UUID.fromString(refreshTokenId))).thenReturn(response);

        // mock security context
        Authentication authentication = mock(Authentication.class);
        when(authentication.getCredentials()).thenReturn("access-token");
        SecurityContext context = mock(SecurityContext.class);
        when(context.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(context);

        // Act
        ResponseEntity<GenericResponseDTO> result = authController.logout(refreshTokenId);

        // Assert
        assertEquals(200, result.getStatusCodeValue());
        assertTrue(result.getHeaders().containsKey(HttpHeaders.SET_COOKIE));
        assertEquals(response, result.getBody());
        verify(cookieUtil).buildInvalidRefreshToken(refreshTokenId);
        verify(cookieUtil).buildInvalidAccessToken("access-token");
        verify(tokenService).logout(UUID.fromString(refreshTokenId));
    }

    // ---------- /refresh ----------

    @Test
    void refresh_shouldThrowInvalidTokenException_whenTokenIdIsNull() {
        assertThrows(InvalidTokenException.class, () -> authController.refresh(null));
    }

    @Test
    void refresh_shouldThrowInvalidTokenException_whenTokenNotFound() {
        UUID tokenId = UUID.randomUUID();
        when(tokenService.validateRefreshToken(tokenId)).thenReturn(null);

        assertThrows(InvalidTokenException.class, () -> authController.refresh(tokenId.toString()));
    }

    @Test
    void refresh_shouldReturnNewAccessToken_whenValid() {
        UUID tokenId = UUID.randomUUID();
        RefreshToken refreshToken = new RefreshToken();
        UserEntity userEntity = new UserEntity();
        refreshToken.setUser(userEntity);

        when(tokenService.validateRefreshToken(tokenId)).thenReturn(refreshToken);
        when(authService.generateAccessToken(any())).thenReturn("new-access-token");
        when(cookieUtil.buildAccessToken("new-access-token"))
                .thenReturn(ResponseCookie.from("accessToken", "new-access-token").build());
        GenericResponseDTO userInfoResponse = new GenericResponseDTO(true, "user-info", ZonedDateTime.now());

        // Act
        ResponseEntity<GenericResponseDTO> result = authController.refresh(tokenId.toString());

        // Assert
        assertEquals(200, result.getStatusCodeValue());
        assertTrue(result.getHeaders().containsKey(HttpHeaders.SET_COOKIE));
        assertNotNull(result.getBody());
        verify(tokenService).validateRefreshToken(tokenId);
        verify(authService).generateAccessToken(any());
        verify(cookieUtil).buildAccessToken("new-access-token");
    }

    // ---------- /.well-known/jwks.json ----------

    @Test
    void getJwkSet_shouldReturnStringFromAuthService() {
        when(authService.getJwkSet()).thenReturn("{\"keys\":[]}");

        String result = authController.getJwkSet();

        assertEquals("{\"keys\":[]}", result);
        verify(authService).getJwkSet();
    }
}
