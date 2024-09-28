package com.epam.wca.gym.service;

import com.epam.wca.gym.dto.trainer.TrainerSavingDTO;
import com.epam.wca.gym.entity.Trainer;
import com.epam.wca.gym.entity.Training;
import com.epam.wca.gym.entity.TrainingType;
import com.epam.wca.gym.repository.impl.TrainerDAO;
import com.epam.wca.gym.service.impl.TrainerService;
import com.epam.wca.gym.util.UserFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TrainerServiceTest {
    @Mock
    private TrainerDAO trainerDAO;

    @Mock
    private ProfileService profileService;

    private TrainerSavingDTO trainerDTO;

    @InjectMocks
    private TrainerService trainerService;

    @BeforeEach
    void setUp() {
        Trainer.setProfileService(profileService);

        trainerDTO = new TrainerSavingDTO("John",
                "Doe",
                new TrainingType("POWERLIFTING"));
    }


    @Test
    void testSave() {
        Trainer trainer = UserFactory.createTrainer(trainerDTO);  // Mock trainer created from dto

        // When
        doNothing().when(trainerDAO).save(trainer);

        // Act
        Trainer savedTrainer = trainerService.save(trainerDTO);

        // Then
        Mockito.verify(trainerDAO, times(1)).save(trainer);
        assertEquals(trainer, savedTrainer);  // Ensure the saved trainer is the one returned
    }

    @Test
    void testFindAllTrainingsById() {
        // Given
        long trainerId = 1L;
        Set<Training> mockTrainings = Set.of(new Training());  // Create mock data

        // When
        when(trainerDAO.findAllTrainingsById(trainerId)).thenReturn(mockTrainings);

        // Act
        Set<Training> foundTrainings = trainerService.findAllTrainingsById(trainerId);

        // Then
        verify(trainerDAO, times(1)).findAllTrainingsById(trainerId);
        assertEquals(mockTrainings, foundTrainings);
    }

    @Test
    void testFindTrainingByCriteria() {
        // Given
        String username = "john.doe";
        ZonedDateTime fromDate = ZonedDateTime.now().minusDays(5);
        ZonedDateTime toDate = ZonedDateTime.now();
        String trainerName = "Jane";
        TrainingType trainingType = new TrainingType("YOGA");
        List<Training> mockTrainings = List.of(new Training());  // Mock data

        // When
        when(trainerDAO.findTrainingByCriteria(username, fromDate, toDate, trainerName, trainingType))
                .thenReturn(mockTrainings);

        // Act
        List<Training> foundTrainings = trainerService.findTrainingByCriteria(username, fromDate, toDate, trainerName, trainingType);

        // Then
        verify(trainerDAO, times(1)).findTrainingByCriteria(username, fromDate, toDate, trainerName, trainingType);
        assertEquals(mockTrainings, foundTrainings);
    }
}
