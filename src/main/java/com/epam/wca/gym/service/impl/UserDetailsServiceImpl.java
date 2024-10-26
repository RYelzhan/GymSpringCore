package com.epam.wca.gym.service.impl;

import com.epam.wca.gym.exception.UserBlockedException;
import com.epam.wca.gym.exception.authentication.UserBlockedAuthException;
import com.epam.wca.gym.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;
    private final LoginAttemptService loginAttemptService;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException, UserBlockedAuthException {
        if (loginAttemptService.isBlocked()) {
            log.info("User got blocked. IP: %s".formatted(loginAttemptService.getClientIP()));

            throw new UserBlockedAuthException("User is blocked", new UserBlockedException("User is blocked"));
        }

        return userRepository.findUserByUserName(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found. Incorrect Username."));
    }
}
