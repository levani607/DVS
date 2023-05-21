package com.example.core.exceptions;

import lombok.Getter;

public class CoreException extends RuntimeException {

    @Getter
    private final ErrorCode errorCode;

    public CoreException(ErrorCode errorCode,String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public CoreException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
