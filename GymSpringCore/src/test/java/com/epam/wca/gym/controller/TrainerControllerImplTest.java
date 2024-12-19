package com.epam.wca.gym.controller;

import com.epam.wca.gym.controller.impl.TrainerControllerImpl;
import com.epam.wca.gym.dto.trainer.TrainerSendDTO;
import com.epam.wca.gym.dto.trainer.TrainerUpdateDTO;
import com.epam.wca.gym.dto.training.TrainerTrainingQuery;
import com.epam.wca.gym.dto.training.TrainingBasicDTO;
import com.epam.wca.gym.entity.Trainee;
import com.epam.wca.gym.entity.Trainer;
import com.epam.wca.gym.entity.TrainingType;
import com.epam.wca.gym.interceptor.LoggingInterceptor;
import com.epam.wca.gym.interceptor.UserDetailsInterceptor;
import com.epam.wca.gym.repository.TraineeRepository;
import com.epam.wca.gym.repository.TrainingTypeRepository;
import com.epam.wca.gym.repository.UserRepository;
import com.epam.wca.gym.service.TrainerService;
import com.epam.wca.gym.util.DTOFactory;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TrainerControllerImpl.class)
@EnableAspectJAutoProxy(proxyTargetClass = true)
class TrainerControllerImplTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TrainerService trainerService;

    @MockitoBean
    private UserRepository userRepository;

    @MockitoBean
    private TraineeRepository traineeRepository;

    @MockitoBean
    private TrainingTypeRepository trainingTypeRepository;

    @MockitoBean
    private LoggingInterceptor loggingInterceptor;

    @MockitoBean
    private UserDetailsInterceptor userDetailsInterceptor;

    private final Trainer trainer = new Trainer();

    @BeforeEach
    void setUp() throws IOException {
        when(loggingInterceptor.preHandle(any(), any(), any())).thenReturn(true);
        when(userDetailsInterceptor.preHandle(any(), any(), any())).thenReturn(true);
        when(userRepository.findUserByUsername(any())).thenReturn(Optional.of(trainer));
    }

    @Test
    void testGetProfile() throws Exception {
        var trainerSendDto = mock(TrainerSendDTO.class);

        try(var mockDTOFactory = mockStatic(DTOFactory.class)) {
            mockDTOFactory.when(() -> DTOFactory.createTrainerSendDTO(trainer))
                    .thenReturn(trainerSendDto);

            mockMvc.perform(get("/users/trainers/profiles"))
                    .andExpect(status().isOk());
        }
    }

    @Test
    void testUpdateProfile_ValidRequest() throws Exception {
        var trainerSendDto = mock(TrainerSendDTO.class);

        when(trainerService.update(any(Trainer.class), any(TrainerUpdateDTO.class)))
                .thenReturn(trainerSendDto);
        when(trainingTypeRepository.findTrainingTypeByType(anyString()))
                .thenReturn(Optional.of(new TrainingType()));

        mockMvc.perform(put("/users/trainers/profiles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                             "firstName": "John",
                             "lastName": "Doe",
                             "trainingType": "YOGA",
                             "isActive": true
                        }
                    """))
                .andExpect(status().isOk());
    }

    static Stream<Arguments> updateProfileBadData() {
        return Stream.of(
                Arguments.of("Firstname missing",
                        """
                        {
                             "lastName": "Doe",
                             "trainingType": "YOGA",
                             "isActive": true
                        }
                        """
                ),
                Arguments.of("Firstname too short",
                        """
                        {
                             "firstName": "J",
                             "lastName": "Doe",
                             "trainingType": "YOGA",
                             "isActive": true
                        }
                        """
                ),
                Arguments.of("Firstname too long",
                        """
                        {
                             "firstName": "ANameThatIsWayTooLongForTheSpecifiedLimit",
                             "lastName": "Doe",
                             "trainingType": "YOGA",
                             "isActive": true
                        }
                        """
                ),
                Arguments.of("Lastname missing",
                        """
                        {
                             "firstName": "John",
                             "trainingType": "YOGA",
                             "isActive": true
                        }
                        """
                ),
                Arguments.of("Lastname too short",
                        """
                        {
                             "firstName": "John",
                             "lastName": "D",
                             "trainingType": "YOGA",
                             "isActive": true
                        }
                        """
                ),
                Arguments.of("Lastname too long",
                        """
                        {
                             "firstName": "John",
                             "lastName": "ALastNameThatIsWayTooLongForTheSpecifiedLimit",
                             "trainingType": "YOGA",
                             "isActive": true
                        }
                        """
                ),
                Arguments.of("Training type missing",
                        """
                        {
                             "firstName": "John",
                             "lastName": "Doe",
                             "isActive": true
                        }
                        """
                ),
                Arguments.of("Training type too short",
                        """
                        {
                             "firstName": "John",
                             "lastName": "Doe",
                             "trainingType": "Y",
                             "isActive": true
                        }
                        """
                ),
                Arguments.of("Invalid training type",
                        """
                        {
                             "firstName": "John",
                             "lastName": "Doe",
                             "trainingType": "invalid_type",
                             "isActive": true
                        }
                        """
                ),
                Arguments.of("Training type too long",
                        """
                        {
                             "firstName": "John",
                             "lastName": "Doe",
                             "trainingType": "VeryLongTrainingTypeThatExceedsLimit",
                             "isActive": true
                        }
                        """
                )
        );
    }

    @ParameterizedTest(name = "{index} - {0}")
    @MethodSource("updateProfileBadData")
    void testUpdateProfile_BadData_ShouldReturnBadRequest(String testName, String body) throws Exception {
        when(trainingTypeRepository.findTrainingTypeByType("invalid_type"))
                .thenReturn(Optional.empty());
        when(trainingTypeRepository.findTrainingTypeByType("YOGA"))
                .thenReturn(Optional.of(new TrainingType()));

        mockMvc.perform(put("/users/trainers/profiles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGetTrainings_ValidRequest() throws Exception {
        List<TrainingBasicDTO> trainingBasicDTOS = new ArrayList<>();

        when(trainerService.findTrainingsFiltered(any(Long.class), any(TrainerTrainingQuery.class)))
                .thenReturn(trainingBasicDTOS);
        when(traineeRepository.findTraineeByUsername(any()))
                .thenReturn(Optional.of(new Trainee()));

        mockMvc.perform(post("/users/trainers/trainings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                             "traineeName": "jane.smith",
                             "dateFrom": "01.01.2024 10:00:00 UTC",
                             "dateTo": "01.01.2024 10:00:00 UTC"
                        }
                    """))
                .andExpect(status().isOk());
    }

    static Stream<Arguments> getTrainingsBadData() {
        return Stream.of(
                Arguments.of("Trainee name too short",
                        """
                        {
                             "traineeName": "J",
                             "dateFrom": "01.01.2024 10:00:00 UTC",
                             "dateTo": "01.01.2024 10:00:00 UTC"
                        }
                        """
                ),
                Arguments.of("Trainee name too long",
                        """
                        {
                             "traineeName": "ANameThatIsWayTooLongForTheLimitOfFiftyCharacters",
                             "dateFrom": "01.01.2024 10:00:00 UTC",
                             "dateTo": "01.01.2024 10:00:00 UTC"
                        }
                        """
                ),
                Arguments.of("Date from invalid format",
                        """
                        {
                             "traineeName": "jane.smith",
                             "dateFrom": "2024-01-01T10:00:00Z",
                             "dateTo": "01.01.2024 10:00:00 UTC"
                        }
                        """
                ),
                Arguments.of("Date to invalid format",
                        """
                        {
                             "traineeName": "jane.smith",
                             "dateFrom": "01.01.2024 10:00:00 UTC",
                             "dateTo": "2024-01-01T10:00:00Z"
                        }
                        """
                )
        );
    }

    @ParameterizedTest(name = "{index} - {0}")
    @MethodSource("getTrainingsBadData")
    void testGetTrainings_BadData_ShouldReturnBadRequest(String testName, String body) throws Exception {
        when(traineeRepository.findTraineeByUsername(any()))
                .thenReturn(Optional.of(new Trainee()));

        mockMvc.perform(post("/users/trainers/trainings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCreateTraining() throws Exception {
        when(traineeRepository.findTraineeByUsername(any()))
                .thenReturn(Optional.of(new Trainee()));

        mockMvc.perform(post("/users/trainers/trainings/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                             "traineeUsername": "jane.smith",
                             "trainingName": "YogaSession",
                             "date": "31.12.2024 10:00:00 UTC",
                             "trainingDuration": 60
                        }
                    """))
                .andExpect(status().isAccepted());
    }

    static Stream<Arguments> createTrainingBadData() {
        return Stream.of(
                Arguments.of("Trainee username too short",
                        """
                        {
                             "trainerUsername": "j",
                             "trainingName": "YogaSession",
                             "date": "31.12.2024 10:00:00 UTC",
                             "trainingDuration": 60
                        }
                        """
                ),
                Arguments.of("Trainee username too long",
                        """
                        {
                             "trainerUsername": "ANameThatIsWayTooLongForTheLimitOfTwentyFiveCharacters",
                             "trainingName": "YogaSession",
                             "date": "31.12.2024 10:00:00 UTC",
                             "trainingDuration": 60
                        }
                        """
                ),
                Arguments.of("Training name too short",
                        """
                        {
                             "traineeUsername": "jane.smith",
                             "trainingName": "Y",
                             "date": "31.12.2024 10:00:00 UTC",
                             "trainingDuration": 60
                        }
                        """
                ),
                Arguments.of("Training name too long",
                        """
                        {
                             "traineeUsername": "jane.smith",
                             "trainingName": "VeryLongTrainingNameThatExceedsLimitOfTwentyFiveCharacters",
                             "date": "31.12.2024 10:00:00 UTC",
                             "trainingDuration": 60
                        }
                        """
                ),
                Arguments.of("Date null",
                        """
                        {
                             "traineeUsername": "jane.smith",
                             "trainingName": "YogaSession",
                             "trainingDuration": 60
                        }
                        """
                ),
                Arguments.of("Training date in past",
                        """
                        {
                             "traineeUsername": "jane.smith",
                             "trainingName": "YogaSession",
                             "date": "31.12.2023 10:00:00 UTC",
                             "trainingDuration": 60
                        }
                        """
                ),
                Arguments.of("Training duration too short",
                        """
                        {
                             "traineeUsername": "jane.smith",
                             "trainingName": "YogaSession",
                             "date": "31.12.2025 10:00:00 UTC",
                             "trainingDuration": 5
                        }
                        """
                ),
                Arguments.of("Training duration too long",
                        """
                        {
                             "traineeUsername": "jane.smith",
                             "trainingName": "YogaSession",
                             "date": "31.12.2025 10:00:00 UTC",
                             "trainingDuration": 400
                        }
                        """
                ),
                Arguments.of("Training duration null",
                        """
                        {
                             "traineeUsername": "jane.smith",
                             "trainingName": "YogaSession",
                             "date": "31.12.2025 10:00:00 UTC"
                        }
                        """
                ),
                Arguments.of("Training name null",
                        """
                        {
                             "trainerUsername": "ethan.white",
                             "date": "31.12.2025 10:00:00 UTC",
                             "trainingDuration": 400
                        }
                        """
                ),
                Arguments.of("Trainee username null",
                        """
                        {
                             "trainingName": "YogaSession",
                             "date": "31.12.2025 10:00:00 UTC",
                             "trainingDuration": 400
                        }
                        """
                )
        );
    }

    @ParameterizedTest(name = "{index} - {0}")
    @MethodSource("createTrainingBadData")
    void testCreateTraining_BadData_ShouldReturnBadRequest(String testName, String body) throws Exception {
        when(traineeRepository.findTraineeByUsername(any()))
                .thenReturn(Optional.of(new Trainee()));

        mockMvc.perform(post("/users/trainers/trainings/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest());
    }
}
