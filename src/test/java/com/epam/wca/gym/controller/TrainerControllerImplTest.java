package com.epam.wca.gym.controller;

import com.epam.wca.gym.dto.trainer.TrainerSendDTO;
import com.epam.wca.gym.dto.trainer.TrainerUpdateDTO;
import com.epam.wca.gym.dto.training.TrainerTrainingQuery;
import com.epam.wca.gym.dto.training.TrainingBasicDTO;
import com.epam.wca.gym.entity.Trainer;
import com.epam.wca.gym.service.TrainerService;
import com.epam.wca.gym.util.DTOFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
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
class TrainerControllerImplTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TrainerService trainerService;

    private final Trainer trainer = new Trainer();

    @Test
    void testGetProfile() throws Exception {
        var trainerSendDto = mock(TrainerSendDTO.class);

        try(var mockDTOFactory = mockStatic(DTOFactory.class)) {
            mockDTOFactory.when(() -> DTOFactory.createTrainerSendDTO(trainer))
                    .thenReturn(trainerSendDto);

            mockMvc.perform(get("/users/trainers/profiles")
                            .with(user(trainer)))
                    .andExpect(status().isOk());
        }
    }

    @Test
    void testUpdateProfile_ValidRequest() throws Exception {
        var trainerSendDto = mock(TrainerSendDTO.class);

        when(trainerService.update(any(Trainer.class), any(TrainerUpdateDTO.class)))
                .thenReturn(trainerSendDto);

        mockMvc.perform(put("/users/trainers/profiles")
                        .with(user(trainer))
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

    @Test
    void testUpdateProfile_FirstnameMissing_ShouldReturnBadRequest() throws Exception {
        mockMvc.perform(put("/users/trainers/profiles")
                        .with(user(trainer))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                             "lastName": "Doe",
                             "trainingType": "YOGA",
                             "isActive": true
                        }
                    """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testUpdateProfile_FirstnameTooShort_ShouldReturnBadRequest() throws Exception {
        mockMvc.perform(put("/users/trainers/profiles")
                        .with(user(trainer))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                             "firstName": "J",
                             "lastName": "Doe",
                             "trainingType": "YOGA",
                             "isActive": true
                        }
                    """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testUpdateProfile_FirstnameTooLong_ShouldReturnBadRequest() throws Exception {
        mockMvc.perform(put("/users/trainers/profiles")
                        .with(user(trainer))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                             "firstName": "ANameThatIsWayTooLongForTheSpecifiedLimit",
                             "lastName": "Doe",
                             "trainingType": "YOGA",
                             "isActive": true
                        }
                    """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testUpdateProfile_LastnameMissing_ShouldReturnBadRequest() throws Exception {
        mockMvc.perform(put("/users/trainers/profiles")
                        .with(user(trainer))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                             "firstName": "John",
                             "trainingType": "YOGA",
                             "isActive": true
                        }
                    """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testUpdateProfile_LastnameTooShort_ShouldReturnBadRequest() throws Exception {
        mockMvc.perform(put("/users/trainers/profiles")
                        .with(user(trainer))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                             "firstName": "John",
                             "lastName": "D",
                             "trainingType": "YOGA",
                             "isActive": true
                        }
                    """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testUpdateProfile_LastnameTooLong_ShouldReturnBadRequest() throws Exception {
        mockMvc.perform(put("/users/trainers/profiles")
                        .with(user(trainer))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                             "firstName": "John",
                             "lastName": "ALastNameThatIsWayTooLongForTheSpecifiedLimit",
                             "trainingType": "YOGA",
                             "isActive": true
                        }
                    """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testUpdateProfile_TrainingTypeMissing_ShouldReturnBadRequest() throws Exception {
        mockMvc.perform(put("/users/trainers/profiles")
                        .with(user(trainer))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                             "firstName": "John",
                             "lastName": "Doe",
                             "isActive": true
                        }
                    """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testUpdateProfile_TrainingTypeTooShort_ShouldReturnBadRequest() throws Exception {
        mockMvc.perform(put("/users/trainers/profiles")
                        .with(user(trainer))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                             "firstName": "John",
                             "lastName": "Doe",
                             "trainingType": "Y",
                             "isActive": true
                        }
                    """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testUpdateProfile_InvalidTrainingType_ShouldReturnBadRequest() throws Exception {
        mockMvc.perform(put("/users/trainers/profiles")
                        .with(user(trainer))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                             "firstName": "John",
                             "lastName": "Doe",
                             "trainingType": "invalid_type",
                             "isActive": true
                        }
                    """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testUpdateProfile_TrainingTypeTooLong_ShouldReturnBadRequest() throws Exception {
        mockMvc.perform(put("/users/trainers/profiles")
                        .with(user(trainer))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                             "firstName": "John",
                             "lastName": "Doe",
                             "trainingType": "VeryLongTrainingTypeThatExceedsLimit",
                             "isActive": true
                        }
                    """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testUpdateProfile_IsActiveMissing_ShouldReturnBadRequest() throws Exception {
        mockMvc.perform(put("/users/trainers/profiles")
                        .with(user(trainer))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                             "firstName": "John",
                             "lastName": "Doe",
                             "trainingType": "YOGA"
                        }
                    """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testDeleteProfile() throws Exception {
        mockMvc.perform(delete("/users/trainers/profiles")
                        .with(user(trainer)))
                .andExpect(status().isNoContent());
    }

    @Test
    void testGetTrainings_ValidRequest() throws Exception {
        List<TrainingBasicDTO> trainingBasicDTOS = new ArrayList<>();

        when(trainerService.findTrainingsFiltered(any(Long.class), any(TrainerTrainingQuery.class)))
                .thenReturn(trainingBasicDTOS);

        mockMvc.perform(post("/users/trainers/trainings")
                        .with(user(trainer))
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

    @Test
    void testGetTrainings_TraineeNameTooShort_ShouldReturnBadRequest() throws Exception {
        mockMvc.perform(post("/users/trainers/trainings")
                        .with(user(trainer))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                             "traineeName": "J",
                             "dateFrom": "01.01.2024 10:00:00 UTC",
                             "dateTo": "01.01.2024 10:00:00 UTC"
                        }
                    """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGetTrainings_TraineeNameTooLong_ShouldReturnBadRequest() throws Exception {
        mockMvc.perform(post("/users/trainers/trainings")
                        .with(user(trainer))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                             "traineeName": "ANameThatIsWayTooLongForTheLimitOfFiftyCharacters",
                             "dateFrom": "01.01.2024 10:00:00 UTC",
                             "dateTo": "01.01.2024 10:00:00 UTC"
                        }
                    """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGetTrainings_DateFromInvalidFormat_ShouldReturnBadRequest() throws Exception {
        mockMvc.perform(post("/users/trainers/trainings")
                        .with(user(trainer))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                             "traineeName": "jane.smith",
                             "dateFrom": "2024-01-01T10:00:00Z",
                             "dateTo": "01.01.2024 10:00:00 UTC"
                        }
                    """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGetTrainings_DateToInvalidFormat_ShouldReturnBadRequest() throws Exception {
        mockMvc.perform(post("/users/trainers/trainings")
                        .with(user(trainer))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                             "traineeName": "jane.smith",
                             "dateFrom": "01.01.2024 10:00:00 UTC",
                             "dateTo": "2024-01-01T10:00:00Z"
                        }
                    """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCreateTraining() throws Exception {
        mockMvc.perform(post("/users/trainers/trainings/new")
                        .with(user(trainer))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                             "traineeUsername": "jane.smith",
                             "trainingName": "YogaSession",
                             "date": "31.12.2024 10:00:00 UTC",
                             "trainingDuration": 60
                        }
                    """))
                .andExpect(status().isCreated());
    }

    @Test
    void testCreateTraining_TraineeUsernameTooShort_ShouldReturnBadRequest() throws Exception {
        mockMvc.perform(post("/users/trainers/trainings/new")
                        .with(user(trainer))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                             "trainerUsername": "j",
                             "trainingName": "YogaSession",
                             "date": "31.12.2024 10:00:00 UTC",
                             "trainingDuration": 60
                        }
                    """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCreateTraining_TraineeUsernameTooLong_ShouldReturnBadRequest() throws Exception {
        mockMvc.perform(post("/users/trainers/trainings/new")
                        .with(user(trainer))
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
        mockMvc.perform(post("/users/trainers/trainings/new")
                        .with(user(trainer))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                             "traineeUsername": "jane.smith",
                             "trainingName": "Y",
                             "date": "31.12.2024 10:00:00 UTC",
                             "trainingDuration": 60
                        }
                    """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCreateTraining_TrainingNameTooLong_ShouldReturnBadRequest() throws Exception {
        mockMvc.perform(post("/users/trainers/trainings/new")
                        .with(user(trainer))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                             "traineeUsername": "jane.smith",
                             "trainingName": "VeryLongTrainingNameThatExceedsLimitOfTwentyFiveCharacters",
                             "date": "31.12.2024 10:00:00 UTC",
                             "trainingDuration": 60
                        }
                    """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCreateTraining_DateNull_ShouldReturnBadRequest() throws Exception {
        mockMvc.perform(post("/users/trainers/trainings/new")
                        .with(user(trainer))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                             "traineeUsername": "jane.smith",
                             "trainingName": "YogaSession",
                             "trainingDuration": 60
                        }
                    """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCreateTraining_TrainingDateInPast_ShouldReturnBadRequest() throws Exception {
        mockMvc.perform(post("/users/trainers/trainings/new")
                        .with(user(trainer))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                             "traineeUsername": "jane.smith",
                             "trainingName": "YogaSession",
                             "date": "31.12.2023 10:00:00 UTC",
                             "trainingDuration": 60
                        }
                    """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCreateTraining_TrainingDurationTooShort_ShouldReturnBadRequest() throws Exception {
        mockMvc.perform(post("/users/trainers/trainings/new")
                        .with(user(trainer))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                             "traineeUsername": "jane.smith",
                             "trainingName": "YogaSession",
                             "date": "31.12.2025 10:00:00 UTC",
                             "trainingDuration": 5
                        }
                    """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCreateTraining_TrainingDurationTooLong_ShouldReturnBadRequest() throws Exception {
        mockMvc.perform(post("/users/trainers/trainings/new")
                        .with(user(trainer))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                             "traineeUsername": "jane.smith",
                             "trainingName": "YogaSession",
                             "date": "31.12.2025 10:00:00 UTC",
                             "trainingDuration": 400
                        }
                    """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCreateTraining_TrainingDurationNull_ShouldReturnBadRequest() throws Exception {
        mockMvc.perform(post("/users/trainers/trainings/new")
                        .with(user(trainer))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                             "traineeUsername": "jane.smith",
                             "trainingName": "YogaSession",
                             "date": "31.12.2025 10:00:00 UTC"
                        }
                    """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCreateTraining_TrainingNameNull_ShouldReturnBadRequest() throws Exception {
        mockMvc.perform(post("/users/trainers/trainings/new")
                        .with(user(trainer))
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
    void testCreateTraining_TraineeUsernameNull_ShouldReturnBadRequest() throws Exception {
        mockMvc.perform(post("/users/trainers/trainings/new")
                        .with(user(trainer))
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
