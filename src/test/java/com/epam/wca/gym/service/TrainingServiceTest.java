package com.epam.wca.gym.service;

import com.epam.wca.gym.dto.TrainingDTO;
import com.epam.wca.gym.entity.Trainee;
import com.epam.wca.gym.entity.Trainer;
import com.epam.wca.gym.entity.Training;
import com.epam.wca.gym.entity.TrainingType;
import com.epam.wca.gym.repository.impl.TraineeDAO;
import com.epam.wca.gym.repository.impl.TrainerDAO;
import com.epam.wca.gym.repository.impl.TrainingDAO;
import com.epam.wca.gym.service.impl.TrainingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TrainingServiceTest {
    @Mock
    private TrainingDAO trainingDAO;

    @Mock
    private TraineeDAO traineeDAO;

    @Mock
    private TrainerDAO trainerDAO;

    @InjectMocks
    private TrainingService trainingService;

    private Training training;

    @BeforeEach
    public void setUp() {
        training = new Training();
    }

    @Test
    void testSave() {
        // Given
        long traineeId = 1L;
        long trainerId = 2L;

        TrainingDTO dto = new TrainingDTO(traineeId,
                trainerId,
                "Strength Training",
                new TrainingType("POWERLIFTING"),
                ZonedDateTime.now(),
                60);
        Trainee mockTrainee = new Trainee();  // Mock trainee
        Trainer mockTrainer = new Trainer();  // Mock trainer
        Training mockTraining = new Training(mockTrainee,
                mockTrainer,
                dto.trainingName(),
                dto.trainingType(),
                dto.trainingDate(),
                dto.trainingDuration());

        // When
        when(traineeDAO.findById(traineeId)).thenReturn(mockTrainee);
        when(trainerDAO.findById(trainerId)).thenReturn(mockTrainer);

        // Act
        Training savedTraining = trainingService.save(dto);

        // Then
        Mockito.verify(traineeDAO, times(1)).findById(traineeId);
        Mockito.verify(trainerDAO, times(1)).findById(trainerId);
        assertEquals(mockTraining.getTrainee(), savedTraining.getTrainee());
        assertEquals(mockTraining.getTrainer(), savedTraining.getTrainer());
    }

    @Test
    void testUnsupportedOperations() {
        // Test update operation
        UnsupportedOperationException exception = assertThrows(UnsupportedOperationException.class,
                () -> trainingService.update(training));
        assertEquals(UnsupportedOperationException.class, exception.getClass());

        // Test deleteById operation
        exception = assertThrows(UnsupportedOperationException.class,
                () -> trainingService.deleteById(1L));
        assertEquals(UnsupportedOperationException.class, exception.getClass());

        // Test findByUniqueName operation
        exception = assertThrows(UnsupportedOperationException.class,
                () -> trainingService.findByUniqueName("Strength Training"));
        assertEquals(UnsupportedOperationException.class, exception.getClass());

        // Test findAll operation
        exception = assertThrows(UnsupportedOperationException.class,
                () -> trainingService.findAll());

        assertEquals(UnsupportedOperationException.class,
                exception.getClass());
    }
}
