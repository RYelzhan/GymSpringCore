package com.epam.wca.gym.service;

import com.epam.wca.gym.service.impl.ProfileServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProfileServiceImplTest {
    @Mock
    private Map<String, Integer> usernameCounter;

    @InjectMocks
    private ProfileServiceImpl profileService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateUserName_NewUser() {
        String firstName = "John";
        String lastName = "Doe";
        String expectedUsername = "John.Doe";

        when(usernameCounter.containsKey(expectedUsername)).thenReturn(false);

        String actualUsername = profileService.createUserName(firstName, lastName);

        assertEquals(expectedUsername, actualUsername);
    }

    @Test
    void testCreateUserName_ExistingUser() {
        String firstName = "John";
        String lastName = "Doe";
        String baseUsername = "John.Doe";
        int existingCount = 1;

        when(usernameCounter.containsKey(baseUsername)).thenReturn(true);
        when(usernameCounter.get(baseUsername)).thenReturn(existingCount);

        String expectedUsername = baseUsername + (existingCount + 1);
        String actualUsername = profileService.createUserName(firstName, lastName);

        assertEquals(expectedUsername, actualUsername);
    }

    @Test
    void testCreatePassword() {
        String password = profileService.createPassword();

        assertEquals(10, password.length());
    }
}
