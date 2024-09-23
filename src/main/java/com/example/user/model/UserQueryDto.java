package com.example.user.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record UserQueryDto(
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
        @Schema(description = "page number")
        @JsonProperty("page")
        @Min(value = 0, message = "page cannot be negative")
        @NotNull
        Integer page,
        @Schema(description = "page size")
        @JsonProperty("size")
        @Min(value = 1, message = "page size  cannot be less than 1")
        @Max(value = 50, message = "page size cannot be greater than 50")
        @NotNull
        Integer size
) {
}
