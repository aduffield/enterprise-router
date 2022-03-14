package com.hastings.router.authentication.filter;

public class JWTUserException extends RuntimeException {


    public JWTUserException() {
    }

    public JWTUserException(String message) {
        super(message);
    }

    public JWTUserException(String message, Throwable cause) {
        super(message, cause);
    }
}
