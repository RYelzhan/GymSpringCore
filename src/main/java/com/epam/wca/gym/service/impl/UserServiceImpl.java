package com.epam.wca.gym.service.impl;

import com.epam.wca.gym.dto.user.UserActivationDTO;
import com.epam.wca.gym.dto.user.UserUpdateDTO;
import com.epam.wca.gym.entity.User;
import com.epam.wca.gym.repository.UserRepository;
import com.epam.wca.gym.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public void update(User user, UserUpdateDTO userDTO) {
        user.setPassword(userDTO.newPassword());

        userRepository.save(user);
    }

    @Override
    public void update(User user, UserActivationDTO userDTO) {
        user.setActive(userDTO.isActive());

        userRepository.save(user);
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findUserByUserName(username);
    }
}