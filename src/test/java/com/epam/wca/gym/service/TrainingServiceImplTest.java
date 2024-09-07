package com.epam.wca.gym.service;

import com.epam.wca.gym.dto.TrainingDTO;
import com.epam.wca.gym.entity.Trainee;
import com.epam.wca.gym.entity.Trainer;
import com.epam.wca.gym.entity.Training;
import com.epam.wca.gym.entity.TrainingType;
import com.epam.wca.gym.repository.TrainingDAO;
import com.epam.wca.gym.service.impl.TrainingServiceImpl;
import com.epam.wca.gym.util.AppConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
public class TrainingServiceImplTest {
    @Mock
    private TrainingDAO trainingDAO;

    @Mock
    private TraineeService traineeService;

    @Mock
    private TrainerService trainerService;

    @InjectMocks
    private TrainingServiceImpl trainingService;

    private TrainingDTO trainingDTO;
    private Training training;

    @BeforeEach
    void setUp() {
        trainingDTO = new TrainingDTO(1L,
                2L,
                "Yoga",
                TrainingType.POWERLIFTING,
                LocalDate.parse("2024.08.31", DateTimeFormatter.ofPattern(AppConstants.DEFAULT_DATE_FORMAT)),
                60);

        training = new Training(1L,
                2L,
                "Yoga",
                TrainingType.POWERLIFTING,
                LocalDate.parse("2024.08.31", DateTimeFormatter.ofPattern(AppConstants.DEFAULT_DATE_FORMAT)),
                60);
    }

    @Test
    void testCreateTraining_Success() {
        when(traineeService.findById(1L))
                .thenReturn(new Trainee());

        when(trainerService.findById(2L))
                .thenReturn(new Trainer());

        when(trainingDAO.save(any(Training.class)))
                .thenReturn(new Training());

        Training createdTraining = trainingService.createTraining(trainingDTO);

        assertNotNull(createdTraining);
        assertEquals("Yoga", createdTraining.getTrainingName());

        verify(trainingDAO, times(1))
                .save(any(Training.class));
    }

    @Test
    void testCreateTraining_TraineeNotFound() {
        when(traineeService.findById(1L))
                .thenReturn(null);

        assertThrows(IllegalStateException.class, () -> trainingService.createTraining(trainingDTO));

        verify(trainingDAO, never()).save(any(Training.class));
    }

    @Test
    void testCreateTraining_TrainerNotFound() {
        when(traineeService.findById(1L))
                .thenReturn(new Trainee());

        when(trainerService.findById(2L))
                .thenReturn(null);

        assertThrows(IllegalStateException.class, () -> trainingService.createTraining(trainingDTO));

        verify(trainingDAO, never()).save(any(Training.class));
    }

    @Test
    void testFindById() {
        when(trainingDAO.findById(1L))
                .thenReturn(training);

        Training foundTraining = trainingService.findById(1L);

        assertNotNull(foundTraining);
        assertEquals("Yoga", foundTraining.getTrainingName());
    }
}
