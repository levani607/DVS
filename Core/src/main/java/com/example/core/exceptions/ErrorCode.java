package com.example.core.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;


public enum ErrorCode {

    CONFLICT(HttpStatus.CONFLICT,"Conflict!"),
    OBJECT_NOT_FOUND(HttpStatus.NOT_FOUND,"Object not found!"),
    ILLEGAL_ARGUMENT_PROVIDED(HttpStatus.BAD_REQUEST,"Provided arguments are invalid!"),
    FORBIDDEN(HttpStatus.FORBIDDEN,"You do not have access!"),
    UNEXPECTED_ERROR(HttpStatus.INTERNAL_SERVER_ERROR,"Unexpected error!");

    @Getter
    private final HttpStatus code;
    @Getter
    private final String message;
    ErrorCode(HttpStatus code,String message) {
        this.code=code;
        this.message=message;
    }
}
