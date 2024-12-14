package com.epam.wca.authentication.service.impl;

import com.epam.wca.authentication.communication.GymCommunicationService;
import com.epam.wca.authentication.dto.UserUpdateDTO;
import com.epam.wca.authentication.entity.Role;
import com.epam.wca.authentication.entity.User;
import com.epam.wca.authentication.repository.RoleRepository;
import com.epam.wca.authentication.repository.UserRepository;
import com.epam.wca.common.gymcommon.auth_dto.UserAuthenticatedDTO;
import com.epam.wca.common.gymcommon.auth_dto.UserRegistrationDTO;
import com.epam.wca.common.gymcommon.exception.InternalErrorException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private GymCommunicationService gymCommunicationService;

    @Mock
    private ProfileServiceImpl profileService;

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private UserService userService;

    private User user;
    private UserUpdateDTO userUpdateDTO;

    @BeforeEach
    public void setUp() {
        user = new User(); // Initialize as necessary
        userUpdateDTO = new UserUpdateDTO("newPassword123"); // Adjust constructor as needed
    }

    @Test
    void testUpdatePassword() {
        // Given
        String newPassword = "newEncodedPassword";
        userUpdateDTO = new UserUpdateDTO(newPassword);

        when(passwordEncoder.encode(any())).thenReturn(newPassword);

        // When
        userService.update(user, userUpdateDTO);

        // Then
        assertEquals(newPassword, user.getPassword());
        verify(userRepository).save(user);
    }

    @Test
    void testCreate() {
        // Arrange
        UserRegistrationDTO userRegistrationDTO = new UserRegistrationDTO(
                "John",
                "Doe",
                Set.of("TRAINEE")
        );
        Role role = new Role("TRAINEE");
        when(roleRepository.findRoleByName("TRAINEE")).thenReturn(Optional.of(role));
        when(profileService.createUsername("John", "Doe")).thenReturn("john.doe");
        when(profileService.createPassword()).thenReturn("randomPassword");
        when(passwordEncoder.encode("randomPassword")).thenReturn("encodedPassword");

        // Act
        UserAuthenticatedDTO result = userService.create(userRegistrationDTO);

        // Assert
        assertEquals("john.doe", result.username());
        assertEquals("randomPassword", result.password());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void testCreate_WithNonExistentRole() {
        // Arrange
        UserRegistrationDTO userRegistrationDTO = new UserRegistrationDTO(
                "John",
                "Doe",
                Set.of("NON_EXISTENT_ROLE")
        );
        when(roleRepository.findRoleByName("NON_EXISTENT_ROLE")).thenReturn(Optional.empty());

        // Act & Assert
        InternalErrorException exception = assertThrows(InternalErrorException.class, () -> {
            userService.create(userRegistrationDTO);
        });
        assertEquals("User trying to register with non-existent role.", exception.getMessage());
    }

    @Test
    void testDeleteTrainee() {
        // Arrange
        Role traineeRole = new Role("TRAINEE");
        User user = new User("traineeUser", "password", Set.of(traineeRole));

        // Act
        userService.delete(user);

        // Assert
        verify(userRepository).delete(user);
        verify(gymCommunicationService).deleteTrainee("traineeUser");
    }

    @Test
    void testDeleteTrainer() {
        // Arrange
        Role trainerRole = new Role("TRAINER");
        User user = new User("trainerUser", "password", Set.of(trainerRole));

        // Act
        userService.delete(user);

        // Assert
        verify(userRepository).delete(user);
        verify(gymCommunicationService).deleteTrainer("trainerUser");
    }
}
