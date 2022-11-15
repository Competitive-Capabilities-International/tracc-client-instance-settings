package com.traccsolution.scaffold.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Singular;
import org.springframework.http.HttpStatus;

import java.util.List;

/**
 * @author KeeshanReddy
 * @see <a href="https://www.baeldung.com/global-error-handler-in-a-spring-rest-api">Custom Error Message Handling for REST API</a>
 */
@Data
@Builder
public class ApiErrorDTO {
    private HttpStatus status;
    private String message;
    @Singular
    private List<String> errors;
}
