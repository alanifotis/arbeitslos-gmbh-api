package arbeitslos.gmbh.api.errors;

import arbeitslos.gmbh.api.errors.custom.DuplicateEmailException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.*;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.reactive.result.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.ServerWebInputException;
import reactor.core.publisher.Mono;
import reactor.util.annotation.NonNull;

import java.net.URI;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CustomErrorHandler extends ResponseEntityExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(CustomErrorHandler.class);
    private final URI PROBLEM_DETAIL_TYPE_BAD_REQUEST = URI.create("https://www.rfc-editor.org/rfc/rfc9110.html#name-400-bad-request");

    @ExceptionHandler(DuplicateEmailException.class)
    public Mono<ResponseEntity<ProblemDetail>> handleEmailConflictException(DuplicateEmailException ex, ServerHttpRequest request) {
        String userMessage = "The email address '" + ex.getEmail() + "' is already registered. Please use a different email.";
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, userMessage);
        problemDetail.setTitle("Email Already Exists");
        problemDetail.setType(URI.create("/errors/email-conflict"));
        problemDetail.setProperty("timestamp", Instant.now());
        problemDetail.setProperty("conflictingEmail", ex.getEmail());
        problemDetail.setProperties(Map.of("info", Map.of("first", "Did you forget your password??", "second", "Try registering with another email address.")));

        logger.warn("Email conflict for {}: {}", ex.getEmail(), ex.getMessage());

        return Mono.just(ResponseEntity.status(HttpStatus.CONFLICT).body(problemDetail));
    }

    @Override
    @NonNull
    protected Mono<ResponseEntity<Object>> handleWebExchangeBindException(
            WebExchangeBindException exception, @NonNull HttpHeaders headers, @NonNull HttpStatusCode status,@NonNull ServerWebExchange exchange) {

        // Create a map to hold field errors (field name -> error message)
        var errors = new LinkedHashMap<String, Object>();

        var temp = new LinkedHashMap<String, String>();

        // Extract field errors from the exception
        exception.getBindingResult().getAllErrors().forEach(error -> {
            // Check if the error is a FieldError to get the field name
            if (error instanceof FieldError fieldError) {
                String fieldName = fieldError.getField();
                String errorMessage = error.getDefaultMessage();
                temp.put(fieldName, errorMessage);
            } else {
                // Handle global errors (not tied to a specific field) if necessary
                temp.put(error.getObjectName(), error.getDefaultMessage());
            }
        });

        errors.put(temp.size() < 2 ? "error" : "errors", temp);

        ProblemDetail problemDetail = ProblemDetail
                .forStatusAndDetail(HttpStatus.BAD_REQUEST, "Field validation exception(s).");
        problemDetail.setType(PROBLEM_DETAIL_TYPE_BAD_REQUEST);
        problemDetail.setTitle("Field validation exception(s).");
        problemDetail.setProperties(errors);
        problemDetail.setInstance(URI.create("validation-exception"));

        // Return a ResponseEntity with the errors map and BAD_REQUEST status
        // Note the return type is Mono<ResponseEntity<Object>> as required by the override
        return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problemDetail));
    }

    @Override
    @NonNull
    public Mono<ResponseEntity<Object>>handleServerWebInputException(ServerWebInputException exception, @NonNull HttpHeaders headers, @NonNull HttpStatusCode status,
                                                                         @NonNull ServerWebExchange exchange) {
        if (!exception.getCause().getMessage().contains("arbeitslos.gmbh.api.model.EmploymentStatus")) {
            var problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, exception.getCause().getMessage());
            problemDetail.setTitle("JSON Decoding Error");
            problemDetail.setType(PROBLEM_DETAIL_TYPE_BAD_REQUEST);
           return Mono.just(ResponseEntity.status(400).body(problemDetail));
        }

        var problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "EmploymentStatus should have one of the valid values.");

        problemDetail.setType(PROBLEM_DETAIL_TYPE_BAD_REQUEST);
        problemDetail.setInstance(URI.create("invalid-field-value"));
        problemDetail.setTitle("Invalid value provided for Employment Status Field.");
        problemDetail.setProperties(Map.of("valid options", List.of("UNEMPLOYED", "EMPLOYED", "ACTIVELY_LOOKING", "GAVE_UP_ALL_HOPE", "FARMING")));

        return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problemDetail));

    }
}
