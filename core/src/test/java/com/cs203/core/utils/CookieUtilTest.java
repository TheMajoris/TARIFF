package com.cs203.core.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseCookie;

import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CookieUtilTest {

    private CookieUtil cookieUtil;

    @BeforeEach
    void setUp() {
        cookieUtil = new CookieUtil();

        // Manually set the properties since we aren't using Spring context here
        cookieUtil.httpsEnabled = true;
        cookieUtil.refreshTokenDurationInSeconds = 3600;
        cookieUtil.accessTokenDurationInSeconds = 1800;
    }

    @Test
    @DisplayName("Should build refresh token cookies correctly")
    void testBuildRefreshToken() {
        String value = "refresh123";
        List<ResponseCookie> cookies = cookieUtil.buildRefreshToken(value);

        assertEquals(2, cookies.size()); // one for each path

        for (ResponseCookie cookie : cookies) {
            assertEquals("refreshToken", cookie.getName());
            assertEquals(value, cookie.getValue());
            assertTrue(cookie.isHttpOnly());
            assertTrue(cookie.isSecure());
            assertTrue(cookie.getPath().equals("/api/v1/auth/refresh") ||
                    cookie.getPath().equals("/api/v1/auth/logout"));
            assertEquals(Duration.ofSeconds(3600), cookie.getMaxAge());
        }
    }

    @Test
    @DisplayName("Should build access token cookie correctly")
    void testBuildAccessToken() {
        String value = "access123";
        ResponseCookie cookie = cookieUtil.buildAccessToken(value);

        assertEquals("accessToken", cookie.getName());
        assertEquals(value, cookie.getValue());
        assertTrue(cookie.isHttpOnly());
        assertTrue(cookie.isSecure());
        assertEquals("/", cookie.getPath());
        assertEquals(Duration.ofSeconds(1800), cookie.getMaxAge());
    }

    @Test
    @DisplayName("Should build invalidated refresh token cookies correctly")
    void testBuildInvalidRefreshToken() {
        String value = "invalidRefresh";
        List<ResponseCookie> cookies = cookieUtil.buildInvalidRefreshToken(value);

        assertEquals(2, cookies.size());

        for (ResponseCookie cookie : cookies) {
            assertEquals("refreshToken", cookie.getName());
            assertEquals(value, cookie.getValue());
            assertEquals(Duration.ofSeconds(0), cookie.getMaxAge());
        }
    }

    @Test
    @DisplayName("Should build invalidated access token cookie correctly")
    void testBuildInvalidAccessToken() {
        String value = "invalidAccess";
        ResponseCookie cookie = cookieUtil.buildInvalidAccessToken(value);

        assertEquals("accessToken", cookie.getName());
        assertEquals(value, cookie.getValue());
        assertEquals(Duration.ofSeconds(0), cookie.getMaxAge());
    }
}
