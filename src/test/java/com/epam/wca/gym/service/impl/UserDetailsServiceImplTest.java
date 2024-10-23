package com.epam.wca.gym.service.impl;

import com.epam.wca.gym.entity.User;
import com.epam.wca.gym.exception.UserBlockedException;
import com.epam.wca.gym.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserDetailsServiceImplTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private LoginAttemptService loginAttemptService;

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    @Test
    void testLoadUserByUsername_UserBlocked_ThrowsUserBlockedException() {
        // Mock the login attempt service to simulate a blocked user
        when(loginAttemptService.isBlocked()).thenReturn(true);
        when(loginAttemptService.getClientIP()).thenReturn("127.0.0.1");

        // Assert that a UserBlockedException is thrown when the user is blocked
        assertThrows(UserBlockedException.class, () -> userDetailsService.loadUserByUsername("testuser"),
                "Expected UserBlockedException to be thrown when the user is blocked");

        // Verify that the login attempt service was checked
        verify(loginAttemptService).isBlocked();
    }

    @Test
    void testLoadUserByUsername_UserNotFound_ThrowsUsernameNotFoundException() {
        // Mock the login attempt service to simulate that the user is not blocked
        when(loginAttemptService.isBlocked()).thenReturn(false);

        // Mock the repository to return an empty result, simulating a user not found scenario
        when(userRepository.findUserByUserName("nonexistentuser")).thenReturn(Optional.empty());

        // Assert that a UsernameNotFoundException is thrown when the user is not found
        assertThrows(UsernameNotFoundException.class, () -> userDetailsService.loadUserByUsername("nonexistentuser"),
                "Expected UsernameNotFoundException to be thrown when the user is not found");

        // Verify that the login attempt service and repository methods were called
        verify(loginAttemptService).isBlocked();
        verify(userRepository).findUserByUserName("nonexistentuser");
    }

    @Test
    void testLoadUserByUsername_UserFound_ReturnsUserDetails() {
        // Mock the login attempt service to simulate that the user is not blocked
        when(loginAttemptService.isBlocked()).thenReturn(false);

        // Mock a user entity and return it when the repository is called
        User mockUser = new User();
        when(userRepository.findUserByUserName("testuser")).thenReturn(Optional.of(mockUser));

        // Call the method and assert the returned user details are correct
        UserDetails userDetails = userDetailsService.loadUserByUsername("testuser");
        assertNotNull(userDetails, "Expected non-null UserDetails object");

        // Verify that the login attempt service and repository methods were called
        verify(loginAttemptService).isBlocked();
        verify(userRepository).findUserByUserName("testuser");
    }
}
