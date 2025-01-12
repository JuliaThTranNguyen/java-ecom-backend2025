package com.example.fakestore.exception;


import java.util.Arrays;


public class ApiException extends RuntimeException {
    private final ErrorCode errorCode;
    private final String message;

    public ApiException(ErrorCode errorCode, String... obj) {
        this.message = String.format("%s", Arrays.stream(obj).findFirst().orElse(errorCode.getMessage()));
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
