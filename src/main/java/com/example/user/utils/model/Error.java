package com.example.user.utils.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.util.List;

@Getter
@Setter
@edu.umd.cs.findbugs.annotations.SuppressFBWarnings(value = {
        "EI_EXPOSE_REP",
        "EI_EXPOSE_REP2"
}, justification = "Error is used for error does not expose any sensitive information.")
public class Error {
        @Schema(description = "status code of the error")
        private HttpStatus statusCode;

        @Schema(description = "error messages")
        private List<String> messages;
}
