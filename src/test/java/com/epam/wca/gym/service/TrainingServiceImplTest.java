package com.epam.wca.gym.service;

import com.epam.wca.gym.dto.TrainingDTO;
import com.epam.wca.gym.entity.Trainee;
import com.epam.wca.gym.entity.Trainer;
import com.epam.wca.gym.entity.Training;
import com.epam.wca.gym.entity.TrainingType;
import com.epam.wca.gym.repository.TrainingDAO;
import com.epam.wca.gym.service.impl.TrainingServiceImpl;
import com.epam.wca.gym.utils.AppConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
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
    void setUp() throws ParseException {
        MockitoAnnotations.openMocks(this);
        trainingDTO = new TrainingDTO(1L, 2L, "Yoga",
                TrainingType.POWERLIFTING, new SimpleDateFormat(AppConstants.DEFAULT_DATE_FORMAT).parse("2024.08.31"), 60);
        training = new Training(1L, 2L, "Yoga",
                TrainingType.POWERLIFTING, new SimpleDateFormat(AppConstants.DEFAULT_DATE_FORMAT).parse("2024.08.31"), 60);
    }

    @Test
    void testCreateTraining_Success() {
        when(traineeService.findById(1L)).thenReturn(new Trainee());
        when(trainerService.findById(2L)).thenReturn(new Trainer());
        doNothing().when(trainingDAO).save(any(Training.class));

        Training createdTraining = trainingService.createTraining(trainingDTO);

        assertNotNull(createdTraining);
        assertEquals("Yoga", createdTraining.getTrainingName());
        verify(trainingDAO, times(1)).save(any(Training.class));
    }

    @Test
    void testCreateTraining_TraineeNotFound() {
        when(traineeService.findById(1L)).thenReturn(null);

        assertThrows(IllegalStateException.class, () -> trainingService.createTraining(trainingDTO));

        verify(trainingDAO, never()).save(any(Training.class));
    }

    @Test
    void testCreateTraining_TrainerNotFound() {
        when(traineeService.findById(1L)).thenReturn(new Trainee());
        when(trainerService.findById(2L)).thenReturn(null);

        assertThrows(IllegalStateException.class, () -> trainingService.createTraining(trainingDTO));

        verify(trainingDAO, never()).save(any(Training.class));
    }

    @Test
    void testFindById() {
        when(trainingDAO.findById(1L)).thenReturn(training);

        Training foundTraining = trainingService.findById(1L);

        assertNotNull(foundTraining);
        assertEquals("Yoga", foundTraining.getTrainingName());
    }
}
