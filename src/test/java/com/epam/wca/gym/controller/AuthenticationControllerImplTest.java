package com.epam.wca.gym.controller;

import com.epam.wca.gym.dto.trainee.TraineeRegistrationDTO;
import com.epam.wca.gym.dto.trainer.TrainerRegistrationDTO;
import com.epam.wca.gym.dto.user.UserAuthenticatedDTO;
import com.epam.wca.gym.entity.User;
import com.epam.wca.gym.service.AuthService;
import com.epam.wca.gym.service.TraineeService;
import com.epam.wca.gym.service.TrainerService;
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

    private final User user = new User();

    private final String traineeRegisterUrl = "/authentication/account/trainee";
    private final String trainerRegisterUrl = "/authentication/account/trainer";

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
    void testRegisterTrainee_ValidRequest_ShouldReturnCreated() throws Exception {
        String username = "username";
        String password = "password";

        var userAuthenticatedDTO = new UserAuthenticatedDTO(username, password);

        when(traineeService.save(any(TraineeRegistrationDTO.class))).thenReturn(userAuthenticatedDTO);

        mockMvc.perform(post(traineeRegisterUrl)
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
                    "password": %s
                }
            """.formatted(username, password)));
    }

    @Test
    void testRegisterTrainee_MissingRequiredLastname_ShouldReturnBadRequest() throws Exception {
        mockMvc.perform(post(traineeRegisterUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                    {
                        "firstName": "John",
                        "dateOfBirth": "01.01.1990 10:10:10 UTC",
                        "address": "123 Main St"
                    }
                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testRegisterTrainee_MissingRequiredFirstname_ShouldReturnBadRequest() throws Exception {
        mockMvc.perform(post(traineeRegisterUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                    {
                        "lastname": "Smith",
                        "dateOfBirth": "01.01.1990 10:10:10 UTC",
                        "address": "123 Main St"
                    }
                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testRegisterTrainee_InvalidDateFormat_ShouldReturnBadRequest() throws Exception {
        mockMvc.perform(post(traineeRegisterUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                            "firstName": "John",
                            "lastName": "Doe",
                            "dateOfBirth": "1990-01-01T10:10:10",
                            "address": "123 Main St"
                        }
                    """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testRegisterTrainee_FutureDate_ShouldReturnBadRequest() throws Exception {
        mockMvc.perform(post(traineeRegisterUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                            "firstName": "John",
                            "lastName": "Doe",
                            "dateOfBirth": "01.01.3000 10:10:10 UTC",
                            "address": "123 Main St"
                        }
                    """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testRegisterTrainee_EmptyFirstname_ShouldReturnBadRequest() throws Exception {
        mockMvc.perform(post(traineeRegisterUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                            "firstName": "",
                            "lastName": "Doe",
                            "dateOfBirth": "01.01.1990 10:10:10 UTC",
                            "address": "123 Main St"
                        }
                    """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testRegisterTrainee_EmptyLastname_ShouldReturnBadRequest() throws Exception {
        mockMvc.perform(post(traineeRegisterUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                            "firstName": "John",
                            "lastName": "",
                            "dateOfBirth": "01.01.1990 10:10:10 UTC",
                            "address": "123 Main St"
                        }
                    """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testRegisterTrainee_ExceedingFirstnameLength_ShouldReturnBadRequest() throws Exception {
        String longFirstName = "John".repeat(100); // Exceeds typical length constraints
        mockMvc.perform(post(traineeRegisterUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                            "firstName": "%s",
                            "lastName": "Doe",
                            "dateOfBirth": "01.01.1990 10:10:10 UTC",
                            "address": "123 Main St"
                        }
                    """.formatted(longFirstName)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testRegisterTrainee_ExceedingLastnameLength_ShouldReturnBadRequest() throws Exception {
        String longLastName = "Doe".repeat(100); // Exceeds typical length constraints
        mockMvc.perform(post(traineeRegisterUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                            "firstName": "John",
                            "lastName": "%s",
                            "dateOfBirth": "01.01.1990 10:10:10 UTC",
                            "address": "123 Main St"
                        }
                    """.formatted(longLastName)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testRegisterTrainee_ExceedingAddressLength_ShouldReturnBadRequest() throws Exception {
        String longAddress = "address".repeat(100); // Exceeds typical length constraints
        mockMvc.perform(post(traineeRegisterUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                            "firstName": "John",
                            "lastName": "Doe",
                            "dateOfBirth": "01.01.1990 10:10:10 UTC",
                            "address": "%s"
                        }
                    """.formatted(longAddress)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testRegisterTrainer_ValidRequest_ShouldReturnCreated() throws Exception {
        String username = "username";
        String password = "password";

        var userAuthenticatedDTO = new UserAuthenticatedDTO(username, password);

        when(trainerService.save(any(TrainerRegistrationDTO.class))).thenReturn(userAuthenticatedDTO);

        mockMvc.perform(post(trainerRegisterUrl)
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

    @Test
    void testRegisterTrainer_InvalidTrainingType_ShouldReturnBadRequest() throws Exception {
        mockMvc.perform(post(trainerRegisterUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                            "firstName": "Jane",
                            "lastName": "Smith",
                            "trainingType": "INVALID_TYPE"
                        }
                    """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testRegisterTrainer_MissingRequiredLastname_ShouldReturnBadRequest() throws Exception {
        mockMvc.perform(post(trainerRegisterUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                            "firstName": "Jane",
                            "trainingType": "YOGA"
                        }
                    """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testRegisterTrainer_MissingRequiredFirstname_ShouldReturnBadRequest() throws Exception {
        mockMvc.perform(post(trainerRegisterUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                            "lastname": "Smith",
                            "trainingType": "YOGA"
                        }
                    """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testRegisterTrainer_MissingRequiredTrainingType_ShouldReturnBadRequest() throws Exception {
        mockMvc.perform(post(trainerRegisterUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                            "firstName": "Jane",
                            "lastname": "Smith"
                        }
                    """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testRegisterTrainer_EmptyLastname_ShouldReturnBadRequest() throws Exception {
        mockMvc.perform(post(trainerRegisterUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                            "firstName": "Jane",
                            "lastName": "",
                            "trainingType": "YOGA"
                        }
                    """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testRegisterTrainer_EmptyFirstname_ShouldReturnBadRequest() throws Exception {
        mockMvc.perform(post(trainerRegisterUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                            "firstName": "",
                            "lastName": "Smith",
                            "trainingType": "YOGA"
                        }
                    """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testRegisterTrainer_EmptyTrainingType_ShouldReturnBadRequest() throws Exception {
        mockMvc.perform(post(trainerRegisterUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                            "firstName": "Jane",
                            "lastName": "Smith",
                            "trainingType": ""
                        }
                    """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testRegisterTrainee_ExceedingTrainingTypeLength_ShouldReturnBadRequest() throws Exception {
        String longTrainingType = "YOGA".repeat(100); // Exceeds typical length constraints
        mockMvc.perform(post(trainerRegisterUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                            "firstName": "Jane",
                            "lastName": "Smith",
                            "trainingType": "%s"
                        }
                    """.formatted(longTrainingType)))
                .andExpect(status().isBadRequest());
    }
}
