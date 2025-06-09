package com.coffee.common.exception;

import com.coffee.common.mapper.CommonResponseMapper;
import com.coffee.common.response.CommonResponse;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<CommonResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> 
            errors.put(error.getField(), error.getDefaultMessage())
        );
        
        CommonResponse response = new CommonResponse();
        CommonResponseMapper.failureResponseMapper(response, 
            new ValidationException("Validation failed: " + errors));
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<CommonResponse> handleConstraintViolationException(ConstraintViolationException ex) {
        CommonResponse response = new CommonResponse();
        CommonResponseMapper.failureResponseMapper(response, ex);
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<CommonResponse> handleResourceNotFoundException(ResourceNotFoundException ex) {
        CommonResponse response = new CommonResponse();
        CommonResponseMapper.failureResponseMapper(response, ex);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<CommonResponse> handleBusinessException(BusinessException ex) {
        CommonResponse response = new CommonResponse();
        CommonResponseMapper.failureResponseMapper(response, ex);
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<CommonResponse> handleAllUncaughtException(Exception ex) {
        CommonResponse response = new CommonResponse();
        CommonResponseMapper.failureResponseMapper(response, 
            new RuntimeException("An unexpected error occurred"));
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
} 