package com.cs203.core.services.impl;

import com.cs203.core.repository.TokenRepository;
import com.cs203.core.repository.UserRepository;
import com.cs203.core.service.impl.TokenServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

public class TokenServiceImplTest {

    @Mock
    private TokenRepository tokenRepository;

    @Mock
    private UserRepository userRepository;

    private TokenServiceImpl tokenService;

    @BeforeEach
    void setUp() {
        tokenService = new TokenServiceImpl(tokenRepository, userRepository);
    }
}
