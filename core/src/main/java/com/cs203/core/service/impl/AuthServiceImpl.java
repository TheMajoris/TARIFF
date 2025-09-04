package com.cs203.core.service.impl;

import com.cs203.core.dto.responses.RefreshLoginResponseDTO;
import com.cs203.core.entity.User;
import com.cs203.core.entity.UserEntity;
import com.cs203.core.service.AuthService;
import com.cs203.core.utils.JwtUtil;
import com.nimbusds.jose.jwk.JWKSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {
    private Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;


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
