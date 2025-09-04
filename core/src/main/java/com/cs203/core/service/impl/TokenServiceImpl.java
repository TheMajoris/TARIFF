package com.cs203.core.service.impl;

import com.cs203.core.service.TokenService;
import org.springframework.beans.factory.annotation.Value;

public class TokenServiceImpl implements TokenService {
    @Value("${jwt.refresh.duration}")
    private long refreshTokenValidity;

    private final TokenRepository tokenRepository;
    private final UserRepository userRepository;
}
