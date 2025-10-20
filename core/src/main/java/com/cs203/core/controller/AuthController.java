package com.cs203.core.controller;

import com.cs203.core.dto.requests.LoginRequestDTO;
import com.cs203.core.dto.responses.GenericResponseDTO;
import com.cs203.core.dto.responses.RefreshLoginResponseDTO;
import com.cs203.core.entity.RefreshToken;
import com.cs203.core.entity.User;
import com.cs203.core.entity.UserEntity;
import com.cs203.core.exception.InvalidTokenException;
import com.cs203.core.service.AuthService;
import com.cs203.core.service.TokenService;
import com.cs203.core.utils.CookieUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Authentication")
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
    @Operation(summary = "Login with email and password")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successful login, sets access & refresh tokens in cookies",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = GenericResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Bad request - Email does not exist OR Password is wrong",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<GenericResponseDTO> login(
            @RequestBody @Valid LoginRequestDTO loginRequestDTO) {
        GenericResponseDTO response = authService.login(loginRequestDTO);
        RefreshLoginResponseDTO userInfo = (RefreshLoginResponseDTO) response.message();
        String refreshToken = tokenService.createRefreshToken(loginRequestDTO.email());

        List<ResponseCookie> refreshTokenCookies = cookieUtil.buildRefreshToken(refreshToken);
        ResponseCookie accessCookie = cookieUtil.buildAccessToken(userInfo.jwt());
        return ResponseEntity.ok()
                .headers(headers -> {
                    for (ResponseCookie cookie : refreshTokenCookies) {
                        headers.add(HttpHeaders.SET_COOKIE, cookie.toString());
                    }
                    headers.add(HttpHeaders.SET_COOKIE, accessCookie.toString());
                })
                .body(response);
    }

    @PostMapping("/logout")
    @Operation(summary = "Logout user")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully logged out",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = GenericResponseDTO.class)))
    })
    public ResponseEntity<GenericResponseDTO> logout(
            @CookieValue(value = "refreshToken", required = false) String refreshTokenId) {
        List<ResponseCookie> refreshTokenCookies = cookieUtil.buildInvalidRefreshToken(refreshTokenId);
        // Jwt jwt = (Jwt)
        // SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String accessToken = (String) SecurityContextHolder.getContext().getAuthentication().getCredentials();
        ResponseCookie accessCookie = cookieUtil.buildInvalidAccessToken(accessToken);

        return ResponseEntity.ok()
                .headers(headers -> {
                    for (ResponseCookie cookie : refreshTokenCookies) {
                        headers.add(HttpHeaders.SET_COOKIE, cookie.toString());
                    }
                    headers.add(HttpHeaders.SET_COOKIE, accessCookie.toString());
                })
                .body(tokenService.logout(UUID.fromString(refreshTokenId)));
    }

    @PostMapping("/refresh")
    @Operation(summary = "Refresh access token using refresh token")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully refreshed token",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = GenericResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Refresh token has expired, hence frontend sent null value",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<GenericResponseDTO> refresh(
            @CookieValue(value = "refreshToken", required = false) String tokenId) {

        // null because refresh token will auto delete from cookies when it expires
        if (tokenId == null) {
            throw new InvalidTokenException("Invalid refresh token, please log in again.");
        }

        RefreshToken refreshToken = tokenService.validateRefreshToken(
                UUID.fromString(tokenId));
        if (refreshToken == null) {
            throw new InvalidTokenException("Invalid refresh token, please log in again.");
        }

        UserEntity userEntity = refreshToken.getUser();
        String accessToken = authService.generateAccessToken(new User(userEntity));
        ResponseCookie accessCookie = cookieUtil.buildAccessToken(accessToken);

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, accessCookie.toString())
                .body(
                        new GenericResponseDTO(true,
                                authService.buildUserInformationResponse(userEntity),
                                ZonedDateTime.now()));
    }

    @GetMapping("/.well-known/jwks.json")
    @Operation(summary = "Retrieve JWKS (JSON Web Key Set)")
    @ApiResponse(responseCode = "200", description = "JWKS JSON",
            content = @Content(mediaType = "application/json", schema = @Schema(type = "string")))
    public String getJwkSet() {
        return authService.getJwkSet();
    }
}
