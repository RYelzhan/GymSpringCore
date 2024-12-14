package com.epam.wca.authentication.controller;

import com.epam.wca.authentication.entity.User;
import com.epam.wca.authentication.service.AuthService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthenticationController {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AuthService authService;

    private final User user = new User();

    @Test
    void testLogin() throws Exception {
        String expectedToken = "dummyToken";

        when(authService.generateToken(any(User.class))).thenReturn(expectedToken);

        mockMvc.perform(post("/authentication/login")
                        .with(user(user)))
                .andExpect(status().isOk())
                .andExpect(content().string("Login Successful. Token: " + expectedToken));
    }

    @Test
    void testLogout() throws Exception {
        mockMvc.perform(post("/authentication/logout")
                        .with(user(user)))
                .andExpect(status().isOk())
                .andExpect(content().string("Logout Successful"));
    }
}
