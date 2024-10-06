package com.epam.wca.gym.service.deprecated;

import com.epam.wca.gym.dto.training.TrainingDTO;
import com.epam.wca.gym.entity.Trainee;
import com.epam.wca.gym.entity.Trainer;
import com.epam.wca.gym.entity.Training;
import com.epam.wca.gym.entity.TrainingType;
import com.epam.wca.gym.repository.deprecated.impl.TraineeDAO;
import com.epam.wca.gym.repository.deprecated.impl.TrainerDAO;
import com.epam.wca.gym.repository.deprecated.impl.TrainingDAO;
import com.epam.wca.gym.service.deprecated.TrainingServiceOld;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TrainingServiceImplTestOldTest {
    @Mock
    private TrainingDAO trainingDAO;

    @Mock
    private TraineeDAO traineeDAO;

    @Mock
    private TrainerDAO trainerDAO;

    @InjectMocks
    private TrainingServiceOld trainingServiceOld;

    private Training training;

    @BeforeEach
    public void setUp() {
        training = new Training();
    }

    @Test
    void testOverriddenSave() {
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
        Training savedTraining = trainingServiceOld.save(dto);

        // Then
        Mockito.verify(traineeDAO, times(1)).findById(traineeId);
        Mockito.verify(trainerDAO, times(1)).findById(trainerId);
        assertEquals(mockTraining.getTrainee(), savedTraining.getTrainee());
        assertEquals(mockTraining.getTrainer(), savedTraining.getTrainer());
    }

    @Test
    public void testSave() {
        // Given
        long traineeId = 1L;
        long trainerId = 2L;

        Trainee mockTrainee = new Trainee();  // Mock trainee
        Trainer mockTrainer = new Trainer();  // Mock trainer
        Training mockTraining = new Training(mockTrainee,
                mockTrainer,
                "Strength Training",
                new TrainingType("POWERLIFTING"),
                ZonedDateTime.now(),
                60);

        // When
        doNothing().when(trainingDAO).save(any());

        Training savedTraining = trainingServiceOld.save(mockTraining);

        // Then
        assertEquals(savedTraining, mockTraining);
        verify(trainingDAO, times(1)).save(any());
    }

    @Test
    void testUnsupportedOperations() {
        // Test update operation
        UnsupportedOperationException exception = assertThrows(UnsupportedOperationException.class,
                () -> trainingServiceOld.update(training));
        assertEquals(UnsupportedOperationException.class, exception.getClass());

        // Test deleteById operation
        exception = assertThrows(UnsupportedOperationException.class,
                () -> trainingServiceOld.deleteById(1L));
        assertEquals(UnsupportedOperationException.class, exception.getClass());

        // Test findByUniqueName operation
        exception = assertThrows(UnsupportedOperationException.class,
                () -> trainingServiceOld.findByUniqueName("Strength Training"));
        assertEquals(UnsupportedOperationException.class, exception.getClass());

        // Test findAll operation
        exception = assertThrows(UnsupportedOperationException.class,
                () -> trainingServiceOld.findAll());

        assertEquals(UnsupportedOperationException.class,
                exception.getClass());
    }
}
