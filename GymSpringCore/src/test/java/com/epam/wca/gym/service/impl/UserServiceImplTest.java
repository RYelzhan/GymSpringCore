package com.epam.wca.gym.service.impl;

import com.epam.wca.gym.dto.user.UserActivationDTO;
import com.epam.wca.gym.entity.User;
import com.epam.wca.gym.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;
    private UserActivationDTO userActivationDTO;

    @BeforeEach
    public void setUp() {
        user = new User(); // Initialize as necessary
        userActivationDTO = new UserActivationDTO(true); // Adjust constructor as needed
    }

    @Test
    void testUpdateActivation() {
        // Given
        boolean isActive = true;
        userActivationDTO = new UserActivationDTO(isActive);

        // When
        userService.update(user, userActivationDTO);

        // Then
        Assertions.assertEquals(isActive, user.isActive());
        verify(userRepository).save(user);
    }

    @Test
    void testFindByUsername() {
        // Given
        String username = "testUser";
        when(userRepository.findUserByUsername(username)).thenReturn(Optional.of(user));

        // When
        User foundUser = userService.findByUsername(username);

        // Then
        assertNotNull(foundUser);
        Assertions.assertEquals(user, foundUser);
        verify(userRepository).findUserByUsername(username);
    }
}
