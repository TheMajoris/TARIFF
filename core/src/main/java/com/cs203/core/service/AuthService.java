package com.cs203.core.service;

import com.cs203.core.dto.responses.GenericResponseDTO;

public interface AuthService {
    GenericResponseDTOseDTO login(LoginRequestDTO loginRequestDTO);
}
