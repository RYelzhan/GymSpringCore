package com.epam.wca.gym.service.impl;

import com.epam.wca.gym.exception.UserBlockedException;
import com.epam.wca.gym.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;
    private final LoginAttemptService loginAttemptService;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (loginAttemptService.isBlocked()) {
            throw new UserBlockedException("User is blocked");
        }

        return userRepository.findUserByUserName(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
