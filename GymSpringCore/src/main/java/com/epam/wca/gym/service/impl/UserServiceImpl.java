package com.epam.wca.gym.service.impl;

import com.epam.wca.common.gymcommon.aop.Logging;
import com.epam.wca.common.gymcommon.exception.InternalErrorException;
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
    @Logging
    public User findByUsername(String username) {
        return userRepository.findUserByUsername(username)
                .orElseThrow(() -> new InternalErrorException("User not found"));
    }
}
