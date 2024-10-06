package com.epam.wca.gym.service.impl;

import com.epam.wca.gym.entity.User;
import com.epam.wca.gym.repository.UserRepository;
import com.epam.wca.gym.service.impl.AuthServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthServiceImplTest {
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private AuthServiceImpl authService;

    @Test
    public void testNullAuthenticationHeader() {
        String header = null;

        assertThrows(IllegalArgumentException.class, () -> authService.authenticate(header));
    }

    @Test
    public void testNotBasicAuthentication() {
        String header = "Oauth ";

        assertThrows(IllegalArgumentException.class, () -> authService.authenticate(header));
    }

    @ParameterizedTest
    @CsvSource({"Basic VGVzdDpUZXN0OlRlc3Q="})
    public void testCredentialsCountNot2(String header) {
        assertThrows(IllegalArgumentException.class, () -> authService.authenticate(header));
    }

    @ParameterizedTest
    @CsvSource({"Basic VGVzdDp0ZXN0"})
    public void testUserNotFound(String header) {
        when(userRepository.findUserByUserName(anyString())).thenReturn(null);

        assertThrows(IllegalArgumentException.class, () -> authService.authenticate(header));
    }

    @ParameterizedTest
    @CsvSource({"Basic VGVzdDoxMjM0NTY3ODkx, 1234567890"})
    public void testPasswordNotCorrect(String header, String userPassword) {
        User user = new User(
                null,
                null,
                null,
                userPassword,
                true
        );

        when(userRepository.findUserByUserName(anyString())).thenReturn(user);

        assertThrows(IllegalArgumentException.class, () -> authService.authenticate(header));
    }

    @ParameterizedTest
    @CsvSource({"Basic VGVzdDoxMjM0NTY3ODkw, 1234567890"})
    public void testSuccessfulAuthentication(String header, String userPassword) {
        User user = new User(
                null,
                null,
                null,
                userPassword,
                true
        );

        when(userRepository.findUserByUserName(anyString())).thenReturn(user);

        User authenticatedUser = authService.authenticate(header);

        Mockito.verify(userRepository, times(1)).findUserByUserName(anyString());
        assertEquals(authenticatedUser, user);
    }
}
