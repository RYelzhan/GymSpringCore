package com.epam.wca.gym.exception;

public class ForbiddenActionException extends RuntimeException{
    public ForbiddenActionException(String message) {
        super(message);
    }
}
