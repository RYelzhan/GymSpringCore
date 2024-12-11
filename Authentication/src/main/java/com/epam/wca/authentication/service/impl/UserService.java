package com.epam.wca.authentication.service.impl;

import com.epam.wca.authentication.communication.GymCommunicationService;
import com.epam.wca.authentication.dto.UserUpdateDTO;
import com.epam.wca.authentication.entity.Role;
import com.epam.wca.authentication.entity.User;
import com.epam.wca.authentication.repository.UserRepository;
import com.epam.wca.common.gymcommon.aop.Logging;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final GymCommunicationService gymCommunicationService;

    @Logging
    @Transactional
    public void update(User user, UserUpdateDTO userDTO) {
        user.setPassword(passwordEncoder.encode(userDTO.newPassword()));

        userRepository.save(user);
    }

    @Logging
    @Transactional
    public void delete(User user) {
        userRepository.delete(user);

        if (user.getRoles().contains(new Role("TRAINEE"))) {
            gymCommunicationService.deleteTrainee(user.getUsername());
        } else if (user.getRoles().contains(new Role("TRAINER"))) {
            gymCommunicationService.deleteTrainer(user.getUsername());
        }
    }
}
