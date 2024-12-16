package com.epam.wca.gym.controller;

import com.epam.wca.common.gymcommon.auth_dto.UserAuthenticatedDTO;
import com.epam.wca.gym.controller.impl.AuthenticationControllerImpl;
import com.epam.wca.gym.dto.trainee.TraineeRegistrationDTO;
import com.epam.wca.gym.dto.trainer.TrainerRegistrationDTO;
import com.epam.wca.gym.entity.TrainingType;
import com.epam.wca.gym.interceptor.LoggingInterceptor;
import com.epam.wca.gym.interceptor.UserDetailsInterceptor;
import com.epam.wca.gym.repository.TrainingTypeRepository;
import com.epam.wca.gym.repository.UserRepository;
import com.epam.wca.gym.service.TraineeService;
import com.epam.wca.gym.service.TrainerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.util.Optional;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthenticationControllerImpl.class)
@EnableAspectJAutoProxy(proxyTargetClass = true)
class AuthenticationControllerImplTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TraineeService traineeService;

    @MockitoBean
    private TrainerService trainerService;

    @MockitoBean
    private LoggingInterceptor loggingInterceptor;

    @MockitoBean
    private UserDetailsInterceptor userDetailsInterceptor;

    @MockitoBean
    private TrainingTypeRepository trainingTypeRepository;

    @MockitoBean
    private UserRepository userRepository;

    private static final String TRAINEE_REGISTER_URL = "/authentication/account/trainee";
    private static final String TRAINER_REGISTER_URL = "/authentication/account/trainer";

    @BeforeEach
    void setUp() throws IOException {
        when(loggingInterceptor.preHandle(any(), any(), any())).thenReturn(true);
        when(userDetailsInterceptor.preHandle(any(), any(), any())).thenReturn(true);
    }

    @Test
    void testRegisterTrainee_ValidRequest_ShouldReturnCreated() throws Exception {
        String username = "username";
        String password = "password";

        var userAuthenticatedDTO = new UserAuthenticatedDTO(username, password);

        when(traineeService.save(any(TraineeRegistrationDTO.class))).thenReturn(userAuthenticatedDTO);

        mockMvc.perform(post(TRAINEE_REGISTER_URL)
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

    static Stream<Arguments> provideInvalidTraineeData() {
        return Stream.of(
                Arguments.of("Missing lastname",
                        """
                            {
                                Missing required last name,
                                {
                                "firstName": "John",
                                "dateOfBirth": "01.01.1990 10:10:10 UTC",
                                "address": "123 Main St"
                                }
                            }
                        """
                ),
                Arguments.of("Missing firstname", """
                            {
                                Missing required first name,
                                {
                                "lastName": "Smith",
                                "dateOfBirth": "01.01.1990 10:10:10 UTC",
                                "address": "123 Main St"
                                }
                            }
                        """
                ),
                Arguments.of("Invalid date format",
                        """
                            {
                                "firstName": "John",
                                "lastName": "Doe",
                                "dateOfBirth": "1990-01-01T10:10:10",
                                "address": "123 Main St"
                            }
                        """
                ),
                Arguments.of("Future date of birth",
                        """
                            {
                                "firstName": "John",
                                "lastName": "Doe",
                                "dateOfBirth": "01.01.3000 10:10:10 UTC",
                                "address": "123 Main St"
                            }
                        """
                ),
                Arguments.of("Empty first name",
                        """
                            {
                                "firstName": "",
                                "lastName": "Doe",
                                "dateOfBirth": "01.01.1990 10:10:10 UTC",
                                "address": "123 Main St"
                            }
                        """
                ),
                Arguments.of("Empty last name",
                        """
                            {
                                "firstName": "John",
                                "lastName": "",
                                "dateOfBirth": "01.01.1990 10:10:10 UTC",
                                "address": "123 Main St"
                            }
                        """
                ),
                Arguments.of("Exceeding first name length", """
                            {
                                "firstName": "%s",
                                "lastName": "Doe",
                                "dateOfBirth": "01.01.1990 10:10:10 UTC",
                                "address": "123 Main St"
                            }
                        """.formatted("John".repeat(100))
                ),
                Arguments.of("Exceeding last name length", """
                            {
                                "firstName": "John",
                                "lastName": "%s",
                                "dateOfBirth": "01.01.1990 10:10:10 UTC",
                                "address": "123 Main St"
                            }
                        """.formatted("Doe".repeat(100))
                ),
                Arguments.of("Exceeding address length", """
                            {
                                "firstName": "John",
                                "lastName": "Doe",
                                "dateOfBirth": "01.01.1990 10:10:10 UTC",
                                "address": "%s"
                            }
                        """.formatted("address".repeat(100))
                )

        );
    }

    @ParameterizedTest(name = "{index} - {0}")
    @MethodSource("provideInvalidTraineeData")
    void testRegisterTrainee_BadRequest_ShouldReturnBadRequest(String testName, String body) throws Exception {
        mockMvc.perform(post(TRAINEE_REGISTER_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testRegisterTrainer_ValidRequest_ShouldReturnCreated() throws Exception {
        String username = "username";
        String password = "password";

        var userAuthenticatedDTO = new UserAuthenticatedDTO(username, password);

        when(trainerService.save(any(TrainerRegistrationDTO.class)))
                .thenReturn(userAuthenticatedDTO);
        when(trainingTypeRepository.findTrainingTypeByType(anyString()))
                .thenReturn(Optional.of(new TrainingType()));

        mockMvc.perform(post(TRAINER_REGISTER_URL)
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

    static Stream<Arguments> provideInvalidTrainerData() {
        return Stream.of(
                Arguments.of("Invalid training type", """
                            {
                                "firstName": "Jane",
                                "lastName": "Smith",
                                "trainingType": "INVALID_TYPE"
                            }
                        """
                ),
                Arguments.of("Missing required last name", """
                            {
                                "firstName": "Jane",
                                "trainingType": "VALID"
                            }
                        """
                ),
                Arguments.of("Missing required first name", """
                            {
                                "lastName": "Smith",
                                "trainingType": "VALID"
                            }
                        """
                ),
                Arguments.of("Missing required training type", """
                            {
                                "firstName": "Jane",
                                "lastName": "Smith"
                            }
                        """
                ),
                Arguments.of("Empty last name", """
                            {
                                "firstName": "Jane",
                                "lastName": "",
                                "trainingType": "VALID"
                            }
                        """
                ),
                Arguments.of("Empty first name", """
                            {
                                "firstName": "",
                                "lastName": "Smith",
                                "trainingType": "VALID"
                            }
                        """
                ),
                Arguments.of("Empty training type", """
                            {
                                "firstName": "Jane",
                                "lastName": "Smith",
                                "trainingType": ""
                            }
                        """
                ),
                Arguments.of("Exceeding training type length", """
                            {
                                "firstName": "Jane",
                                "lastName": "Smith",
                                "trainingType": "%s"
                            }
                        """.formatted("VALID".repeat(100))
                )
        );
    }

    @ParameterizedTest(name = "{index} - {0}")
    @MethodSource("provideInvalidTrainerData")
    void testRegisterTrainer_BadRequest_ShouldReturnBadRequest(String testName, String body) throws Exception {
        when(trainingTypeRepository.findTrainingTypeByType("INVALID_TYPE"))
                .thenReturn(Optional.empty());
        when(trainingTypeRepository.findTrainingTypeByType("VALID"))
                .thenReturn(Optional.of(new TrainingType()));

        mockMvc.perform(post(TRAINER_REGISTER_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest());
    }
}
