package com.example.user;

import com.example.user.model.UserCreateUpdateDto;
import com.example.user.model.UserEntity;
import com.example.user.model.UserQueryDto;
import com.example.user.repository.UserRepository;
import com.example.user.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@SpringBootTest
@ActiveProfiles("test")
class UserServiceTest {

    @Autowired
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @Test
    void userRegisterSuccess() {
        var user = new UserCreateUpdateDto(
                "John",
                "Doe",
                "johndoe@example.com",
                "john");
        Mockito.when(userRepository.saveAndFlush(any())).thenReturn(new UserEntity(
                1L,
                "John",
                "Doe",
                "johndoe@example.com",
                "john"
        ));
        var result = userService.register(user);

        verify(userRepository, Mockito.times(1)).saveAndFlush(any());
        assertEquals(user.firstName(), result.firstName());
        assertEquals(user.lastName(), result.lastName());
        assertEquals(user.email(), result.email());
        assertEquals(user.userName(), result.userName());
    }

    @Test
    void userRegisterFailure() {
        var user = new UserCreateUpdateDto(
                "John",
                "Doe",
                "johndoe@example.com",
                "john123");
        Mockito.when(userRepository.saveAndFlush(any())).thenThrow(DataIntegrityViolationException.class);

        var exception = assertThrows(IllegalArgumentException.class, () -> userService.register(user));

        assertEquals("User already exists with given email/username.", exception.getMessage());
    }

    @Test
    void findAllUsersSuccess() {
        var user = new UserEntity(1L, "John", "Doe", "johndoe@example.com", "john");
        var userEntityPage = new PageImpl<>(List.of(user),
                PageRequest.of(0, 10), 1);
        Mockito.when(userRepository.findAll(any(Example.class), any(Pageable.class))).thenReturn(userEntityPage);

        var result = userService.findAll(new UserQueryDto(
                null,
                "John",
                null,
                null,
                0,
                10
        ));

        assertNotNull(result);
        assertNotNull(result.data());
        assertEquals(1, result.data().size());
    }

    @Test
    void updateUserSuccess() {
        var user = new UserEntity(1L, "John", "Doe", "johndoe@example.com", "john");
        UserCreateUpdateDto userUpdate = new UserCreateUpdateDto(
                "John",
                "Smith",
                "johnsmith@example.com",
                "john");
        Mockito.when(userRepository.getReferenceById(1L)).thenReturn(user);
        Mockito.when(userRepository.saveAndFlush(any())).thenAnswer(it -> it.getArguments()[0]);

        var result = userService.updateUser(1L, userUpdate);

        verify(userRepository, Mockito.times(1)).saveAndFlush(any());
        assertEquals("John", result.firstName());
        assertEquals("Smith", result.lastName());
        assertEquals("johnsmith@example.com", result.email());
        assertEquals("john", result.userName());
    }

    @Test
    void updateUserFailure() {
        var user = new UserEntity(1L, "John", "Doe", "johndoe@example.com", "john");
        UserCreateUpdateDto userUpdate = new UserCreateUpdateDto(
                "John",
                "Smith",
                "johnsmith@example.com",
                "john123");
        Mockito.when(userRepository.getReferenceById(1L)).thenReturn(user);
        Mockito.when(userRepository.saveAndFlush(any())).thenThrow(DataIntegrityViolationException.class);

        var exception = assertThrows(IllegalArgumentException.class, () -> userService.updateUser(1L, userUpdate));

        assertEquals("User already exists with given email/username.", exception.getMessage());
    }

    @Test
    void findUserByIdSuccess() {
        var user = new UserEntity(1L, "John", "Doe", "johndoe@example.com", "john");
        Mockito.when(userRepository.getReferenceById(1L)).thenReturn(user);

        var result = userService.findUserById(1L);

        assertNotNull(result);
        assertEquals("John", result.firstName());
        assertEquals("Doe", result.lastName());
        assertEquals("johndoe@example.com", result.email());
        assertEquals("john", result.userName());
    }

    @Test
    void findUserByIdFailure() {
        Mockito.when(userRepository.getReferenceById(1L))
                .thenThrow(new EntityNotFoundException("User not found with id 1."));

        var exception = assertThrows(EntityNotFoundException.class, () -> userService.findUserById(1L));

        assertEquals("User not found with id 1.", exception.getMessage());
    }

}
