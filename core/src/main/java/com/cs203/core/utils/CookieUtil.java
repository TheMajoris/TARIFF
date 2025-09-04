package com.cs203.core.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CookieUtil {
  private final List<String> cookiePaths = List.of("/api/v1/auth/refresh", "/api/v1/auth/logout");

  @Value("${cookie.httpsEnabled}")
  private boolean httpsEnabled;

  @Value("${jwt.refresh.duration")
  private long refreshTokenDurationInSeconds;

  @Value("${jwt.refresh.duration}")
  private long accessTokenDurationInSeconds;

  public List<ResponseCookie> buildRefreshToken(String attributeValue) {
    List<ResponseCookie> cookies = new ArrayList<>();
    for (String path : cookiePaths) {
      cookies.add(
          ResponseCookie.from("refreshToken", attributeValue)
              .httpOnly(true)
              .secure(httpsEnabled)
              .path(path)
              .maxAge(refreshTokenDurationInSeconds)
              .build());
    }
    return cookies;
  }

  public ResponseCookie buildAccessToken(String attributeValue) {
    return ResponseCookie.from("accessToken", attributeValue)
        .httpOnly(true)
        .secure(httpsEnabled)
        .path("/")
        .maxAge(accessTokenDurationInSeconds)
        .build();
  }

  public List<ResponseCookie> buildInvalidRefreshToken(String attributeValue) {
    List<ResponseCookie> cookies = new ArrayList<>();
    for (String path : cookiePaths) {
      cookies.add(
          ResponseCookie.from("refreshToken", attributeValue)
              .httpOnly(true)
              .secure(httpsEnabled)
              .path(path)
              .maxAge(0)
              .build());
    }
    return cookies;
  }

  public ResponseCookie buildInvalidAccessToken(String attributeValue) {
      return ResponseCookie.from("accessToken", attributeValue)
              .httpOnly(true)
              .secure(httpsEnabled)
              .path("/")
              .maxAge(0)
              .build();
  }
}
