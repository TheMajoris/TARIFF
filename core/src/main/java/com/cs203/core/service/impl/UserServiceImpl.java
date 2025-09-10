package com.cs203.core.service.impl;

import com.cs203.core.dto.requests.CreateUserRequestDTO;
import com.cs203.core.dto.responses.GenericResponseDTO;
import com.cs203.core.entity.UserEntity;
import com.cs203.core.repository.UserRepository;
import com.cs203.core.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.persistence.Transient;
import jakarta.transaction.Transactional;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;

@Service
public class UserServiceImpl implements UserService {
    private final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public GenericResponseDTO createUser(CreateUserRequestDTO createUserRequestDTO) {
        String passwordHash = passwordEncoder.encode(createUserRequestDTO.password());
        UserEntity userEntity = new UserEntity(
                createUserRequestDTO.firstName(),
                createUserRequestDTO.email(),
                passwordHash
        );
        userEntity.setEnabled(true);
        userRepository.save(userEntity);
        logger.info("User Entity created successfully");

        return new GenericResponseDTO(
                true,
                "User creation successful.",
                ZonedDateTime.now()
        );
    }

}
