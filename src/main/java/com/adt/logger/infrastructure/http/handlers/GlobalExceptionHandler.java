package com.adt.logger.infrastructure.http.handlers;

import lombok.NonNull;
import org.springframework.http.*;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.reactive.result.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(Exception.class)
    public Mono<ProblemDetail> handleBaseException(Exception e) {
        return Mono.just(ProblemDetail
                .forStatusAndDetail(HttpStatus.BAD_REQUEST, e.getMessage()));
    }

    @ExceptionHandler(RuntimeException.class)
    public Mono<ProblemDetail> handleRuntimeException(RuntimeException e) {
        return Mono.just(ProblemDetail
                .forStatusAndDetail(HttpStatus.BAD_REQUEST, e.getMessage()));
    }

    @Override
    protected Mono<ResponseEntity<Object>> handleWebExchangeBindException(@NonNull WebExchangeBindException ex,
                                                                          HttpHeaders headers,
                                                                          HttpStatusCode status,
                                                                          ServerWebExchange exchange) {
        ProblemDetail problemDetail = ProblemDetail
                .forStatusAndDetail(status, "Validation failed for one or more fields");

        Map<String, List<String>> errors = ex.getFieldErrors()
                .stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        error -> new ArrayList<>(List.of(errorMessage(error))),
                        (existing, replacement) -> {
                            existing.addAll(replacement);
                            return existing;
                        }
                ));

        problemDetail.setProperty("errors", errors);

        return Mono.just(ResponseEntity.badRequest().body(problemDetail));
    }

    private String errorMessage(FieldError error) {
        return error.getDefaultMessage() != null
                ? error.getDefaultMessage() : "Invalid value";
    }
}
