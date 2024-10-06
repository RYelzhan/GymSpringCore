package com.epam.wca.gym.service.impl;

import com.epam.wca.gym.entity.Training;
import com.epam.wca.gym.repository.TrainingRepository;
import com.epam.wca.gym.service.impl.TrainingServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class TrainingServiceImplTest {
    @Mock
    private TrainingRepository trainingRepository;

    @InjectMocks
    private TrainingServiceImpl trainingService;

    @Test
    void testSave() {
        // Given
        Training training = new Training(); // Create a Training object as needed

        // When
        trainingService.save(training);

        // Then
        Mockito.verify(trainingRepository).save(training); // Verify that save was called on the repository
    }
}
