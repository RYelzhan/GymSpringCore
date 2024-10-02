package com.epam.wca.gym.service.impl;

import com.epam.wca.gym.entity.User;
import com.epam.wca.gym.repository.UserRepository;
import com.epam.wca.gym.service.AuthSService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Base64;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthSService {
    @NonNull
    private UserRepository userRepository;

    @Override
    public User authenticate(String authHeader) {

        if (authHeader != null && authHeader.startsWith("Basic ")) {
            // Extract and decode the Base64 encoded login:password
            String base64Credentials = authHeader.substring("Basic ".length());
            String credentials = new String(Base64.getDecoder().decode(base64Credentials));

            // credentials = "username:password"
            String[] values = credentials.split(":", 3);

            if (values.length == 2) {
                String username = values[0];
                String password = values[1];

                User user = userRepository.findUserByUserName(username);

                if (user != null && user.getPassword().equals(password)) {
                    return user;
                }
            }
        }
        throw new IllegalArgumentException("Not authenticated");
    }
}
