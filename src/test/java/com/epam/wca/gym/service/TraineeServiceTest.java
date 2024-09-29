package com.epam.wca.gym.service;

import com.epam.wca.gym.dto.trainee.TraineeRegistrationDTO;
import com.epam.wca.gym.dto.trainee.TraineeUpdateDTO;
import com.epam.wca.gym.dto.trainer.TrainerBasicDTO;
import com.epam.wca.gym.entity.Trainee;
import com.epam.wca.gym.entity.Trainer;
import com.epam.wca.gym.entity.Training;
import com.epam.wca.gym.entity.TrainingType;
import com.epam.wca.gym.repository.impl.TraineeDAO;
import com.epam.wca.gym.service.impl.TraineeService;
import com.epam.wca.gym.service.impl.TrainerService;
import com.epam.wca.gym.util.AppConstants;
import com.epam.wca.gym.util.UserFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TraineeServiceTest {
    @Mock
    private TraineeDAO traineeDAO;
    @Mock
    private TrainerService trainerService;
    @Mock
    private ProfileService profileService;
    @InjectMocks
    private TraineeService traineeService;

    private TraineeRegistrationDTO traineeRegistrationDTO;

    @BeforeEach
    void setUp() {
        Trainee.setProfileService(profileService);
        Trainer.setProfileService(profileService);

        traineeRegistrationDTO = new TraineeRegistrationDTO("John",
                "Doe",
                ZonedDateTime.parse("01.01.1990 00:00:00 " + ZoneId.systemDefault(),
                        DateTimeFormatter.ofPattern(AppConstants.DEFAULT_DATE_FORMAT)),
                "123 Street");
    }

    @Test
    void testSave() {
        // Given // Create a TraineeDTO object
        Trainee trainee = UserFactory.createTrainee(traineeRegistrationDTO);  // Mock trainee created from dto

        // Act
        Trainee savedTrainee = traineeService.save(traineeRegistrationDTO);

        // Then
        Mockito.verify(traineeDAO, times(1)).save(trainee);
        assertEquals(trainee, savedTrainee);  // Ensure the saved trainee is the one returned
    }

    @Test
    void testFindAllTrainingsById() {
        // Given
        long traineeId = 1L;
        Set<Training> mockTrainings = Set.of(new Training());  // Create mock data

        // When
        when(traineeDAO.findAllTrainingsById(traineeId)).thenReturn(mockTrainings);

        // Act
        Set<Training> foundTrainings = traineeService.findAllTrainingsById(traineeId);

        // Then
        Mockito.verify(traineeDAO, times(1)).findAllTrainingsById(traineeId);
        assertEquals(mockTrainings, foundTrainings);
    }

    @Test
    void testFindTrainingByCriteria() {
        // Given
        String username = "john.doe";
        ZonedDateTime fromDate = ZonedDateTime.now().minusDays(5);
        ZonedDateTime toDate = ZonedDateTime.now();
        String trainerName = "Jane";
        TrainingType trainingType = new TrainingType("YOGA");
        List<Training> mockTrainings = List.of(new Training());  // Mock data

        // When
        when(traineeDAO.findTrainingByCriteria(username, fromDate, toDate, trainerName, trainingType))
                .thenReturn(mockTrainings);

        // Act
        List<Training> foundTrainings = traineeService.findTrainingByCriteria(username, fromDate, toDate, trainerName, trainingType);

        // Then
        Mockito.verify(traineeDAO, times(1)).findTrainingByCriteria(username, fromDate, toDate, trainerName, trainingType);
        assertEquals(mockTrainings, foundTrainings);
    }

    @Test
    void testDeleteByUsername() {
        // Given
        String username = "john.doe";
        Trainee trainee = new Trainee();
        trainee.setId(1L);  // Mock trainee with ID

        // When
        when(traineeDAO.findByUniqueName(username)).thenReturn(trainee);

        // Act
        traineeService.deleteByUsername(username);

        // Then
        Mockito.verify(traineeDAO, times(1)).findByUniqueName(username);
        Mockito.verify(traineeDAO, times(1)).deleteById(trainee.getId());
    }

    @Test
    public void testUpdate() {
        TraineeUpdateDTO traineeUpdateDTO = new TraineeUpdateDTO(
                "John.Doe",
                "John",
                "Doe",
                ZonedDateTime.now().minusYears(35),
                "123 Main St",
                true
        );

        Trainee trainee = new Trainee();

        Trainee updatedTrainee = traineeService.update(trainee, traineeUpdateDTO);

        assertEquals(trainee, updatedTrainee);
        verify(traineeDAO, times(1)).update(trainee);
    }

    @Test
    void testGetListOfNotAssignedTrainers() {
        Trainee trainee = mock(Trainee.class);

        TrainingType trainingType = new TrainingType("YOGA");

        // Mocking Trainer Entities
        Trainer trainer1 = new Trainer();
        trainer1.setId(1L);
        trainer1.setActive(true);

        Trainer trainer2 = new Trainer();
        trainer2.setId(2L);
        trainer2.setActive(true);
        trainer2.setSpecialization(trainingType);

        Trainer trainer3 = new Trainer();
        trainer3.setId(3L);
        trainer3.setActive(false);

        // Set of assigned trainers (trainer1 is assigned)
        Set<Trainer> assignedTrainers = Set.of(trainer1);

        // All trainers list
        List<Trainer> allTrainers = new ArrayList<>(List.of(trainer1, trainer2, trainer3));

        // Mock the return value of trainee.getTrainersAssigned()
        when(trainee.getTrainersAssigned()).thenReturn(assignedTrainers);

        // Mock the return value of trainerService.findAll()
        when(trainerService.findAll()).thenReturn(allTrainers);

        // Invoke the method under test
        List<TrainerBasicDTO> result = traineeService.getListOfNotAssignedTrainers(trainee);

        // Verify expected results
        assertEquals(1, result.size(), "Only one trainer should be returned");
        // Only trainer2 should be returned

        // Verify that trainerService.findAll() was called once
        verify(trainerService, times(1)).findAll();
    }
}