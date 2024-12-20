package com.epam.wca.gym.service.impl;

import com.epam.wca.common.gymcommon.aop.Logging;
import com.epam.wca.common.gymcommon.exception.InternalErrorException;
import com.epam.wca.gym.dto.user.UserActivationDTO;
import com.epam.wca.gym.entity.User;
import com.epam.wca.gym.repository.UserRepository;
import com.epam.wca.gym.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    @Logging
    @Transactional
    public void update(User user, UserActivationDTO userDTO) {
        user.setActive(userDTO.isActive());

        userRepository.save(user);
    }

    @Override
    @Logging
    public User findByUsername(String username) {
        return userRepository.findUserByUsername(username)
                .orElseThrow(() -> new InternalErrorException("User not found"));
    }
}
