package com.epam.wca.gym.service;

import org.springframework.security.core.userdetails.UserDetails;

public interface AuthService {

    String generateToken(UserDetails user);
}
