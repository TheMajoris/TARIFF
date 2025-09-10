package com.cs203.core.service;

import com.cs203.core.dto.requests.CreateUserRequestDTO;
import com.cs203.core.dto.responses.GenericResponseDTO;

public interface UserService {
    GenericResponseDTO createUser(CreateUserRequestDTO createUserRequestDTO);
}
