package com.dgramada.exceptionhandling;

import com.dgramada.exceptionhandling.exception.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleException(Exception ex) {
        logger.error("Unhandled exception", ex);
        return new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Internal server error.",
                "An error occurred while processing the request."
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException ex) {
        logger.warn("Invalid method arguments: ", ex);
        Map<String, String> validationErrors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach((fieldError) ->
                validationErrors.put(fieldError.getField(), fieldError.getDefaultMessage())
        );

        final var errorResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Bad request",
                "Invalid data provided. Please check the request."
        );
        errorResponse.setValidationErrors(validationErrors);
        return errorResponse;
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleResourceNotFound(ResourceNotFoundException ex) {
        logger.warn("Resource not found", ex);
        return new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                "Resource not found.",
                ex.getMessage()
        );
    }
}
