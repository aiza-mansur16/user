package com.example.user.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

public record UserDto(
        @Schema(description = "user id")
        @JsonProperty("id")
        Long id,

        @Schema(description = "user first name")
        @JsonProperty("firstName")
        String firstName,

        @Schema(description = "user last name")
        @JsonProperty("lastName")
        String lastName,

        @Schema(description = "user email")
        @JsonProperty("email")
        String email,

        @Schema(description = "user's username")
        @JsonProperty("userName")
        String userName
) {
}
