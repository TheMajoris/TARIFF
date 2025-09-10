package com.cs203.core.service.impl;

import com.cs203.core.dto.requests.LoginRequestDTO;
import com.cs203.core.dto.responses.GenericResponseDTO;
import com.cs203.core.dto.responses.RefreshLoginResponseDTO;
import com.cs203.core.entity.User;
import com.cs203.core.entity.UserEntity;
import com.cs203.core.exception.InvalidUserCredentials;
import com.cs203.core.repository.UserRepository;
import com.cs203.core.service.AuthService;
import com.cs203.core.utils.JwtUtil;
import com.nimbusds.jose.jwk.JWKSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;

@Service
public class AuthServiceImpl implements AuthService {
    private Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    @Autowired
    public AuthServiceImpl(
            AuthenticationManager authenticationManager,
            JwtUtil jwtUtil,
            UserRepository userRepository
    ) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
    }

    @Override
    public GenericResponseDTO login(LoginRequestDTO loginRequestDTO) {
        String userEmail = loginRequestDTO.email();

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userEmail, loginRequestDTO.password())
        );

        UserEntity userEntity = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new InvalidUserCredentials("User does not exist"));

        logger.info("{} login successful", userEmail);

        return new GenericResponseDTO(
                true,
                this.buildUserInformationResponse(userEntity),
                ZonedDateTime.now()
        );
    }

    @Override
    public String generateAccessToken(UserDetails userDetails) {
        return jwtUtil.generateToken(userDetails);
    }

    @Override
    public String getJwkSet() {
        return new JWKSet(jwtUtil.getRsaKey()).toJSONObject().toString();
    }

    public RefreshLoginResponseDTO buildUserInformationResponse(UserEntity userEntity) {
        return new RefreshLoginResponseDTO(
                userEntity.getId().toString(),
                userEntity.getFirstName() + " " + userEntity.getLastName(),
                userEntity.getUserRole().getAuthority(),
                this.generateAccessToken(new User(userEntity))
        );
    }
}
