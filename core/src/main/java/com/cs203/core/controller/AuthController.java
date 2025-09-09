package com.cs203.core.controller;

import com.cs203.core.dto.requests.LoginRequestDTO;
import com.cs203.core.dto.responses.GenericResponseDTO;
import com.cs203.core.service.AuthService;
import com.cs203.core.service.TokenService;
import com.cs203.core.utils.CookieUtil;
import jakarta.validation.Valid;
import org.antlr.v4.runtime.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthService authService;
    private final CookieUtil cookieUtil;
    private final TokenService tokenService;

    @Autowired
    public AuthController(AuthService authService,
                          CookieUtil cookieUtil,
                          TokenService tokenService) {
        this.authService = authService;
        this.cookieUtil = cookieUtil;
        this.tokenService = tokenService;
    }

    @PostMapping("/login")
    public ResponseEntity<GenericResponseDTO> login(
            @RequestBody @Valid LoginRequestDTO loginRequestDTO
            ) {
        return ResponseEntity.ok(authService.login(loginRequestDTO));
    }

    @PostMapping("/logout")
    public ResponseEntity<GenericResponseDTO> logout(
            @CookieValue(value="refreshToken", required=false) String refreshTokenId
    ) {
        List<ResponseCookie> refreshTokenCookies = cookieUtil.buildInvalidRefreshToken(refreshTokenId);
        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ResponseCookie accessCookie = cookieUtil.buildInvalidAccessToken(jwt.getTokenValue());

        return ResponseEntity.ok()
                .headers(headers -> {
                    for (ResponseCookie cookie : refreshTokenCookies) {
                        headers.add(HttpHeaders.SET_COOKIE, cookie.toString());
                    }
                    headers.add(HttpHeaders.SET_COOKIE, accessCookie.toString());
                })
                .body(tokenService.logout(UUID.fromString(refreshTokenId)));
    }

    // @PostMapping("/refresh")
    // public ResponseEntity<GenericResponseDTO> refresh(
    //         @CookieValue(value="refreshToken", required = false) String token
    //
    // )


}
