package com.epam.wca.gym.service.impl;

import com.epam.wca.gym.dto.user.UserLoginDTO;
import com.epam.wca.gym.entity.User;
import com.epam.wca.gym.exception.AuthenticationException;
import com.epam.wca.gym.exception.BadControllerRequestException;
import com.epam.wca.gym.repository.UserRepository;
import com.epam.wca.gym.service.AuthService;
import com.epam.wca.gym.service.JwtService;
import com.epam.wca.gym.util.AuthenticationUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private static final int CREDENTIALS_LENGTH = 2;
    private static final int USERNAME_INDEX = 0;
    private static final int PASSWORD_INDEX = 1;
    private final UserRepository userRepository;
    private final JwtService jwtService;

    @Override
    public User authenticate(String authHeader) {
        try {
            String[] credentials = AuthenticationUtils.extractCredentials(authHeader);

            if (credentials.length == CREDENTIALS_LENGTH) {
                String username = credentials[USERNAME_INDEX];
                String password = credentials[PASSWORD_INDEX];

                Optional<User> user = userRepository.findUserByUserName(username);

                if (user.isPresent() && AuthenticationUtils.validatePassword(password, user.get().getPassword())) {
                    return user.get();
                }
            }
        } catch (IllegalArgumentException e) {
            throw new AuthenticationException("Invalid credentials format");
        }

        throw new AuthenticationException("Not authenticated");
    }

    @Override
    public String authenticate(UserLoginDTO loginDTO) {
        Optional<User> user = userRepository.findUserByUserName(loginDTO.username());

        if (user.isEmpty() || !user.get().getPassword().equals(loginDTO.password())) {
            throw new BadControllerRequestException("Invalid Username or Password");
        }

        return jwtService.generateToken(user.get());
    }
}
