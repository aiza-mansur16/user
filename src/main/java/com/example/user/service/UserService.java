package com.example.user.service;

import com.example.user.mapper.UserMapper;
import com.example.user.model.UserCreateUpdateDto;
import com.example.user.model.UserDto;
import com.example.user.model.UserEntity;
import com.example.user.model.UserQueryDto;
import com.example.user.repository.UserRepository;
import com.example.user.utils.model.Page;
import com.example.user.utils.model.ResponseEnvelope;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class UserService {

    private final UserRepository repository;
    private final UserMapper mapper;

    public UserService(UserRepository repository, UserMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public UserDto register(UserCreateUpdateDto user) {
        try {
            if (log.isDebugEnabled()) {
                log.debug("Creating user with username:{}", user.userName());
            }
            var savedUser = repository.saveAndFlush(mapper.toUserEntity(user));
            return mapper.toUserDto(savedUser);
        } catch (DataIntegrityViolationException e) {
            if (log.isWarnEnabled()) {
                log.warn("Error:{} while creating user", e.getMessage());
            }
            throw new IllegalArgumentException("User already exists with given email/username.", e);
        }
    }

    public ResponseEnvelope<List<UserDto>> findAll(UserQueryDto userQueryDto) {
        if (log.isDebugEnabled()) {
            log.debug("Retrieving users with query: {}", userQueryDto);
        }
        var result = repository.findAll(getExampleUserEntity(userQueryDto),
                PageRequest.of(userQueryDto.page(), userQueryDto.size()));
        if (log.isDebugEnabled()) {
            log.debug("Users successfully retrieved");
        }
        return new ResponseEnvelope<>(
                result.stream().map(mapper::toUserDto).toList(),
                null,
                new Page(userQueryDto.page(), userQueryDto.size(), result.getTotalElements(), result.getTotalPages()));
    }

    public UserDto updateUser(Long id, UserCreateUpdateDto user) {
        if (log.isDebugEnabled()) {
            log.debug("Retrieving user with id: {} to update", id);
        }
        repository.getReferenceById(id);
        try {
            if (log.isDebugEnabled()) {
                log.debug("Updating user with id: {}", id);
            }
            return mapper.toUserDto(repository.saveAndFlush(mapper.toUserEntity(user, id)));
        } catch (DataIntegrityViolationException e) {
            if (log.isWarnEnabled()) {
                log.warn("Error:{} while updating user", e.getMessage());
            }
            throw new IllegalArgumentException("User already exists with given email/username.", e);
        }
    }

    private Example<UserEntity> getExampleUserEntity(UserQueryDto userQueryDto) {
        return Example.of(UserEntity
                .builder()
                .id(userQueryDto.id())
                .email(userQueryDto.email())
                .firstName(userQueryDto.firstName())
                .lastName(userQueryDto.lastName())
                .build());
    }

    public UserDto findUserById(Long id) {
        if (log.isDebugEnabled()) {
            log.debug("Retrieving user with id: {}", id);
        }
        return mapper.toUserDto(repository.getReferenceById(id));
    }
}
