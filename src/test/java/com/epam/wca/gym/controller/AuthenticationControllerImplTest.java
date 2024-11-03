package com.epam.wca.gym.controller;

import com.epam.wca.gym.dto.trainee.TraineeRegistrationDTO;
import com.epam.wca.gym.dto.trainer.TrainerRegistrationDTO;
import com.epam.wca.gym.dto.user.UserAuthenticatedDTO;
import com.epam.wca.gym.entity.User;
import com.epam.wca.gym.service.AuthService;
import com.epam.wca.gym.service.TraineeService;
import com.epam.wca.gym.service.TrainerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AuthenticationControllerImplTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    @MockBean
    private TraineeService traineeService;

    @MockBean
    private TrainerService trainerService;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
    }

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

    @Test
    void testRegisterTrainee() throws Exception {
        String username = "username";
        String password = "password";

        var userAuthenticatedDTO = new UserAuthenticatedDTO(username, password);

        when(traineeService.save(any(TraineeRegistrationDTO.class))).thenReturn(userAuthenticatedDTO);

        mockMvc.perform(post("/authentication/account/trainee")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                            "firstName": "John",
                            "lastName": "Doe",
                            "dateOfBirth": "01.01.1990 10:10:10 UTC",
                            "address": "123 Main St"
                        }
                    """))
                .andExpect(status().isCreated())
                .andExpect(content().json("""
                {
                    "username": %s,
                    "password": %s"
                }
            """.formatted(username, password)));
    }

    @Test
    void testRegisterTrainer() throws Exception {
        String username = "username";
        String password = "password";

        var userAuthenticatedDTO = new UserAuthenticatedDTO(username, password);

        when(trainerService.save(any(TrainerRegistrationDTO.class))).thenReturn(userAuthenticatedDTO);

        mockMvc.perform(post("/authentication/account/trainer")
                        .contentType("application/json")
                        .content("""
                        {
                            "firstName": "Jane",
                            "lastName": "Smith",
                            "trainingType": "YOGA"
                        }
                    """))
                .andExpect(status().isCreated())
                .andExpect(content().json("""
                {
                    "username": %s,
                    "password": %s
                }
            """.formatted(username, password)));
    }
}
