package com.acsousa.gerenciador_de_rotinas.common.exceptions;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ProblemDetail entityNotFound(ResourceNotFoundException e, HttpServletRequest request) {
        log.warn("Recurso não encontrado: {}", e.getMessage());
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.getMessage());
        problemDetail.setTitle("Resource not found");
        problemDetail.setInstance(URI.create(request.getRequestURI()));
        problemDetail.setProperty("timestamp", Instant.now());
        problemDetail.setProperty("message", e.getMessage()); // Compatibilidade com StandardError legada
        return problemDetail;
    }

    @ExceptionHandler(AttributeAlreadyExistsException.class)
    public ProblemDetail attributeAlreadyExists(AttributeAlreadyExistsException e, HttpServletRequest request) {
        log.warn("Atributo já existente: {}", e.getMessage());
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, e.getMessage());
        problemDetail.setTitle("Existing attribute");
        problemDetail.setInstance(URI.create(request.getRequestURI()));
        problemDetail.setProperty("timestamp", Instant.now());
        problemDetail.setProperty("message", e.getMessage()); // Compatibilidade com StandardError legada
        return problemDetail;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail validationConstraints(MethodArgumentNotValidException e, HttpServletRequest request) {
        log.warn("Erro de validação nos campos: {}", e.getMessage());
        String defaultDetail = "Erro de validação nos campos informados.";
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.UNPROCESSABLE_ENTITY, defaultDetail);
        problemDetail.setTitle("Validation exception");
        problemDetail.setInstance(URI.create(request.getRequestURI()));
        problemDetail.setProperty("timestamp", Instant.now());
        problemDetail.setProperty("message", defaultDetail); // Compatibilidade com StandardError legada

        // Lista de objetos com fieldName e message para compatibilidade com ValidationError/FieldMessage
        List<Map<String, String>> errorList = new ArrayList<>();
        for (FieldError fieldError : e.getBindingResult().getFieldErrors()) {
            Map<String, String> err = new HashMap<>();
            err.put("fieldName", fieldError.getField());
            err.put("message", fieldError.getDefaultMessage());
            errorList.add(err);
        }
        problemDetail.setProperty("errors", errorList);

        return problemDetail;
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ProblemDetail requestError(HttpMessageNotReadableException e, HttpServletRequest request) {
        log.warn("Erro ao ler requisição: {}", e.getMessage());
        String message = "Erro na formatação do JSON ou valor inválido.";

        if (e.getCause() instanceof InvalidFormatException invalidFormatException) {
            if (invalidFormatException.getPath() != null && !invalidFormatException.getPath().isEmpty()) {
                String fieldName = invalidFormatException.getPath().getFirst().getFieldName();
                message = String.format("O campo '%s' possui um valor inválido.", fieldName);
            }
        }

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.UNPROCESSABLE_ENTITY, message);
        problemDetail.setTitle("Request error");
        problemDetail.setInstance(URI.create(request.getRequestURI()));
        problemDetail.setProperty("timestamp", Instant.now());
        problemDetail.setProperty("message", message); // Compatibilidade com StandardError legada
        return problemDetail;
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ProblemDetail databaseViolation(DataIntegrityViolationException e, HttpServletRequest request) {
        log.error("Erro de integridade de banco de dados: ", e);
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.getMessage());
        problemDetail.setTitle("Database violation error");
        problemDetail.setInstance(URI.create(request.getRequestURI()));
        problemDetail.setProperty("timestamp", Instant.now());
        problemDetail.setProperty("message", e.getMessage()); // Compatibilidade com StandardError legada
        return problemDetail;
    }
}
