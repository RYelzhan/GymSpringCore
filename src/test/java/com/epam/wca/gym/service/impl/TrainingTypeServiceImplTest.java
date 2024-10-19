package com.epam.wca.gym.service.impl;

import com.epam.wca.gym.dto.training_type.TrainingTypeBasicDTO;
import com.epam.wca.gym.entity.TrainingType;
import com.epam.wca.gym.exception.ControllerValidationException;
import com.epam.wca.gym.repository.TrainingTypeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TrainingTypeServiceImplTest {
    @Mock
    private TrainingTypeRepository trainingTypeRepository;

    @InjectMocks
    private TrainingTypeServiceImpl trainingTypeService;

    @Test
    void testFindByType_ValidType() {
        // Given
        String type = "Yoga";
        TrainingType expectedTrainingType = new TrainingType(); // Create and populate as needed
        when(trainingTypeRepository.findTrainingTypeByType(type)).thenReturn(Optional.of(expectedTrainingType));

        // When
        TrainingType actualTrainingType = trainingTypeService.findByType(type);

        // Then
        assertEquals(expectedTrainingType, actualTrainingType);
        verify(trainingTypeRepository).findTrainingTypeByType(type);
    }

    @Test
    void testFindByType_InvalidType() {
        // Given
        String type = "InvalidType";
        when(trainingTypeRepository.findTrainingTypeByType(type)).thenReturn(null);

        // When & Then
        ControllerValidationException exception = assertThrows(
                ControllerValidationException.class,
                () -> trainingTypeService.findByType(type)
        );
        assertEquals("Invalid Training Type choice", exception.getMessage());
    }

    @Test
    void testFindAll() {
        // Given
        TrainingType trainingType1 = new TrainingType(); // Create and populate as needed
        TrainingType trainingType2 = new TrainingType(); // Create and populate as needed
        List<TrainingType> trainingTypeList = List.of(trainingType1, trainingType2);
        when(trainingTypeRepository.findAll()).thenReturn(trainingTypeList);

        // When
        List<TrainingTypeBasicDTO> actualDTOs = trainingTypeService.findAll();

        // Then
        assertNotNull(actualDTOs);
        assertEquals(2, actualDTOs.size());
        verify(trainingTypeRepository).findAll();
    }
}
