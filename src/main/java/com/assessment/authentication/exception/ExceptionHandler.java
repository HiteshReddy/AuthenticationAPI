package com.assessment.authentication.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;

import static com.assessment.authentication.exception.ApiException.*;
import static org.springframework.http.HttpStatus.*;

@ControllerAdvice
public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(NoAccountAvailableException.class)
    public ResponseEntity<AuthenticationAppError> handle(final NoAccountAvailableException e) {
        final AuthenticationAppError error = AuthenticationAppError.builder()
                .message(e.getMessage())
                .code(ApiException.ErrorCodes.NOT_FOUND.code())
                .build();
        return ResponseEntity.status(NOT_FOUND).body(error);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(UsernameExistsException.class)
    public ResponseEntity<AuthenticationAppError> handle(final UsernameExistsException e) {
        final AuthenticationAppError error = AuthenticationAppError.builder()
                .message(e.getMessage())
                .code(ErrorCodes.CONFLICT.code())
                .build();
        return ResponseEntity.status(CONFLICT).body(error);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(InvalidInputException.class)
    public ResponseEntity<AuthenticationAppError> handle(final InvalidInputException e) {
        final AuthenticationAppError error = AuthenticationAppError.builder()
                .message(e.getMessage())
                .code(ErrorCodes.BAD_REQUEST.code())
                .build();
        return ResponseEntity.status(BAD_REQUEST).body(error);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<AuthenticationAppError> handle(final AuthenticationException e) {
        final AuthenticationAppError error = AuthenticationAppError.builder()
                .message(e.getMessage())
                .code(ErrorCodes.BAD_CREDENTIALS.code())
                .build();
        return ResponseEntity.status(UNAUTHORIZED).body(error);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(AccountApiException.class)
    public ResponseEntity<AuthenticationAppError> handle(final AccountApiException e) {
        final AuthenticationAppError error = AuthenticationAppError.builder()
                .message(e.getMessage())
                .code(ErrorCodes.NOT_FOUND.code())
                .build();
        return ResponseEntity.status(NOT_FOUND).body(error);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(UpstreamApiException.class)
    public ResponseEntity<AuthenticationAppError> handle(final UpstreamApiException e) {
        final AuthenticationAppError error = AuthenticationAppError.builder()
                .message(e.getMessage())
                .code(ErrorCodes.ACCOUNTS_API_UNAVAILABLE.code())
                .build();
        return ResponseEntity.status(SERVICE_UNAVAILABLE).body(error);
    }
}
