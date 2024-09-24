package com.epam.wca.gym.service;

import com.epam.wca.gym.entity.Username;
import com.epam.wca.gym.repository.impl.UsernameDAO;
import com.epam.wca.gym.service.impl.ProfileServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProfileServiceImplTest {
    @Mock
    private UsernameDAO usernameDAO;

    @InjectMocks
    private ProfileServiceImpl profileService;

    @ParameterizedTest
    @CsvSource({"John, Doe, John.Doe2", "Jane, Smith, Jane.Smith2"})
    void testCreateUserName_WhenUsernameExists(String firstName,
                                               String lastName,
                                               String expected) {
        String baseUsername = firstName + "." + lastName;

        // Given
        Username existingUsername = new Username(baseUsername, 1L);

        // Mocking usernameDAO behavior
        when(usernameDAO.findByUniqueName(baseUsername)).thenReturn(existingUsername);

        // When
        String generatedUsername = profileService.createUserName(firstName, lastName);

        // Then
        assertEquals(expected, generatedUsername); // Check if the counter was incremented
        Mockito.verify(usernameDAO).findByUniqueName(baseUsername);
        Mockito.verify(usernameDAO).save(existingUsername); // Make sure it was saved
    }

    @ParameterizedTest
    @CsvSource({"John, Doe, John.Doe", "Jane, Smith, Jane.Smith"})
    void testCreateUserName_WhenUsernameDoesNotExist(String firstName,
                                                     String lastName,
                                                     String expected) {
        // Given
        String baseUsername = firstName + "." + lastName;

        // Mocking the case where no username is found
        when(usernameDAO.findByUniqueName(baseUsername)).thenThrow(new RuntimeException());

        // When
        String generatedUsername = profileService.createUserName(firstName, lastName);

        // Then
        assertEquals(expected, generatedUsername); // The new username should be the base one
        Mockito.verify(usernameDAO).findByUniqueName(baseUsername);
        Mockito.verify(usernameDAO).save(any(Username.class)); // Verify that a new username was saved
    }

    @Test
    void testCreatePassword() {
        String password = profileService.createPassword();

        assertEquals(10, password.length());
    }
}
