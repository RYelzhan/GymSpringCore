package com.epam.wca.gym.service.impl;

import com.epam.wca.gym.communication.StatisticsCommunicationService;
import com.epam.wca.gym.entity.Trainer;
import com.epam.wca.gym.entity.Training;
import com.epam.wca.gym.repository.TrainingRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class TrainingServiceImplTest {
    @Mock
    private TrainingRepository trainingRepository;

    @Mock
    private StatisticsCommunicationService statisticsCommunicationService;

    @InjectMocks
    private TrainingServiceImpl trainingService;

    @Test
    void testSave() {
        // Given
        Training training = new Training(
                null,
                new Trainer(
                        "John.Doe",
                        "John",
                        "Doe",
                        null),
                null,
                null,
                null,
                15
        ); // Create a Training object as needed

        // When
        trainingService.save(training);

        // Then
        verify(trainingRepository).save(training); // Verify that save was called on the repository
        verify(statisticsCommunicationService).addNewTraining(Mockito.argThat(
                r -> r.firstname().equals("John") && r.lastname().equals("Doe")
        ));
    }
}
