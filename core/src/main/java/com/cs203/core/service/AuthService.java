package com.cs203.core.service;

import com.cs203.core.dto.requests.LoginRequestDTO;
import com.cs203.core.dto.responses.GenericResponseDTO;
import com.cs203.core.dto.responses.RefreshLoginResponseDTO;
import org.springframework.security.core.userdetails.UserDetails;
import com.cs203.core.entity.UserEntity;

public interface AuthService {
  GenericResponseDTO login(LoginRequestDTO loginRequestDTO);

  String generateAccessToken(UserDetails userDetails);

  String getJwkSet();

  RefreshLoginResponseDTO buildUserInformationResponse(UserEntity userEntity);
}
