package com.epam.wca.gym.service.impl;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LoginAttemptServiceTest {

    @Mock
    private HttpServletRequest mockRequest;

    @InjectMocks
    private LoginAttemptService loginAttemptService;

    @Test
    void testLoginFailed_IncrementsAttempts() {
        final String ip = "127.0.0.1";

        when(mockRequest.getRemoteAddr()).thenReturn(ip);
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(mockRequest));

        // Simulate 3 failed login attempts
        loginAttemptService.loginFailed(ip);
        loginAttemptService.loginFailed(ip);
        loginAttemptService.loginFailed(ip);

        // The user should now be blocked
        assertTrue(
                loginAttemptService.isBlocked(),
                "The user should be blocked after 3 failed attempts"
        );
    }

    @Test
    void testLoginFailed_BlocksAfterMaxAttempts() {
        final String ip = "127.0.0.2";

        when(mockRequest.getRemoteAddr()).thenReturn(ip);
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(mockRequest));

        // Simulate 2 failed attempts (not yet blocked)
        loginAttemptService.loginFailed(ip);
        loginAttemptService.loginFailed(ip);
        assertFalse(
                loginAttemptService.isBlocked(),
                "The user should not be blocked after 2 attempts"
        );

        // Simulate the 3rd failed attempt (blocked now)
        loginAttemptService.loginFailed(ip);
        assertTrue(
                loginAttemptService.isBlocked(),
                "The user should be blocked after 3 failed attempts"
        );
    }

    @Test
    void testIsBlocked_ResetsAfterBlockTime() {
        final String ip = "127.0.0.3";

        when(mockRequest.getRemoteAddr()).thenReturn(ip);
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(mockRequest));

        // Simulate 3 failed login attempts (block user)
        loginAttemptService.loginFailed(ip);
        loginAttemptService.loginFailed(ip);
        loginAttemptService.loginFailed(ip);

        // Set block time in the past to simulate expiration
        loginAttemptService.getBlockTime().put(ip, new Date(System.currentTimeMillis() - 1));

        // User should no longer be blocked since block time has expired
        assertFalse(
                loginAttemptService.isBlocked(),
                "The user should no longer be blocked after block time expires"
        );
    }

    @Test
    void testGetClientIP_WithXForwardedFor() {
        String expectedIp = "192.168.1.1";

        when(mockRequest.getHeader("X-Forwarded-For")).thenReturn(expectedIp + ", 192.168.1.2");
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(mockRequest));

        String clientIp = loginAttemptService.getClientIP();
        assertEquals(
                expectedIp,
                clientIp,
                "The client IP should be the first entry in X-Forwarded-For header"
        );
    }

    @Test
    void testGetClientIP_WithoutXForwardedFor() {
        String remoteAddr = "10.0.0.1";

        when(mockRequest.getRemoteAddr()).thenReturn(remoteAddr);
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(mockRequest));

        String clientIp = loginAttemptService.getClientIP();
        assertEquals(
                remoteAddr,
                clientIp,
                "The client IP should be from getRemoteAddr if X-Forwarded-For is missing"
        );
    }
}
