package com.epam.wca.gym.service;

import com.epam.wca.gym.dto.user.UserActivationDTO;
import com.epam.wca.gym.dto.user.UserUpdateDTO;
import com.epam.wca.gym.entity.User;

public interface UserService {
    void update(User user, UserUpdateDTO userDTO);

    void update(User user, UserActivationDTO userDTO);

    User findByUsername(String username);
}
