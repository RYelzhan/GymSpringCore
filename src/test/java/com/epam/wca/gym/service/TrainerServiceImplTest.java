package com.epam.wca.gym.service;

import com.epam.wca.gym.dto.TrainerDTO;
import com.epam.wca.gym.entity.Trainer;
import com.epam.wca.gym.entity.TrainingType;
import com.epam.wca.gym.repository.TrainerDAO;
import com.epam.wca.gym.service.impl.TrainerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TrainerServiceImplTest {
    @Mock
    private TrainerDAO trainerDAO;
    @Mock
    private ProfileService profileService;
    @InjectMocks
    private TrainerServiceImpl trainerService;
    private TrainerDTO trainerDTO;
    private Trainer trainer;
    @BeforeEach
    public void setUp() {
        Trainer.setProfileService(profileService);
        when(profileService.createUserName(anyString(), anyString())).thenReturn("John.Doe");

        trainer = new Trainer("Jane", "Smith", TrainingType.YOGA);
        trainer.setUserId(1L);
        trainerDTO = new TrainerDTO("Jane", "Smith", TrainingType.YOGA);
    }
    @Test
    void testCreateTrainer() {
        when(trainerDAO.save(any(Trainer.class))).thenReturn(trainer);
        Trainer createdTrainer = trainerService.create(trainerDTO);
        assertEquals("Jane", createdTrainer.getFirstName());
        verify(trainerDAO, times(1)).save(any(Trainer.class));
    }

    @Test
    void testUpdateTrainer() {
        doNothing().when(trainerDAO).updateByUsername(anyString(), any(Trainer.class));
        trainerService.updateByUsername("jane.doe", trainer);
        verify(trainerDAO, times(1)).updateByUsername(anyString(), any(Trainer.class));
    }

    @Test
    void testFindByUsername() {
        when(trainerDAO.findByUsername(anyString())).thenReturn(trainer);
        Trainer foundTrainer = trainerService.findByUsername("jane.doe");
        assertEquals("Jane", foundTrainer.getFirstName());
    }

    @Test
    void testFindById() {
        when(trainerDAO.findById(anyLong())).thenReturn(trainer);
        Trainer foundTrainer = trainerService.findById(1L);
        assertEquals(1L, foundTrainer.getUserId());
    }
}
