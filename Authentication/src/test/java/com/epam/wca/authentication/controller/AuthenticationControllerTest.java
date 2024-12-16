package com.epam.wca.authentication.controller;

import com.epam.wca.authentication.advice.AccessDeniedHandlerImpl;
import com.epam.wca.authentication.advice.RestAuthenticationEntryPoint;
import com.epam.wca.authentication.config.SecurityConfig;
import com.epam.wca.authentication.controller.impl.AuthenticationControllerImpl;
import com.epam.wca.authentication.entity.Role;
import com.epam.wca.authentication.entity.User;
import com.epam.wca.authentication.interceptor.LoggingInterceptor;
import com.epam.wca.authentication.service.AuthService;
import com.epam.wca.authentication.service.impl.JwtService;
import com.epam.wca.authentication.service.impl.LoginAttemptService;
import com.epam.wca.authentication.service.impl.UserService;
import com.epam.wca.common.gymcommon.auth_dto.UserAuthenticatedDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.util.Set;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthenticationControllerImpl.class)
@EnableAspectJAutoProxy(proxyTargetClass = true)
@Import(SecurityConfig.class)
public class AuthenticationControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AuthService authService;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private LoggingInterceptor loggingInterceptor;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private UserDetailsService userDetailsService;

    @MockitoBean
    private ApplicationEventPublisher applicationEventPublisher;

    @MockitoBean
    private LoginAttemptService loginAttemptService;

    @MockitoBean
    private RestAuthenticationEntryPoint restAuthenticationEntryPoint;

    @MockitoBean
    private AccessDeniedHandlerImpl accessDeniedHandler;

    private final User user = new User(
            "user.name",
            "password",
            Set.of(new Role("ADMIN"))
    );

    @BeforeEach
    void setUp() throws IOException {
        when(loggingInterceptor.preHandle(any(), any(), any())).thenReturn(true);
    }

    @Test
    void testAuthenticate() throws Exception {
        String username = "Dummy.Username";
        user.setUsername(username);

        mockMvc.perform(get("/authenticate")
                        .with(user(user)))
                .andExpect(status().isOk())
                .andExpect(content().string(username));
    }

    @Test
    void testLogin() throws Exception {
        String expectedToken = "dummyToken";

        when(authService.generateToken(user)).thenReturn(expectedToken);

        mockMvc.perform(post("/login")
                        .with(user(user)))
                .andDo(mvcResult -> {
                    System.out.println(mvcResult.getResponse().getContentAsString());
                })
                .andExpect(status().isOk())
                .andExpect(content().string("Login Successful. Token: " + expectedToken));
    }

    @Test
    void testDelete() throws Exception {
        mockMvc.perform(delete("/delete")
                        .with(user(user))
                        .contentType(MediaType.ALL_VALUE))
                .andExpect(status().isNoContent());
    }

    static Stream<Arguments> provideInvalidUserData() {
        return Stream.of(
                Arguments.of(
                        "Missing firstname",
                        """
                        {
                            "lastname": "Doe",
                            "roles": ["TRAINER"]
                        }
                        """
                ),
                Arguments.of(
                        "Missing lastname",
                        """
                        {
                            "firstname": "John",
                            "roles": ["TRAINER"]
                        }
                        """
                ),
                Arguments.of(
                        "Missing roles",
                        """
                        {
                            "firstname": "John",
                            "lastname": "Doe"
                        }
                        """
                ),
                Arguments.of(
                        "Empty firstname",
                        """
                        {
                            "firstname": "",
                            "lastname": "Doe",
                            "roles": ["TRAINER"]
                        }
                        """
                ),
                Arguments.of(
                        "Empty lastname",
                        """
                        {
                            "firstname": "John",
                            "lastname": "",
                            "roles": ["TRAINER"]
                        }
                        """
                ),
                Arguments.of(
                        "Empty roles",
                        """
                        {
                            "firstname": "John",
                            "lastname": "Doe",
                            "roles": []
                        }
                        """
                ),
                Arguments.of(
                        "Firstname too short",
                        """
                        {
                            "firstname": "J",
                            "lastname": "Doe",
                            "roles": ["TRAINER"]
                        }
                        """
                ),
                Arguments.of(
                        "Firstname too long",
                        """
                        {
                            "firstname": "ThisFirstnameIsWayTooLongForValidation",
                            "lastname": "Doe",
                            "roles": ["TRAINER"]
                        }
                        """
                ),
                Arguments.of(
                        "Lastname too short",
                        """
                        {
                            "firstname": "John",
                            "lastname": "D",
                            "roles": ["TRAINER"]
                        }
                        """
                ),
                Arguments.of(
                        "Lastname too long",
                        """
                        {
                            "firstname": "John",
                            "lastname": "ThisLastnameIsWayTooLongForValidation",
                            "roles": ["TRAINER"]
                        }
                        """
                ),
                Arguments.of(
                        "Role too short",
                        """
                        {
                            "firstname": "John",
                            "lastname": "Doe",
                            "roles": [""]
                        }
                        """
                ),
                Arguments.of(
                        "Role too long",
                        """
                        {
                            "firstname": "John",
                            "lastname": "Doe",
                            "roles": ["ThisRoleNameIsWayTooLong"]
                        }
                        """
                ),
                Arguments.of(
                        "Role is null",
                        """
                        {
                            "firstname": "John",
                            "lastname": "Doe",
                            "roles": [null]
                        }
                        """
                )
        );
    }

    @ParameterizedTest(name = "{index} - {0}")
    @MethodSource(value = "provideInvalidUserData")
    void testRegister_BadRequest_ShouldReturnBadRequest(String testName, String body) throws Exception {
        mockMvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testRegister_ValidRequest() throws Exception {
        var authenticatedDTO = new UserAuthenticatedDTO(
                "valid.username",
                "newPassword"
        );

        when(userService.create(any())).thenReturn(authenticatedDTO);

        mockMvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                            "firstname": "John",
                            "lastname": "Doe",
                            "roles": ["TRAINEE"]
                        }
                        """))
                .andExpect(status().isCreated());
    }

    static Stream<Arguments> provideInvalidNewPasswordData() {
        return Stream.of(
                Arguments.of(
                        "Missing newPassword",
                        """
                        {
                        }
                        """
                ),
                Arguments.of(
                        "Empty newPassword",
                        """
                        {
                            "newPassword": ""
                        }
                        """
                ),
                Arguments.of(
                        "New password too short",
                        """
                        {
                            "newPassword": "short"
                        }
                        """
                ),
                Arguments.of(
                        "New password too long",
                        """
                        {
                            "newPassword": "ThisPasswordIsWayTooLong"
                        }
                        """
                ),
                Arguments.of(
                        "New password null",
                        """
                        {
                            "newPassword": null
                        }
                        """
                )
        );
    }

    @ParameterizedTest(name = "{index} - {0}")
    @MethodSource(value = "provideInvalidNewPasswordData")
    void testChangePassword_BadRequest_ShouldReturnBadRequest(String testName, String body) throws Exception {
        mockMvc.perform(put("/password")
                        .with(user(user))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testChangePassword_ValidRequest() throws Exception {
        mockMvc.perform(put("/password")
                        .with(user(user))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                            "newPassword": "newPassword"
                        }
                        """))
                .andExpect(status().isOk());
    }
}
