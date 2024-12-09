package com.epam.wca.authentication.exception;

public class UserBlockedException extends RuntimeException {
    public UserBlockedException(String message) {
        super(message);
    }
}
