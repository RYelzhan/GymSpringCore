package com.epam.wca.gym.service;

import com.epam.wca.gym.dto.TraineeDTO;
import com.epam.wca.gym.entity.Trainee;
import com.epam.wca.gym.repository.TraineeDAO;
import com.epam.wca.gym.service.impl.TraineeServiceImpl;
import com.epam.wca.gym.utils.AppConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
class TraineeServiceImplTest {
    @Mock
    private TraineeDAO traineeDAO;
    @Mock
    private ProfileService profileService;
    @InjectMocks
    private TraineeServiceImpl traineeService;

    private TraineeDTO traineeDTO;
    private Trainee trainee;

    @BeforeEach
    void setUp() throws ParseException {
        Trainee.setProfileService(profileService);
        when(profileService.createUserName(anyString(), anyString())).thenReturn("John.Doe");

        traineeDTO = new TraineeDTO("John", "Doe", new SimpleDateFormat((AppConstants.DEFAULT_DATE_FORMAT)).parse("1990.01.01"), "123 Street");
        trainee = new Trainee("John", "Doe", new SimpleDateFormat((AppConstants.DEFAULT_DATE_FORMAT)).parse("1990.01.01"), "123 Street");
    }

    @Test
    void testCreateTrainee() {
        when(traineeDAO.save(any(Trainee.class))).thenReturn(trainee);
        Trainee createdTrainee = traineeService.create(traineeDTO);
        assertNotNull(createdTrainee);
        assertEquals("John", createdTrainee.getFirstName());
        verify(traineeDAO, times(1)).save(any(Trainee.class));
    }

    @Test
    void testUpdateTrainee() {
        doNothing().when(traineeDAO).updateByUsername(anyString(), any(Trainee.class));
        traineeService.updateByUsername("John.Doe", trainee);
        verify(traineeDAO, times(1)).updateByUsername(anyString(), any(Trainee.class));
    }

    @Test
    void testDeleteTrainee() {
        doNothing().when(traineeDAO).deleteByUsername(anyString());
        traineeService.deleteByUsername("John.Doe");
        verify(traineeDAO, times(1)).deleteByUsername(anyString());
    }

    @Test
    void testFindByUsername() {
        when(traineeDAO.findByUsername("John.Doe")).thenReturn(trainee);
        Trainee foundTrainee = traineeService.findByUsername("John.Doe");
        assertNotNull(foundTrainee);
        assertEquals("John", foundTrainee.getFirstName());
    }

    @Test
    void testFindById() {
        when(traineeDAO.findById(1L)).thenReturn(trainee);
        Trainee foundTrainee = traineeService.findById(1L);
        assertNotNull(foundTrainee);
        assertEquals("John", foundTrainee.getFirstName());
    }
}