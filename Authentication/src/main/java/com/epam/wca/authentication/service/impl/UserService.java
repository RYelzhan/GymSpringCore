package com.epam.wca.authentication.service.impl;

import com.epam.wca.authentication.communication.GymCommunicationService;
import com.epam.wca.authentication.dto.UserUpdateDTO;
import com.epam.wca.authentication.entity.Role;
import com.epam.wca.authentication.entity.User;
import com.epam.wca.authentication.repository.RoleRepository;
import com.epam.wca.authentication.repository.UserRepository;
import com.epam.wca.common.gymcommon.aop.Logging;
import com.epam.wca.common.gymcommon.auth_dto.UserAuthenticatedDTO;
import com.epam.wca.common.gymcommon.auth_dto.UserRegistrationDTO;
import com.epam.wca.common.gymcommon.exception.InternalErrorException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final GymCommunicationService gymCommunicationService;
    private final ProfileServiceImpl profileService;
    private final RoleRepository roleRepository;

    @Logging
    @Transactional
    public void update(User user, UserUpdateDTO userDTO) {
        user.setPassword(passwordEncoder.encode(userDTO.newPassword()));

        userRepository.save(user);
    }

    @Logging
    @Transactional
    public UserAuthenticatedDTO create(UserRegistrationDTO userRegistrationDTO) {
        Set<Role> roles = userRegistrationDTO
                .roles()
                .stream()
                .map(roleName -> roleRepository.findRoleByName(roleName)
                        .orElseThrow(() -> {
                            throw new InternalErrorException("User trying to register with non-existent role.");
                        })
                )
                .collect(Collectors.toSet());

        var authenticatedUser = new UserAuthenticatedDTO(
                profileService.createUsername(userRegistrationDTO.firstname(), userRegistrationDTO.lastname()),
                profileService.createPassword()
        );

        User user = new User(
                authenticatedUser.username(),
                passwordEncoder.encode(authenticatedUser.password()),
                roles
        );

        userRepository.save(user);

        return authenticatedUser;
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
