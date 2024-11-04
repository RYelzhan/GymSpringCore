package com.epam.wca.gym.controller;

import com.epam.wca.gym.entity.User;
import com.epam.wca.gym.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerImplTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    private final User user = new User();

    @Test
    void testChangePassword_ValidRequest() throws Exception {
        mockMvc.perform(put("/users/password")
                        .with(user(user))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                             "newPassword": "ValidPass11"
                        }
                        """))
                .andExpect(status().isOk());
    }

    @Test
    void testChangePassword_NewPasswordTooShort_ShouldReturnBadRequest() throws Exception {
        mockMvc.perform(put("/users/password")
                        .with(user(user))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                             "newPassword": "ShortPass"
                        }
                        """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testChangePassword_NewPasswordTooLong_ShouldReturnBadRequest() throws Exception {
        mockMvc.perform(put("/users/password")
                        .with(user(user))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                             "newPassword": "PasswordThatIsTooLong"
                        }
                        """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testChangePassword_NewPasswordEmpty_ShouldReturnBadRequest() throws Exception {
        mockMvc.perform(put("/users/password")
                        .with(user(user))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                             "newPassword": ""
                        }
                        """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testChangePassword_NewPasswordNull_ShouldReturnBadRequest() throws Exception {
        mockMvc.perform(put("/users/password")
                        .with(user(user))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                             
                        }
                        """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testActivateDeactivate_Active_ValidRequest() throws Exception {
        mockMvc.perform(patch("/users/active")
                        .with(user(user))
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
        mockMvc.perform(patch("/users/active")
                        .with(user(user))
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
                        .with(user(user))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                             
                        }
                        """))
                .andExpect(status().isBadRequest());
    }
}
