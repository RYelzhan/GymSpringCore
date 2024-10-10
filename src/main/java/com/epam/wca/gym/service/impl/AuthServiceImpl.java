package com.epam.wca.gym.service.impl;

import com.epam.wca.gym.dto.user.UserLoginDTO;
import com.epam.wca.gym.entity.User;
import com.epam.wca.gym.exception.AuthenticationException;
import com.epam.wca.gym.exception.BadControllerRequestException;
import com.epam.wca.gym.repository.UserRepository;
import com.epam.wca.gym.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Base64;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private static final int CREDENTIALS_LENGTH = 2;
    private static final String AUTHENTICATION_TYPE = "Basic ";
    private static final String SPLIT_TYPE = ":";
    private static final int LIMIT_OF_CREDENTIALS = 3;
    private static final int USERNAME_INDEX = 0;
    private static final int PASSWORD_INDEX = 1;
    private final UserRepository userRepository;

    @Override
    public User authenticate(String authHeader) {

        if (authHeader != null && authHeader.startsWith(AUTHENTICATION_TYPE)) {
            // Extract part with credentials
            String base64Credentials = authHeader.substring(AUTHENTICATION_TYPE.length());
            // Decode the Base64 encoded login:password
            String credentials = new String(Base64.getDecoder().decode(base64Credentials));

            // credentials = "username:password"
            String[] values = credentials.split(SPLIT_TYPE, LIMIT_OF_CREDENTIALS);

            if (values.length == CREDENTIALS_LENGTH) {
                String username = values[USERNAME_INDEX];
                String password = values[PASSWORD_INDEX];

                User user = userRepository.findUserByUserName(username);

                if (user != null && user.getPassword().equals(password)) {
                    return user;
                }
            }
        }
        throw new AuthenticationException("Not authenticated");
    }

    @Override
    public void authenticate(UserLoginDTO loginDTO) {
        User user = userRepository.findUserByUserName(loginDTO.username());

        if (user == null || !user.getPassword().equals(loginDTO.password())) {
            throw new BadControllerRequestException("Invalid Username or Password");
        }
    }
}
