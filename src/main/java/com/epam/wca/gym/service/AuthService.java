package com.epam.wca.gym.service;

import com.epam.wca.gym.dto.user.UserLoginDTO;
import com.epam.wca.gym.entity.User;
import org.springframework.security.core.userdetails.UserDetails;

public interface AuthService {
    User authenticate(String authHeader);

    void authenticate(UserLoginDTO loginDTO);

    String generateToken(UserDetails user);
}
