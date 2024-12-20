package com.epam.wca.gym.controller;

import com.epam.wca.gym.controller.impl.UserControllerImpl;
import com.epam.wca.gym.entity.User;
import com.epam.wca.gym.interceptor.LoggingInterceptor;
import com.epam.wca.gym.interceptor.UserDetailsInterceptor;
import com.epam.wca.gym.repository.UserRepository;
import com.epam.wca.gym.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserControllerImpl.class)
@EnableAspectJAutoProxy(proxyTargetClass = true)
class UserControllerImplTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private LoggingInterceptor loggingInterceptor;

    @MockitoBean
    private UserDetailsInterceptor userDetailsInterceptor;

    @MockitoBean
    private UserRepository userRepository;

    private final User user = new User();

    @BeforeEach
    void setUp() throws IOException {
        when(loggingInterceptor.preHandle(any(), any(), any())).thenReturn(true);
        when(userDetailsInterceptor.preHandle(any(), any(), any())).thenReturn(true);
        when(userRepository.findUserByUsername(any())).thenReturn(Optional.of(user));
    }

    @Test
    void testActivateDeactivate_Active_ValidRequest() throws Exception {
        when(userRepository.findUserByUsername(anyString()))
                .thenReturn(Optional.of(user));

        mockMvc.perform(patch("/users/active")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                             "isActive": true
                        }
                        """))
                .andExpect(status().isOk());
    }

    @Test
    void testActivateDeactivate_Deactivate_ValidRequest() throws Exception {
        when(userRepository.findUserByUsername(anyString()))
                .thenReturn(Optional.of(user));

        mockMvc.perform(patch("/users/active")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                             "isActive": false
                        }
                        """))
                .andExpect(status().isOk());
    }

    @Test
    void testActivateDeactivate_isActiveNull_ShouldReturnBadRequest() throws Exception {
        mockMvc.perform(patch("/users/active")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                             
                        }
                        """))
                .andExpect(status().isBadRequest());
    }
}
