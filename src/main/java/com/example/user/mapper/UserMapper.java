package com.example.user.mapper;

import com.example.user.model.UserCreateUpdateDto;
import com.example.user.model.UserDto;
import com.example.user.model.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toUserDto(UserEntity user);

    @Mapping(ignore = true, target = "id")
    UserEntity toUserEntity(UserCreateUpdateDto user);

    UserEntity toUserEntity(UserCreateUpdateDto user, Long id);
}
