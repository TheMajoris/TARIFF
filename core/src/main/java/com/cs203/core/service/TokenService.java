package com.cs203.core.service;

import com.cs203.core.dto.responses.GenericResponseDTO;
import com.cs203.core.entity.RefreshToken;

import java.util.UUID;

public interface TokenService {
    String createRefreshToken(String email);

    RefreshToken validateRefreshToken(UUID refreshToken);

    GenericResponseDTO refreshToken(String refreshToken);
}
