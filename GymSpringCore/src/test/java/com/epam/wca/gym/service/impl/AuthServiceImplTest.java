package com.epam.wca.gym.service.impl;

import com.epam.wca.gym.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private JwtService jwtService;
    @InjectMocks
    private AuthServiceImpl authService;

    @Test
    void testGenerateToken() {
        String token = "123456";
        when(jwtService.generateToken(any())).thenReturn(token);

        String generatedToken = authService.generateToken(null);

        assertEquals(token, generatedToken);
    }
}
