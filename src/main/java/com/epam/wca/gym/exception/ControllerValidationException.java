package com.epam.wca.gym.exception;

public class ControllerValidationException extends RuntimeException{
    public ControllerValidationException(String message) {
        super(message);
    }
}
