package com.epam.wca.authentication.exception.authentication;

import org.springframework.security.core.AuthenticationException;

public class UserBlockedAuthException extends AuthenticationException {
    public UserBlockedAuthException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
