package com.example.fakestore.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
    RESOURCE_NOT_FOUND(HttpStatus.NOT_FOUND, ""),
    BAD_REQUEST(HttpStatus.BAD_REQUEST, ""),
    UNCATEGORIZED(HttpStatus.INTERNAL_SERVER_ERROR, "Uncategorized error"),
    UNAUTHENTICATED(HttpStatus.UNAUTHORIZED, "Unauthenticated"),
    UNAUTHORIZED(HttpStatus.FORBIDDEN, "You do not have permission");


    private HttpStatus statusCode;
    private String message;

    ErrorCode(HttpStatus statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }

    public HttpStatus getStatusCode() {
        return statusCode;
    }

    public String getMessage() {
        return message;
    }
}
