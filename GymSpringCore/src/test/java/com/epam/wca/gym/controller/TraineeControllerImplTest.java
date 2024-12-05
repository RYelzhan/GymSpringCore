package com.epam.wca.gym.controller;

import com.epam.wca.gym.dto.trainee.TraineeSendDTO;
import com.epam.wca.gym.dto.trainee.TraineeTrainersUpdateDTO;
import com.epam.wca.gym.dto.trainee.TraineeUpdateDTO;
import com.epam.wca.gym.dto.trainer.TrainerBasicDTO;
import com.epam.wca.gym.dto.training.TraineeTrainingQuery;
import com.epam.wca.gym.dto.training.TrainingBasicDTO;
import com.epam.wca.gym.entity.Trainee;
import com.epam.wca.gym.service.TraineeService;
import com.epam.wca.gym.util.DTOFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("classpath:application-test.properties")
class TraineeControllerImplTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TraineeService traineeService;

    private final Trainee trainee = new Trainee();
    private final TraineeSendDTO traineeSendDto = mock(TraineeSendDTO.class);

    @Test
    void testGetProfile_ValidRequest() throws Exception {
        var traineeSendDTO = mock(TraineeSendDTO.class);

        try (var mockDTOFactory = mockStatic(DTOFactory.class)) {
            mockDTOFactory.when(() -> DTOFactory.createTraineeSendDTO(trainee))
                    .thenReturn(traineeSendDTO);

            mockMvc.perform(get("/users/trainees/profiles")
                            .with(user(trainee)))
                    .andExpect(status().isOk());
        }
    }

    @Test
    void testUpdateProfile_ValidRequest() throws Exception {
        when(traineeService.update(any(Trainee.class), any(TraineeUpdateDTO.class)))
                .thenReturn(traineeSendDto);

        mockMvc.perform(put("/users/trainees/profiles")
                        .with(user(trainee))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                             "firstName": "John",
                             "lastName": "Doe",
                             "dateOfBirth": "01.01.1990 00:00:00 UTC",
                             "address": "123 Main St",
                             "isActive": true
                        }
                    """))
                .andExpect(status().isOk());
    }

    static Stream<Arguments> updateProfileBadData() {
        return Stream.of(
                Arguments.of("Empty first name",
                        """
                        {
                             "firstName": "",
                             "lastName": "Doe",
                             "dateOfBirth": "01.01.1990 00:00:00 UTC",
                             "address": "123 Main St",
                             "isActive": true
                        }
                        """
                ),
                Arguments.of("Empty lastname",
                        """
                        {
                             "firstName": "John",
                             "lastName": "",
                             "dateOfBirth": "01.01.1990 00:00:00 UTC",
                             "address": "123 Main St",
                             "isActive": true
                        }
                        """
                ),
                Arguments.of("Missing firstname",
                        """
                        {
                             "lastName": "Doe",
                             "dateOfBirth": "01.01.1990 00:00:00 UTC",
                             "address": "123 Main St",
                             "isActive": true
                        }
                        """
                ),
                Arguments.of("Missing lastname",
                        """
                        {
                             "firstName": "John",
                             "dateOfBirth": "01.01.1990 00:00:00 UTC",
                             "address": "123 Main St",
                             "isActive": true
                        }
                        """
                ),
                Arguments.of("Missing required isActive",
                        """
                        {
                             "firstName": "John",
                             "lastName": "Doe",
                             "dateOfBirth": "01.01.1990 00:00:00 UTC",
                             "address": "123 Main St"
                        }
                        """
                ),
                Arguments.of("Exceeding firstname length",
                        """
                        {
                             "firstName": "Averylongfirstnameexceedinglimits",
                             "lastName": "Doe",
                             "dateOfBirth": "01.01.1990 00:00:00 UTC",
                             "address": "123 Main St",
                             "isActive": true
                        }
                        """
                ),
                Arguments.of("Exceeding lastname length",
                        """
                        {
                             "firstName": "John",
                             "lastName": "Averylonglastnameexceedinglimits",
                             "dateOfBirth": "01.01.1990 00:00:00 UTC",
                             "address": "123 Main St",
                             "isActive": true
                        }
                        """
                ),
                Arguments.of("Exceeding address length",
                        """
                        {
                             "firstName": "John",
                             "lastName": "Doe",
                             "dateOfBirth": "01.01.1990 00:00:00 UTC",
                             "address": "This address is far too long and exceeds fifty characters",
                             "isActive": true
                        }
                        """
                ),
                Arguments.of("Future date",
                        """
                        {
                             "firstName": "John",
                             "lastName": "Doe",
                             "dateOfBirth": "01.01.3000 00:00:00 UTC",
                             "address": "123 Main St",
                             "isActive": true
                        }
                        """
                ),
                Arguments.of("Invalid date format",
                        """
                        {
                             "firstName": "John",
                             "lastName": "Doe",
                             "dateOfBirth": "1990-01-01T00:00:00Z",
                             "address": "123 Main St",
                             "isActive": true
                        }
                        """
                )
        );
    }

    @ParameterizedTest(name = "{index} - {0}")
    @MethodSource("updateProfileBadData")
    void testUpdateProfile_BadData_ShouldReturnBadRequest(String testName, String body) throws Exception {
        mockMvc.perform(put("/users/trainees/profiles")
                        .with(user(trainee))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testDeleteProfile_Success() throws Exception {
        mockMvc.perform(delete("/users/trainees/profiles")
                        .with(user(trainee)))
                .andExpect(status().isNoContent());
    }

    @Test
    void testGetNotAssignedTrainers() throws Exception {
        List<TrainerBasicDTO> noAssignedTrainers = new ArrayList<>();

        when(traineeService.getListOfNotAssignedTrainers(any(Trainee.class)))
                .thenReturn(noAssignedTrainers);

        mockMvc.perform(get("/users/trainees/trainers")
                        .with(user(trainee)))
                .andExpect(status().isOk());
    }

    @Test
    void testUpdateTrainerList_ValidRequest() throws Exception {
        List<TrainerBasicDTO> newlyAssignedTrainers = new ArrayList<>();

        when(traineeService.addTrainers(any(Trainee.class), any(TraineeTrainersUpdateDTO.class)))
                .thenReturn(newlyAssignedTrainers);

        mockMvc.perform(put("/users/trainees/trainers")
                        .with(user(trainee))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                             "trainerUsernames": ["ethan.white", "olivia.harris"]
                        }
                    """))
                .andExpect(status().isOk());
    }

    static Stream<Arguments> updateTrainersListBadData() {
        return Stream.of(
                Arguments.of("Empty trainers list",
                        """
                        {
                             "trainerUsernames": []
                        }
                        """
                ),
                Arguments.of("Empty trainer username",
                        """
                        {
                             "trainerUsernames": [""]
                        }
                        """
                ),
                Arguments.of("Exceeding trainer username length",
                        """
                        {
                             "trainerUsernames": ["averylongtrainerusernamethatexceedsthefiftycharacterlimit"]
                        }
                        """
                ),
                Arguments.of("Invalid trainer username",
                        """
                        {
                             "trainerUsernames": ["invalid_trainer"]
                        }
                        """
                )
        );
    }

    @ParameterizedTest(name = "{index} - {0}")
    @MethodSource("updateTrainersListBadData")
    void testUpdateTrainerList_BadData_ShouldReturnBadRequest(String testName, String body) throws Exception {
        List<TrainerBasicDTO> newlyAssignedTrainers = new ArrayList<>();

        when(traineeService.addTrainers(any(Trainee.class), any(TraineeTrainersUpdateDTO.class)))
                .thenReturn(newlyAssignedTrainers);

        mockMvc.perform(put("/users/trainees/trainers")
                        .with(user(trainee))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGetTrainings_ValidRequest() throws Exception {
        List<TrainingBasicDTO> newlyAssignedTrainers = new ArrayList<>();

        when(traineeService.findTrainingsFiltered(any(Long.class), any(TraineeTrainingQuery.class)))
                .thenReturn(newlyAssignedTrainers);

        mockMvc.perform(post("/users/trainees/trainings")
                        .with(user(trainee))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                             "trainerName": "ethan.white",
                             "dateFrom": "01.01.2020 10:00:00 UTC",
                             "dateTo": "31.12.2030 10:00:00 UTC",
                             "trainingType": "YOGA"
                        }
                    """))
                .andExpect(status().isOk());
    }

    static Stream<Arguments> getTrainingsBadData() {
        return Stream.of(
                Arguments.of("Trainer name too short",
                        """
                        {
                             "trainerName": "J",
                             "dateFrom": "01.01.2020 10:00:00 UTC",
                             "dateTo": "31.12.2030 10:00:00 UTC",
                             "trainingType": "YOGA"
                        }
                        """
                ),
                Arguments.of("Trainer name too long",
                        """
                        {
                             "trainerName": "ANameThatIsWayTooLongForTheSpecifiedLimitOfFiftyCharacters",
                             "dateFrom": "01.01.2020 10:00:00 UTC",
                             "dateTo": "31.12.2030 10:00:00 UTC",
                             "trainingType": "YOGA"
                        }
                        """
                ),
                Arguments.of("Invalid date format",
                        """
                        {
                              "trainerName": "ethan.white",
                              "dateFrom": "01-01-2023",
                              "dateTo": "2023-12-31T10:00:00Z",
                              "trainingType": "YOGA"
                        }
                        """
                ),
                Arguments.of("Training type too long",
                        """
                        {
                             "trainerName": "ethan.white",
                             "dateFrom": "01.01.2020 10:00:00 UTC",
                             "dateTo": "31.12.2030 10:00:00 UTC",
                             "trainingType": "VeryLongTrainingTypeThatExceedsLimit"
                        }
                        """
                )
        );
    }

    @ParameterizedTest(name = "{index} - {0}")
    @MethodSource("getTrainingsBadData")
    void testGetTrainings_BadData_ShouldReturnBadRequest(String testName, String body) throws Exception {
        mockMvc.perform(post("/users/trainees/trainings")
                        .with(user(trainee))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCreateTraining_ValidRequest() throws Exception {
        mockMvc.perform(post("/users/trainees/trainings/new")
                        .with(user(trainee))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                             "trainerUsername": "ethan.white",
                             "trainingName": "YogaSession",
                             "date": "31.12.2024 10:00:00 UTC",
                             "trainingDuration": 60
                        }
                    """))
                .andExpect(status().isCreated());
    }

    static Stream<Arguments> createTrainingsBadData() {
        return Stream.of(
                Arguments.of("Trainer username too short",
                        """
                        {
                             "trainerUsername": "J",
                             "trainingName": "YogaSession",
                             "date": "31.12.2024 10:00:00 UTC",
                             "trainingDuration": 60
                        }
                        """
                ),
                Arguments.of("Trainer username too long",
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
                             "trainerUsername": "ethan.white",
                             "trainingName": "Y",
                             "date": "31.12.2024 10:00:00 UTC",
                             "trainingDuration": 60
                        }
                        """
                ),
                Arguments.of("Training name too long",
                        """
                        {
                             "trainerUsername": "ethan.white",
                             "trainingName": "VeryLongTrainingNameThatExceedsLimitOfTwentyFiveCharacters",
                             "date": "31.12.2024 10:00:00 UTC",
                             "trainingDuration": 60
                        }
                        """
                ),
                Arguments.of("Date null",
                        """
                        {
                             "trainerUsername": "ethan.white",
                             "trainingName": "YogaSession",
                             "trainingDuration": 60
                        }
                        """
                ),
                Arguments.of("Training date in past",
                        """
                        {
                             "trainerUsername": "ethan.white",
                             "trainingName": "YogaSession",
                             "date": "31.12.2023 10:00:00 UTC",
                             "trainingDuration": 60
                        }
                        """
                ),
                Arguments.of("Training duration too short",
                        """
                        {
                             "trainerUsername": "ethan.white",
                             "trainingName": "YogaSession",
                             "date": "31.12.2025 10:00:00 UTC",
                             "trainingDuration": 5
                        }
                        """
                ),
                Arguments.of("Training duration too long",
                        """
                        {
                             "trainerUsername": "ethan.white",
                             "trainingName": "YogaSession",
                             "date": "31.12.2025 10:00:00 UTC",
                             "trainingDuration": 400
                        }
                        """
                ),
                Arguments.of("Training duration null",
                        """
                        {
                             "trainerUsername": "ethan.white",
                             "date": "31.12.2025 10:00:00 UTC",
                             "trainingDuration": 400
                        }
                        """
                ),
                Arguments.of("Trainer username null",
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
    @MethodSource("createTrainingsBadData")
    void testCreateTraining_BadData_ShouldReturnBadRequest(String testName, String body) throws Exception {
        mockMvc.perform(post("/users/trainees/trainings/new")
                        .with(user(trainee))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest());
    }
}
