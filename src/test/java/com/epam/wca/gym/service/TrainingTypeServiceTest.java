package com.epam.wca.gym.service;

import com.epam.wca.gym.entity.TrainingType;
import com.epam.wca.gym.service.impl.TrainingTypeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class TrainingTypeServiceTest {
    @InjectMocks
    private TrainingTypeService trainingTypeService;

    private TrainingType trainingType;

    @BeforeEach
    void SetUp() {
        trainingType = new TrainingType();
    }

    @Test
    void testUnsupportedSave() {
        // Verify that save() throws UnsupportedOperationException
        assertThrows(UnsupportedOperationException.class, () -> trainingTypeService.save(trainingType));
    }

    @Test
    void testUnsupportedUpdate() {
        // Verify that update() throws UnsupportedOperationException
        assertThrows(UnsupportedOperationException.class, () -> trainingTypeService.update(trainingType));
    }

    @Test
    void testUnsupportedDeleteById() {
        // Verify that deleteById() throws UnsupportedOperationException
        assertThrows(UnsupportedOperationException.class, () -> trainingTypeService.deleteById(1L));
    }

    @Test
    void testUnsupportedFindById() {
        // Verify that findById() throws UnsupportedOperationException
        assertThrows(UnsupportedOperationException.class, () -> trainingTypeService.findById(1L));
    }
}
