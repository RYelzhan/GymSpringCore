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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

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

    @MockBean
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

    @Test
    void testUpdateProfile_EmptyFirstname_ShouldReturnBadRequest() throws Exception {
        mockMvc.perform(put("/users/trainees/profiles")
                        .with(user(trainee))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                             "firstName": "",
                             "lastName": "Doe",
                             "dateOfBirth": "01.01.1990 00:00:00 UTC",
                             "address": "123 Main St",
                             "isActive": true
                        }
                    """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testUpdateProfile_EmptyLastname_ShouldReturnBadRequest() throws Exception {
        mockMvc.perform(put("/users/trainees/profiles")
                        .with(user(trainee))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                             "firstName": "John",
                             "lastName": "",
                             "dateOfBirth": "01.01.1990 00:00:00 UTC",
                             "address": "123 Main St",
                             "isActive": true
                        }
                    """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testUpdateProfile_MissingFirstname_ShouldReturnBadRequest() throws Exception {
        mockMvc.perform(put("/users/trainees/profiles")
                        .with(user(trainee))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                             "lastName": "Doe",
                             "dateOfBirth": "01.01.1990 00:00:00 UTC",
                             "address": "123 Main St",
                             "isActive": true
                        }
                    """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testUpdateProfile_MissingLastname_ShouldReturnBadRequest() throws Exception {
        mockMvc.perform(put("/users/trainees/profiles")
                        .with(user(trainee))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                             "firstName": "John",
                             "dateOfBirth": "01.01.1990 00:00:00 UTC",
                             "address": "123 Main St",
                             "isActive": true
                        }
                    """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testUpdateProfile_MissingRequiredIsActive_ShouldReturnBadRequest() throws Exception {
        mockMvc.perform(put("/users/trainees/profiles")
                        .with(user(trainee))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                             "firstName": "John",
                             "lastName": "Doe",
                             "dateOfBirth": "01.01.1990 00:00:00 UTC",
                             "address": "123 Main St"
                        }
                    """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testUpdateProfile_ExceedingFirstnameLength_ShouldReturnBadRequest() throws Exception {
        mockMvc.perform(put("/users/trainees/profiles")
                        .with(user(trainee))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                             "firstName": "Averylongfirstnameexceedinglimits",
                             "lastName": "Doe",
                             "dateOfBirth": "01.01.1990 00:00:00 UTC",
                             "address": "123 Main St",
                             "isActive": true
                        }
                    """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testUpdateProfile_ExceedingLastnameLength_ShouldReturnBadRequest() throws Exception {
        mockMvc.perform(put("/users/trainees/profiles")
                        .with(user(trainee))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                             "firstName": "John",
                             "lastName": "Averylonglastnameexceedinglimits",
                             "dateOfBirth": "01.01.1990 00:00:00 UTC",
                             "address": "123 Main St",
                             "isActive": true
                        }
                    """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testUpdateProfile_ExceedingAddressLength_ShouldReturnBadRequest() throws Exception {
        mockMvc.perform(put("/users/trainees/profiles")
                        .with(user(trainee))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                             "firstName": "John",
                             "lastName": "Doe",
                             "dateOfBirth": "01.01.1990 00:00:00 UTC",
                             "address": "This address is far too long and exceeds fifty characters",
                             "isActive": true
                        }
                    """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testUpdateProfile_FutureDate_ShouldReturnBadRequest() throws Exception {
        mockMvc.perform(put("/users/trainees/profiles")
                        .with(user(trainee))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                             "firstName": "John",
                             "lastName": "Doe",
                             "dateOfBirth": "01.01.3000 00:00:00 UTC",
                             "address": "123 Main St",
                             "isActive": true
                        }
                    """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testUpdateProfile_InvalidDateFormat_ShouldReturnBadRequest() throws Exception {
        mockMvc.perform(put("/users/trainees/profiles")
                        .with(user(trainee))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                             "firstName": "John",
                             "lastName": "Doe",
                             "dateOfBirth": "1990-01-01T00:00:00Z",
                             "address": "123 Main St",
                             "isActive": true
                        }
                    """))
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

    @Test
    void testUpdateTrainerList_EmptyTrainersList_ShouldReturnBadRequest() throws Exception {
        List<TrainerBasicDTO> newlyAssignedTrainers = new ArrayList<>();

        when(traineeService.addTrainers(any(Trainee.class), any(TraineeTrainersUpdateDTO.class)))
                .thenReturn(newlyAssignedTrainers);

        mockMvc.perform(put("/users/trainees/trainers")
                        .with(user(trainee))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                             "trainerUsernames": []
                        }
                    """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testUpdateTrainerList_EmptyTrainerUsername_ShouldReturnBadRequest() throws Exception {
        List<TrainerBasicDTO> newlyAssignedTrainers = new ArrayList<>();

        when(traineeService.addTrainers(any(Trainee.class), any(TraineeTrainersUpdateDTO.class)))
                .thenReturn(newlyAssignedTrainers);

        mockMvc.perform(put("/users/trainees/trainers")
                        .with(user(trainee))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                             "trainerUsernames": [""]
                        }
                    """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testUpdateTrainerList_ExceedingTrainerUsernameLength_ShouldReturnBadRequest() throws Exception {
        List<TrainerBasicDTO> newlyAssignedTrainers = new ArrayList<>();

        when(traineeService.addTrainers(any(Trainee.class), any(TraineeTrainersUpdateDTO.class)))
                .thenReturn(newlyAssignedTrainers);

        mockMvc.perform(put("/users/trainees/trainers")
                        .with(user(trainee))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                             "trainerUsernames": ["averylongtrainerusernamethatexceedsthefiftycharacterlimit"]
                        }
                    """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testUpdateTrainerList_InvalidTrainerUsername_ShouldReturnBadRequest() throws Exception {
        List<TrainerBasicDTO> newlyAssignedTrainers = new ArrayList<>();

        when(traineeService.addTrainers(any(Trainee.class), any(TraineeTrainersUpdateDTO.class)))
                .thenReturn(newlyAssignedTrainers);

        mockMvc.perform(put("/users/trainees/trainers")
                        .with(user(trainee))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                             "trainerUsernames": ["invalid_trainer"]
                        }
                    """))
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

    @Test
    void testGetTrainings_TrainerNameTooShort_ShouldReturnBadRequest() throws Exception {
        mockMvc.perform(post("/users/trainees/trainings")
                        .with(user(trainee))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                             "trainerName": "J",
                             "dateFrom": "01.01.2020 10:00:00 UTC",
                             "dateTo": "31.12.2030 10:00:00 UTC",
                             "trainingType": "YOGA"
                        }
                    """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGetTrainings_TrainerNameTooLong_ShouldReturnBadRequest() throws Exception {
        mockMvc.perform(post("/users/trainees/trainings")
                        .with(user(trainee))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                             "trainerName": "ANameThatIsWayTooLongForTheSpecifiedLimitOfFiftyCharacters",
                             "dateFrom": "01.01.2020 10:00:00 UTC",
                             "dateTo": "31.12.2030 10:00:00 UTC",
                             "trainingType": "YOGA"
                        }
                    """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGetTrainings_InvalidDateFormat_ShouldReturnBadRequest() throws Exception {
        mockMvc.perform(post("/users/trainees/trainings")
                        .with(user(trainee))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                              "trainerName": "ethan.white",
                              "dateFrom": "01-01-2023",
                              "dateTo": "2023-12-31T10:00:00Z",
                              "trainingType": "YOGA"
                        }
                    """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGetTrainings_TrainingTypeTooLong_ShouldReturnBadRequest() throws Exception {
        mockMvc.perform(post("/users/trainees/trainings")
                        .with(user(trainee))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                             "trainerName": "ethan.white",
                             "dateFrom": "01.01.2020 10:00:00 UTC",
                             "dateTo": "31.12.2030 10:00:00 UTC",
                             "trainingType": "VeryLongTrainingTypeThatExceedsLimit"
                        }
                    """))
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

    @Test
    void testCreateTraining_TrainerUsernameTooShort_ShouldReturnBadRequest() throws Exception {
        mockMvc.perform(post("/users/trainees/trainings/new")
                        .with(user(trainee))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                             "trainerUsername": "J",
                             "trainingName": "YogaSession",
                             "date": "31.12.2024 10:00:00 UTC",
                             "trainingDuration": 60
                        }
                    """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCreateTraining_TrainerUsernameTooLong_ShouldReturnBadRequest() throws Exception {
        mockMvc.perform(post("/users/trainees/trainings/new")
                        .with(user(trainee))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                             "trainerUsername": "ANameThatIsWayTooLongForTheLimitOfTwentyFiveCharacters",
                             "trainingName": "YogaSession",
                             "date": "31.12.2024 10:00:00 UTC",
                             "trainingDuration": 60
                        }
                    """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCreateTraining_TrainingNameTooShort_ShouldReturnBadRequest() throws Exception {
        mockMvc.perform(post("/users/trainees/trainings/new")
                        .with(user(trainee))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                             "trainerUsername": "ethan.white",
                             "trainingName": "Y",
                             "date": "31.12.2024 10:00:00 UTC",
                             "trainingDuration": 60
                        }
                    """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCreateTraining_TrainingNameTooLong_ShouldReturnBadRequest() throws Exception {
        mockMvc.perform(post("/users/trainees/trainings/new")
                        .with(user(trainee))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                             "trainerUsername": "ethan.white",
                             "trainingName": "VeryLongTrainingNameThatExceedsLimitOfTwentyFiveCharacters",
                             "date": "31.12.2024 10:00:00 UTC",
                             "trainingDuration": 60
                        }
                    """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCreateTraining_DateNull_ShouldReturnBadRequest() throws Exception {
        mockMvc.perform(post("/users/trainees/trainings/new")
                        .with(user(trainee))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                             "trainerUsername": "ethan.white",
                             "trainingName": "YogaSession",
                             "trainingDuration": 60
                        }
                    """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCreateTraining_TrainingDateInPast_ShouldReturnBadRequest() throws Exception {
        mockMvc.perform(post("/users/trainees/trainings/new")
                        .with(user(trainee))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                             "trainerUsername": "ethan.white",
                             "trainingName": "YogaSession",
                             "date": "31.12.2023 10:00:00 UTC",
                             "trainingDuration": 60
                        }
                    """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCreateTraining_TrainingDurationTooShort_ShouldReturnBadRequest() throws Exception {
        mockMvc.perform(post("/users/trainees/trainings/new")
                        .with(user(trainee))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                             "trainerUsername": "ethan.white",
                             "trainingName": "YogaSession",
                             "date": "31.12.2025 10:00:00 UTC",
                             "trainingDuration": 5
                        }
                    """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCreateTraining_TrainingDurationTooLong_ShouldReturnBadRequest() throws Exception {
        mockMvc.perform(post("/users/trainees/trainings/new")
                        .with(user(trainee))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                             "trainerUsername": "ethan.white",
                             "trainingName": "YogaSession",
                             "date": "31.12.2025 10:00:00 UTC",
                             "trainingDuration": 400
                        }
                    """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCreateTraining_TrainingDurationNull_ShouldReturnBadRequest() throws Exception {
        mockMvc.perform(post("/users/trainees/trainings/new")
                        .with(user(trainee))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                             "trainerUsername": "ethan.white",
                             "trainingName": "YogaSession",
                             "date": "31.12.2025 10:00:00 UTC"
                        }
                    """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCreateTraining_TrainingNameNull_ShouldReturnBadRequest() throws Exception {
        mockMvc.perform(post("/users/trainees/trainings/new")
                        .with(user(trainee))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                             "trainerUsername": "ethan.white",
                             "date": "31.12.2025 10:00:00 UTC",
                             "trainingDuration": 400
                        }
                    """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCreateTraining_TrainerUsernameNull_ShouldReturnBadRequest() throws Exception {
        mockMvc.perform(post("/users/trainees/trainings/new")
                        .with(user(trainee))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                             "trainingName": "YogaSession",
                             "date": "31.12.2025 10:00:00 UTC",
                             "trainingDuration": 400
                        }
                    """))
                .andExpect(status().isBadRequest());
    }
}
