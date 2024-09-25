package com.example.user.controller;

import com.example.user.model.UserCreateUpdateDto;
import com.example.user.model.UserDto;
import com.example.user.model.UserQueryDto;
import com.example.user.service.UserService;
import com.example.user.utils.model.ResponseEnvelope;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@Validated
public class UserController {
  private final UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  @Operation(
      summary = "Create a new user",
      description = "Create a new user",
      responses = {
          @ApiResponse(responseCode = "201", description = "User created successfully",
              content = @Content(schema = @Schema(implementation = ResponseEnvelope.class))),
          @ApiResponse(responseCode = "400", description = "Invalid Request",
              content = @Content(schema = @Schema(implementation = ResponseEnvelope.class)))
      }
  )
  @PostMapping
  public ResponseEntity<ResponseEnvelope<UserDto>> createUser(@RequestBody @Valid UserCreateUpdateDto user) {
    return new ResponseEntity<>(
        new ResponseEnvelope<>(userService.register(user), null, null),
        HttpStatus.CREATED);
  }

  @Operation(
      summary = "Get user by ID",
      description = "Get user by ID",
      responses = {
          @ApiResponse(responseCode = "200", description = "User found successfully",
              content = @Content(schema = @Schema(implementation = ResponseEnvelope.class))),
          @ApiResponse(responseCode = "404", description = "User not found",
              content = @Content(schema = @Schema(implementation = ResponseEnvelope.class)))
      }
  )
  @GetMapping("/{id}")
  public ResponseEntity<ResponseEnvelope<UserDto>> getUserById(@PathVariable Long id) {
    return new ResponseEntity<>(new ResponseEnvelope<>(userService.findUserById(id), null, null),
        HttpStatus.OK);
  }

  @Operation(
      summary = "Get all users",
      description = "Get all users",
      responses = {
          @ApiResponse(responseCode = "200", description = "Users found successfully",
              content = @Content(schema = @Schema(implementation = ResponseEnvelope.class)))
      }
  )
  @GetMapping
  public ResponseEntity<ResponseEnvelope<List<UserDto>>> getAllUsers(@Valid UserQueryDto userQueryDto) {
    return new ResponseEntity<>(userService.findAll(userQueryDto),
        HttpStatus.OK);
  }

  @Operation(
      summary = "Update user by ID",
      description = "Update user by ID",
      responses = {
          @ApiResponse(responseCode = "200", description = "User updated successfully",
              content = @Content(schema = @Schema(implementation = ResponseEnvelope.class))),
          @ApiResponse(responseCode = "404", description = "User not found",
              content = @Content(schema = @Schema(implementation = ResponseEnvelope.class))),
          @ApiResponse(responseCode = "400", description = "Invalid Request",
              content = @Content(schema = @Schema(implementation = ResponseEnvelope.class)))
      }

  )
  @PutMapping("/{id}")
  public ResponseEntity<ResponseEnvelope<UserDto>> updateUser(@PathVariable Long id,
                                                              @RequestBody @Valid UserCreateUpdateDto user) {
    return new ResponseEntity<>(
        new ResponseEnvelope<>(userService.updateUser(id, user), null, null),
        HttpStatus.OK);
  }

}
