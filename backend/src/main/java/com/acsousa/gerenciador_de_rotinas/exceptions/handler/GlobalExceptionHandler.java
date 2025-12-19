package com.acsousa.gerenciador_de_rotinas.exceptions.handler;

import com.acsousa.gerenciador_de_rotinas.exceptions.custom.AttributeAlreadyExistsException;
import com.acsousa.gerenciador_de_rotinas.exceptions.custom.ResourceNotFoundException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<StandardError> entityNotFound(ResourceNotFoundException e, HttpServletRequest request) {
        HttpStatus httpStatus = HttpStatus.NOT_FOUND;
        StandardError standardError = new StandardError();

        standardError.setTimestamp(Instant.now());
        standardError.setStatus(httpStatus.value());
        standardError.setError("Resource not found");
        standardError.setMessage(e.getMessage());
        standardError.setPath(request.getRequestURI());

        return ResponseEntity.status(httpStatus).body(standardError);
    }

    @ExceptionHandler(AttributeAlreadyExistsException.class)
    public ResponseEntity<StandardError> attributeAlreadyExists(AttributeAlreadyExistsException e, HttpServletRequest request) {
        HttpStatus httpStatus = HttpStatus.CONFLICT;
        StandardError standardError = new StandardError();

        standardError.setTimestamp(Instant.now());
        standardError.setStatus(httpStatus.value());
        standardError.setError("Existing attribute");
        standardError.setMessage(e.getMessage());
        standardError.setPath(request.getRequestURI());

        return ResponseEntity.status(httpStatus).body(standardError);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationError> validationConstraints(MethodArgumentNotValidException e,
                                                                 HttpServletRequest request) {
        HttpStatus httpStatus = HttpStatus.UNPROCESSABLE_ENTITY;
        ValidationError validationError = new ValidationError();

        validationError.setTimestamp(Instant.now());
        validationError.setStatus(httpStatus.value());
        validationError.setError("Validation exception");
        validationError.setMessage(e.getMessage());
        validationError.setPath(request.getRequestURI());

        for (FieldError fieldError : e.getBindingResult().getFieldErrors()){
            validationError.addError(fieldError.getField(), fieldError.getDefaultMessage());
        }

        return ResponseEntity.status(httpStatus).body(validationError);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<StandardError> requestError(HttpMessageNotReadableException e, HttpServletRequest request) {
        HttpStatus httpStatus = HttpStatus.UNPROCESSABLE_ENTITY;
        StandardError standardError = new StandardError();
        String message = "Erro na formatação do JSON ou valor inválido.";

        if (e.getCause() instanceof InvalidFormatException invalidFormatException) {

            if (invalidFormatException.getPath() != null && !invalidFormatException.getPath().isEmpty()) {
                String fieldName = invalidFormatException.getPath().getFirst().getFieldName();
                message = String.format("O campo '%s' possui um valor inválido.", fieldName);
            }
        }

        standardError.setTimestamp(Instant.now());
        standardError.setStatus(httpStatus.value());
        standardError.setError("Request error");
        standardError.setMessage(message);
        standardError.setPath(request.getRequestURI());

        return ResponseEntity.status(httpStatus).body(standardError);
    }
}
