package com.cs203.core.mapper;

import com.cs203.core.dto.requests.CreateUserRequestDTO;
import com.cs203.core.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.factory.Mappers;


@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserEntityMapper {
    UserEntityMapper INSTANCE = Mappers.getMapper(UserEntityMapper.class);

    UserEntity createUserRequestDTOtoUserEntity(CreateUserRequestDTO createUserRequestDTO);
}
