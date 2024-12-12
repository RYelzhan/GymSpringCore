package com.epam.wca.gym.service;

import com.epam.wca.gym.entity.User;

public interface UserService {
    User findByUsername(String username);
}
