package com.cs203.core.service.impl;

import java.time.ZonedDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.cs203.core.dto.requests.CreateUserRequestDTO;
import com.cs203.core.dto.responses.GenericResponseDTO;
import com.cs203.core.entity.UserEntity;
import com.cs203.core.exception.DuplicateEmailException;
import com.cs203.core.mapper.UserEntityMapper;
import com.cs203.core.repository.UserRepository;
import com.cs203.core.service.UserService;

import jakarta.transaction.Transactional;

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
        // Check if email already exists
        if (userRepository.existsByEmail(createUserRequestDTO.email())) {
            throw new DuplicateEmailException("Email already exists: " + createUserRequestDTO.email());
        }
        
        String passwordHash = passwordEncoder.encode(createUserRequestDTO.password());
        UserEntity userEntity = UserEntityMapper.INSTANCE.createUserRequestDTOtoUserEntity(createUserRequestDTO);
        userEntity.setPasswordHash(passwordHash);
        userEntity.setEnabled(true);
        userRepository.save(userEntity);
        logger.info("User Entity created successfully for email: {}", createUserRequestDTO.email());

        return new GenericResponseDTO(
                true,
                "User creation successful.",
                ZonedDateTime.now()
        );
    }
}
