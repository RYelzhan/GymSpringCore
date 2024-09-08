package com.epam.wca.gym.service;

import com.epam.wca.gym.service.impl.ProfileServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProfileServiceImplTest {
    @Mock
    private Map<String, Integer> usernameCounter;

    @InjectMocks
    private ProfileServiceImpl profileService;

    @ParameterizedTest
    @CsvSource({"John, Doe, John.Doe", "Jane, Smith, Jane.Smith"})
    void testCreateNewUserName(String firstName, String lastName, String expectedUsername) {
        when(usernameCounter.containsKey(expectedUsername))
                .thenReturn(false);
        assertEquals(expectedUsername, profileService.createUserName(firstName, lastName));
    }

    @ParameterizedTest
    @CsvSource({"John, Doe, John.Doe2", "Jane, Smith, Jane.Smith2"})
    void testCreateExistingUserName(String firstName, String lastName, String expectedUsername) {
        when(usernameCounter.containsKey(anyString()))
                .thenReturn(true);
        when(usernameCounter.get(anyString()))
                .thenReturn(1);
        assertEquals(expectedUsername, profileService.createUserName(firstName, lastName));
    }

    @Test
    void testCreatePassword() {
        String password = profileService.createPassword();

        assertEquals(10, password.length());
    }
}
