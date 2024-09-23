package com.example.user.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UserCreateUpdateDto(
        @Schema(description = "first name of user")
        @JsonProperty("firstName")
        @NotNull(message = "first name cannot be null")
        @NotBlank(message = "first name cannot be empty")
        String firstName,

        @Schema(description = "last name of user")
        @JsonProperty("lastName")
        @NotNull(message = "last name cannot be null")
        @NotBlank(message = "last name cannot be empty")
        String lastName,

        @Schema(description = "email of user")
        @JsonProperty("email")
        @NotNull(message = "email cannot be null")
        @NotBlank(message = "email cannot be empty")
        @Email
        String email,

        @Schema(description = "user name of user")
        @JsonProperty("userName")
        @NotNull(message = "user name cannot be null")
        @NotBlank(message = "user name cannot be empty")
        String userName
) {
}