package com.epam.wca.authentication.service;

import org.springframework.security.core.userdetails.UserDetails;

public interface AuthService {

    String generateToken(UserDetails user);
}
