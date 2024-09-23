package com.example.user;

import com.example.user.model.UserCreateUpdateDto;
import com.example.user.model.UserDto;
import com.example.user.service.UserService;
import com.example.user.utils.model.ResponseEnvelope;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class UserControllerTest {

    @LocalServerPort
    int port = 0;

    @Autowired
    TestRestTemplate testRestTemplate;

    @MockBean
    private UserService userService;

    @Test
    void testRegisterSuccess() {
        Mockito.when(userService.register(any()))
                .thenReturn(new UserDto(
                        1L,
                        "John",
                        "Doe",
                        "john.doe@email.com",
                        "john"
                ));
        var response = testRestTemplate.exchange(
                "http://localhost:" + port + "/api/users",
                HttpMethod.POST,
                new HttpEntity<>(new UserCreateUpdateDto(
                        "John",
                        "Doe",
                        "john.doe@email.com",
                        "john"
                )),
                new ParameterizedTypeReference<ResponseEnvelope<UserDto>>() {
                }
        );
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    void testRegisterFailure() {
        Mockito.when(userService.register(any()))
               .thenThrow(new IllegalArgumentException("Email already exists."));
        var response = testRestTemplate.exchange(
                "http://localhost:" + port + "/api/users",
                HttpMethod.POST,
                new HttpEntity<>(new UserCreateUpdateDto(
                        "John",
                        "Doe",
                        "john.doe@email.com",
                        "john"
                )),
                new ParameterizedTypeReference<ResponseEnvelope<UserDto>>() {
                }
        );
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void findUserByIdSuccess() {
        Mockito.when(userService.findUserById(1L))
               .thenReturn(new UserDto(
                        1L,
                        "John",
                        "Doe",
                        "john.doe@email.com",
                        "john"
                ));
        var response = testRestTemplate.exchange(
                "http://localhost:" + port + "/api/users/1",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<ResponseEnvelope<UserDto>>() {}
        );
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().data());
        assertEquals("John", response.getBody().data().firstName());
        assertEquals("Doe", response.getBody().data().lastName());
        assertEquals("john.doe@email.com", response.getBody().data().email());
        assertEquals("john", response.getBody().data().userName());
    }

    @Test
    void findUserByIdFailure() {
        Mockito.when(userService.findUserById(1L))
               .thenThrow(new EntityNotFoundException("User not found."));
        var response = testRestTemplate.exchange(
                "http://localhost:" + port + "/api/users/1",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<ResponseEnvelope<UserDto>>() {}
        );
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void updateUserSuccess() {
        Mockito.when(userService.updateUser(any(), any()))
               .thenReturn(new UserDto(
                        1L,
                        "John",
                        "Doe",
                        "john.doe@email.com",
                        "john"
                ));
        var response = testRestTemplate.exchange(
                "http://localhost:" + port + "/api/users/1",
                HttpMethod.PUT,
                new HttpEntity<>(new UserCreateUpdateDto(
                        "John",
                        "Doe",
                        "john.doe@email.com",
                        "john"
                )),
                new ParameterizedTypeReference<ResponseEnvelope<UserDto>>() {
                }
        );
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void updateUserFailure() {
        Mockito.when(userService.updateUser(any(), any()))
                .thenThrow(new IllegalArgumentException("User already exists with given email/username."));
        var response = testRestTemplate.exchange(
                "http://localhost:" + port + "/api/users/1",
                HttpMethod.PUT,
                new HttpEntity<>(new UserCreateUpdateDto(
                        "John",
                        "Doe",
                        "john.doe@email.com",
                        "john"
                )),
                new ParameterizedTypeReference<ResponseEnvelope<UserDto>>() {
                }
        );
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

   @Test
    void findAllUsersSuccess() {
        Mockito.when(userService.findAll(any()))
               .thenReturn(new ResponseEnvelope<>(
                        List.of(
                                new UserDto(
                                        1L,
                                        "John",
                                        "Doe",
                                        "john.doe@email.com",
                                        "john"
                                )
                        ),
                       null,
                       null
                ));
        var response = testRestTemplate.exchange(
                "http://localhost:" + port + "/api/users?page=0&size=10",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<ResponseEnvelope<List<UserDto>>>() {
                }
        );
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().data());
        assertEquals(1, response.getBody().data().size());
   }
}
