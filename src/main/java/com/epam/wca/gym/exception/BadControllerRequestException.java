package com.epam.wca.gym.exception;

public class BadControllerRequestException extends RuntimeException {
    public BadControllerRequestException(String message) {
        super(message);
    }
}
