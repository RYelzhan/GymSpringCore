package com.epam.wca.authentication.service.impl;

import com.epam.wca.authentication.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final JwtService jwtService;

    @Override
    public String generateToken(UserDetails user) {
        return jwtService.generateToken(user);
    }
}
