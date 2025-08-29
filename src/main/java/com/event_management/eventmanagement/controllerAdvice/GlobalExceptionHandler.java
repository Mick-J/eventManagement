package com.event_management.eventmanagement.controllerAdvice;

import com.event_management.eventmanagement.utils.ApiMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LogManager.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiMessage> handle(ResourceNotFoundException ex) {
        logger.error("Error: {}", ex.getMessage(), ex);

        String userMessage = ex.getUserMessage().isBlank()
                ? ex.getClass().getSimpleName() + " not Found"
                : ex.getUserMessage();
        ApiMessage message = new ApiMessage(
                "error",
                "Something went wrong",
                userMessage,
                Collections.emptyMap());
        //
        return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ApiMessage> handle( DuplicateResourceException ex) {
        logger.error("Error: {}", ex.getMessage(), ex);
        ApiMessage message = new ApiMessage(
                "error",
                "Something went wrong",
                ex.getUserMessage(),
                Collections.emptyMap());
        return new ResponseEntity<>(message, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(EntityInUseException.class)
    public ResponseEntity<ApiMessage> handle(EntityInUseException ex) {
        ApiMessage message = new ApiMessage(
                "error",
                "Delete Error",
                ex.getUserMessage(),
                Map.of("redirectPath", ex.getRedirectPath()));
        return new ResponseEntity<>(message, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<ApiMessage> handle(BindException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));

        ApiMessage message = new ApiMessage(
                "error",
                "Validation Failed",
                "Please correct the highlighted errors.",
                errors
        );
        return ResponseEntity.badRequest().body(message);
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<ApiMessage> handle(DataAccessException ex) {
        logger.error("Database error: {}", ex.getMessage(), ex);

        ApiMessage message = new ApiMessage(
                "error",
                "Database Error",
                "An error occurred while processing your request. Please contact the admin",
                Collections.emptyMap()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(message);
    }

    @ExceptionHandler({Exception.class, RuntimeException.class})
    public ResponseEntity<ApiMessage> handle(Exception ex) {
        logger.error("Unhandled exception occurred: {}", ex.getMessage(), ex);

        ApiMessage message = new ApiMessage(
                "error",
                "Internal Server Error: ",
                "Something went wrong. Please Contact the admin.",
                Collections.emptyMap()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(message);
    }
}