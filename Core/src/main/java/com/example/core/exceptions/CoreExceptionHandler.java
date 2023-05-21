package com.example.core.exceptions;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@ControllerAdvice
@Slf4j
public class CoreExceptionHandler {

    @ExceptionHandler(CoreException.class)
    public ResponseEntity<ExceptionBody> handleCoreException(CoreException e, HttpServletRequest request) {
        String message = e.getMessage();

        ExceptionBody exceptionBody = new ExceptionBody(message, LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS), request.getRequestURI());

        log.error("Request: {}, threw an exception. Message: {}.", request.getRequestURI(), e.getMessage());


        return ResponseEntity.status(e.getErrorCode().getCode()).body(exceptionBody);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionBody> handleException(Exception e, HttpServletRequest request) {
        String message = e.getMessage();

        ExceptionBody exceptionBody = new ExceptionBody(message, LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS), request.getRequestURI());

        log.error("Request: {}, threw an exception. Message: {}.", request.getRequestURI(), e.getMessage());

        e.printStackTrace();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exceptionBody);
    }
}
