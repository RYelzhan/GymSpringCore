package com.epam.wca.gym.service;

import com.epam.wca.gym.entity.User;

public interface AuthSService {
    User authenticate(String authHeader);
}
