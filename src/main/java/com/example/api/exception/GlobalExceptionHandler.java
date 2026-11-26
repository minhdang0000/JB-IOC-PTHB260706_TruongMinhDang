package com.example.api.exception;

import com.example.api.model.dto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Object>> handleValidationException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(err->{
           errors.put(err.getField(), err.getDefaultMessage());
        });
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.<Object>builder()
                        .success(false)
                        .message(ex.getMessage())
                        .data(errors)
                        .errors(HttpStatus.BAD_REQUEST.getReasonPhrase())
                        .httpStatus(HttpStatus.BAD_REQUEST.value())
                        .build()
        );
    }

    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ApiResponse<Object>> handleDuplicateResourceException(DuplicateResourceException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ApiResponse.<Object>builder()
                        .success(false)
                        .message("CONFLICT")
                        .data(ex.getMessage())
                        .errors(HttpStatus.CONFLICT.getReasonPhrase())
                        .httpStatus(HttpStatus.CONFLICT.value())
                        .build()
        );
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleResourceNotFoundException(ResourceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.<Object>builder()
                        .success(false)
                        .message("NOT FOUND")
                        .data(ex.getMessage())
                        .errors(HttpStatus.NOT_FOUND.getReasonPhrase())
                        .httpStatus(HttpStatus.NOT_FOUND.value())
                        .build()
        );
    }

    @ExceptionHandler(InvalidStatusTransitionException.class)
    public ResponseEntity<ApiResponse<Object>> handleInvalidStatusTransitionException(InvalidStatusTransitionException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.<Object>builder()
                        .success(false)
                        .message("BAD REQUEST")
                        .data(ex.getMessage())
                        .errors(HttpStatus.BAD_REQUEST.getReasonPhrase())
                        .httpStatus(HttpStatus.BAD_REQUEST.value())
                        .build()
                );
    }


    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<Object>> handleIllegalArgumentException(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.<Object>builder()
                        .success(false)
                        .message("BAD REQUEST")
                        .data(ex.getMessage())
                        .errors(HttpStatus.BAD_REQUEST.getReasonPhrase())
                        .httpStatus(HttpStatus.BAD_REQUEST.value())
                        .build()
                );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.<Object>builder()
                        .success(false)
                        .message("INTERNAL SERVER ERROR")
                        .data(ex.getMessage())
                        .errors(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
                        .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .build()
                );
    }
}
