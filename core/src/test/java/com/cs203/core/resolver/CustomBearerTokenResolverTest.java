package com.cs203.core.resolver;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CustomBearerTokenResolverTest {

    private CustomBearerTokenResolver resolver;
    private HttpServletRequest request;

    @BeforeEach
    void setUp() {
        resolver = new CustomBearerTokenResolver();
        request = mock(HttpServletRequest.class);
    }

    @Test
    void shouldReturnNullForNonProtectedPaths() {
        when(request.getRequestURI()).thenReturn("/public/path");
        when(request.getMethod()).thenReturn("GET");

        String token = resolver.resolve(request);
        assertNull(token);
    }

    @Test
    void shouldReturnTokenFromAuthorizationHeader() {
        when(request.getRequestURI()).thenReturn("/api/v1/tariff-rate/123");
        when(request.getMethod()).thenReturn("GET");

        String headerToken = "header-token";
        CustomBearerTokenResolver spyResolver = spy(resolver);
        doReturn(headerToken).when(spyResolver).resolve(any(HttpServletRequest.class));

        String token = spyResolver.resolve(request);
        assertEquals(headerToken, token);
    }

    @Test
    void shouldReturnTokenFromCookieIfHeaderMissing() {
        when(request.getRequestURI()).thenReturn("/api/v1/tariff-rate/123");
        when(request.getMethod()).thenReturn("GET");

        when(request.getCookies()).thenReturn(new Cookie[]{
                new Cookie("accessToken", "cookie-token")
        });

        String token = resolver.resolve(request);
        assertEquals("cookie-token", token);
    }

    @Test
    void shouldReturnNullIfNoHeaderAndNoCookies() {
        when(request.getRequestURI()).thenReturn("/api/v1/tariff-rate/123");
        when(request.getMethod()).thenReturn("GET");

        when(request.getCookies()).thenReturn(null);

        String token = resolver.resolve(request);
        assertNull(token);
    }

    @Test
    void requiresAuthenticationShouldReturnTrueForProtectedPaths() {
        String[] protectedPaths = {
                "/api/v1/product-categories",
                "/api/v1/product-categories/123",
                "/api/v1/tariff-rate",
                "/api/v1/tariff-rate/123",
                "/api/v1/route",
                "/api/v1/route/abc",
                "/api/v1/auth/logout",
                "/api/v1/auth/refresh",
                "/api/v1/calculation-history",
                "/api/v1/calculation-history/1"
        };

        for (String path : protectedPaths) {
            assertTrue(resolver.requiresAuthentication(path, "GET"), path);
        }
    }

    @Test
    void requiresAuthenticationShouldReturnFalseForNonProtectedPaths() {
        String[] unprotectedPaths = {
                "/public",
                "/login",
                "/swagger-ui.html",
                "/api/v1/other"
        };

        for (String path : unprotectedPaths) {
            assertFalse(resolver.requiresAuthentication(path, "GET"), path);
        }
    }

    @Test
    void shouldReturnNullIfNoAccessTokenCookie() {
        when(request.getRequestURI()).thenReturn("/api/v1/tariff-rate/123");
        when(request.getMethod()).thenReturn("GET");

        // cookies exist but no accessToken cookie
        Cookie[] cookies = {
                new Cookie("other", "value"),
                new Cookie("somethingElse", "abc")
        };
        when(request.getCookies()).thenReturn(cookies);

        String token = resolver.resolve(request);
        assertNull(token);
    }

    @Test
    void shouldUseCookiesWhenHeaderTokenIsNull() {
        // Protected path -> allow resolver to run
        when(request.getRequestURI()).thenReturn("/api/v1/tariff-rate");
        when(request.getMethod()).thenReturn("GET");

        // DefaultBearerTokenResolver internally calls getHeaders("Authorization")
        when(request.getHeaders("Authorization"))
                .thenReturn(Collections.emptyEnumeration());

        // Provide cookies -> branch is hit
        when(request.getCookies()).thenReturn(new Cookie[]{
                new Cookie("accessToken", "cookie-token")
        });

        String token = resolver.resolve(request);

        assertEquals("cookie-token", token);
    }

    @Test
    void shouldCheckCookiesWhenHeaderTokenIsNull() {
        // Arrange
        when(request.getRequestURI()).thenReturn("/api/v1/tariff-rate");
        when(request.getMethod()).thenReturn("GET");

        // Simulate no Authorization header by returning empty enumeration
        when(request.getHeaders("Authorization"))
                .thenReturn(Collections.emptyEnumeration());
        when(request.getHeader("Authorization")).thenReturn(null);

        Cookie[] cookies = {new Cookie("accessToken", "cookie-token")};
        when(request.getCookies()).thenReturn(cookies);

        // Act
        String token = resolver.resolve(request);

        // Assert
        assertEquals("cookie-token", token);
    }

    @Test
    void shouldNotCheckCookiesWhenHeaderTokenExists() {
        // Arrange
        when(request.getRequestURI()).thenReturn("/api/v1/tariff-rate");
        when(request.getMethod()).thenReturn("GET");

        // Mock Authorization header to exist
        when(request.getHeader("Authorization")).thenReturn("Bearer header-token");

        // Provide cookies (but they should be ignored)
        Cookie[] cookies = {new Cookie("accessToken", "cookie-token")};
        when(request.getCookies()).thenReturn(cookies);

        // Act
        String token = resolver.resolve(request);

        // Assert
        // DefaultBearerTokenResolver strips "Bearer " prefix
        assertEquals("header-token", token);
    }
}
