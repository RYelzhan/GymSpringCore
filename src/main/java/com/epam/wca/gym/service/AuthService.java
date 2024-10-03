package com.epam.wca.gym.service;

import com.epam.wca.gym.dto.user.UserLoginDTO;
import com.epam.wca.gym.entity.User;

public interface AuthService {
    User authenticate(String authHeader);
    void authenticate(UserLoginDTO loginDTO);
}
