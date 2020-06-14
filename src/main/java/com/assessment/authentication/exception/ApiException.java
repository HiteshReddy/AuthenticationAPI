package com.assessment.authentication.exception;

public class ApiException extends RuntimeException {

    public ApiException(final String message) {
        super(message);
    }

    public ApiException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public static final class NoAccountAvailableException extends ApiException {
        public NoAccountAvailableException(String message) {
            super(message);
        }
    }

    public static final class UsernameExistsException extends ApiException {
        public UsernameExistsException(String message) {
            super(message);
        }
    }

    public static final class InvalidInputException extends ApiException {
        public InvalidInputException(String message) {
            super(message);
        }
    }

    public static final class UpstreamApiException extends ApiException {
        public UpstreamApiException(String message) {
            super(message);
        }
    }

    public static final class AuthenticationException extends ApiException {
        public AuthenticationException(String message) {
            super(message);
        }
    }

    public static final class AccountApiException extends ApiException {
        public AccountApiException(String message) {
            super(message);
        }
    }

    public enum ErrorCodes {
        BAD_REQUEST(400),
        BAD_CREDENTIALS(401),
        NOT_FOUND(404),
        CONFLICT(409),
        SERVER_EXCEPTION(500),
        ACCOUNTS_API_UNAVAILABLE(503);

        private int code;

        ErrorCodes(int code) {
            this.code = code;
        }

        public int code() {
            return this.code;
        }
    }

}
