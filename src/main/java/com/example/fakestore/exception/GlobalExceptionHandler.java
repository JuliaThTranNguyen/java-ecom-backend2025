package com.example.fakestore.exception;

import com.example.fakestore.dto.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.file.AccessDeniedException;
import java.util.Optional;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ApiResponse<Object>> handleApiException(ApiException ex) {
        ErrorCode errorCode = ex.getErrorCode();

        ApiResponse<Object> response = ApiResponse.builder()
                .code(errorCode.getStatusCode())
                .message(errorCode.getMessage())
                .build();

        return ResponseEntity.status(errorCode.getStatusCode()).body(response);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse<Object>> handleAccessDeniedException(AccessDeniedException ex) {
        ErrorCode errorCode = ErrorCode.UNAUTHORIZED;

        ApiResponse<Object> response = ApiResponse.builder()
                .code(errorCode.getStatusCode())
                .message(errorCode.getMessage())
                .build();

        return ResponseEntity.status(errorCode.getStatusCode()).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleGlobalException(Exception ex) {
        ApiResponse<Object> response = ApiResponse.builder()
                .code(ErrorCode.UNCATEGORIZED.getStatusCode())
                //                .message(ErrorCode.UNCATEGORIZED.getMessage())
                .message(ex.getMessage())
                .build();

        return ResponseEntity.status(ErrorCode.UNCATEGORIZED.getStatusCode()).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Object>> handleValidation(MethodArgumentNotValidException ex) {
        String message = Optional.ofNullable(ex.getFieldError())
                .map(fieldError -> fieldError.getDefaultMessage())
                .orElse("Validation failed");

        ApiResponse<Object> response = ApiResponse.builder()
                .code(HttpStatus.BAD_REQUEST)
                .message(message)
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
}
