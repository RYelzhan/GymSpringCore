package com.epam.wca.gym.service.impl;

import com.epam.wca.common.gymcommon.exception.InternalErrorException;
import com.epam.wca.gym.communication.StatisticsCommunicationService;
import com.epam.wca.gym.dto.trainer.TrainerBasicDTO;
import com.epam.wca.gym.dto.trainer.TrainerRegistrationDTO;
import com.epam.wca.gym.dto.trainer.TrainerSendDTO;
import com.epam.wca.gym.dto.trainer.TrainerTrainingCreateDTO;
import com.epam.wca.gym.dto.trainer.TrainerUpdateDTO;
import com.epam.wca.gym.dto.training.TrainerTrainingQuery;
import com.epam.wca.gym.dto.training.TrainingBasicDTO;
import com.epam.wca.common.gymcommon.auth_dto.UserAuthenticatedDTO;
import com.epam.wca.gym.entity.Trainee;
import com.epam.wca.gym.entity.Trainer;
import com.epam.wca.gym.entity.Training;
import com.epam.wca.gym.entity.TrainingType;
import com.epam.wca.gym.exception.ProfileNotFoundException;
import com.epam.wca.gym.repository.TraineeRepository;
import com.epam.wca.gym.repository.TrainerRepository;
import com.epam.wca.gym.util.DTOFactory;
import com.epam.wca.gym.util.Filter;
import com.epam.wca.gym.util.UserFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TrainerServiceImplTest {
    @Mock
    private TrainerRepository trainerRepository;
    @Mock
    private TraineeRepository traineeRepository;
    @Mock
    private TrainingTypeServiceImpl trainingTypeService;
    @Mock
    private TrainingServiceImpl trainingService;
    @Mock
    private ProfileServiceImpl profileService;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private StatisticsCommunicationService statisticsCommunicationService;

    @InjectMocks
    private TrainerServiceImpl trainerService;

    @BeforeEach
    public void setUp() {
        // Given // Create a TraineeDTO object
        Trainee.setProfileService(profileService);
        Trainer.setProfileService(profileService);
    }

    @Test
    void testSave() {
        // Given
        TrainingType trainingType = new TrainingType("CROSSFIT");
        Trainer trainer = new Trainer("John", "Doe", trainingType);
        TrainerRegistrationDTO trainerDTO = new TrainerRegistrationDTO(
                "John",
                "Doe",
                "CROSSFIT"
        );

        String encodedPassword = "encoded_password";

        // Mock trainingTypeService to return a specific TrainingType
        when(trainingTypeService.findByType(trainerDTO.trainingType())).thenReturn(trainingType);
        when(passwordEncoder.encode(any())).thenReturn(encodedPassword);

        // Mock static UserFactory.createTrainer to return a Trainer object
        try (MockedStatic<UserFactory> mockedUserFactory = mockStatic(UserFactory.class)) {
            mockedUserFactory.when(() -> UserFactory.createTrainer(trainerDTO, trainingType))
                    .thenReturn(trainer);

            // Act: Call the save method
            UserAuthenticatedDTO result = trainerService.save(trainerDTO);

            // Assert: Validate the result
            assertEquals(trainer.getUsername(), result.username());

            // Verify interactions
            verify(trainingTypeService, times(1)).findByType(trainerDTO.trainingType());
            verify(trainerRepository, times(1)).save(trainer);

            // Verify the static method call
            mockedUserFactory.verify(
                    () -> UserFactory.createTrainer(trainerDTO, trainingType),
                    times(1)
            );
        }
    }

    @Test
    void testTrainingsFiltered() {
        // Mock input and return data
        Long traineeId = 1L;
        TrainerTrainingQuery trainerTrainingQuery = mock(TrainerTrainingQuery.class); // Mock DTO input

        // Create a mock Trainee object with a set of trainings
        Trainer trainer = mock(Trainer.class);
        Set<Training> trainings = new HashSet<>();
        Training training1 = mock(Training.class);  // Mock training
        trainings.add(training1);

        // When findById is called, return the mocked trainee
        when(trainerRepository.findById(traineeId)).thenReturn(Optional.of(trainer));
        when(trainer.getTrainings()).thenReturn(trainings);

        try (var mockFilter = mockStatic(Filter.class);
             var mockDTOFactory = mockStatic(DTOFactory.class)) {
            // Mock filtering method
            List<Training> filteredTrainings = List.of(training1); // Mock filtered trainings

            // When the static method is called, return the filteredTrainings
            mockFilter.when(() -> Filter.filterTrainerTrainings(trainings, trainerTrainingQuery))
                    .thenReturn(filteredTrainings);

            // Mock the DTOFactory to return a list of TrainingBasicDTO
            TrainingBasicDTO trainingBasicDTO = mock(TrainingBasicDTO.class);
            mockDTOFactory.when(() -> DTOFactory.createTrainerBasicTrainingDTO(training1))
                    .thenReturn(trainingBasicDTO);

            // Call the method under test
            List<TrainingBasicDTO> result = trainerService.findTrainingsFiltered(traineeId, trainerTrainingQuery);

            // Verify the result and behavior
            assertNotNull(result);  // Ensure the result is not null
            assertEquals(1, result.size());  // Ensure the result contains the correct number of items
            assertEquals(trainingBasicDTO, result.get(0));  // Verify the correct mapping was used

            // Verify the interactions with mocks
            verify(trainer, times(1)).getTrainings();
        }
    }

    @Test
    void testUpdateTrainee() {
        // Create test data
        Trainer trainer = new Trainer();
        trainer.setFirstname("John");
        trainer.setLastname("Doe");
        trainer.setSpecialization(new TrainingType("YOGA"));

        TrainerUpdateDTO trainerUpdateDTO = new TrainerUpdateDTO(
                "Jane",
                "Smith",
                "CALISTHENICS",
                false
        );

        TrainingType updatedTrainingType = new TrainingType("CALISTHENICS");

        when(trainingTypeService.findByType("CALISTHENICS")).thenReturn(updatedTrainingType);

        // Mock repository behavior
        when(trainerRepository.save(trainer)).thenReturn(trainer);

        // Mock DTOFactory
        TrainerSendDTO trainerSendDTO = mock(TrainerSendDTO.class);

        try (var mockDTOFactory = mockStatic(DTOFactory.class)) {
            mockDTOFactory.when(() -> DTOFactory.createTrainerSendDTO(trainer))
                    .thenReturn(trainerSendDTO);

            // Call the method under test
            TrainerSendDTO result = trainerService.update(trainer, trainerUpdateDTO);

            // Verify that the trainee fields were updated correctly
            assertEquals("Jane", trainer.getFirstname());
            assertEquals("Smith", trainer.getLastname());
            assertEquals(updatedTrainingType, trainer.getSpecialization());
            assertFalse(trainer.isActive());

            // Verify that the save method was called
            verify(trainerRepository).save(trainer);

            // Verify that the correct DTO is returned
            assertEquals(trainerSendDTO, result);
        }
    }

    @Test
    void testFindActiveUnassignedTrainers() {
        // Initialize a set of assigned trainers
        Set<Trainer> assignedTrainers = new HashSet<>();

        // Initialize a list of unassigned trainers that the repository will return
        List<Trainer> unassignedTrainers = List.of(
                new Trainer(
                        "Unassigned1",
                        "Trainer",
                        null
                )
        );

        // Given: Mocking the repository to return a list of unassigned trainers
        when(trainerRepository.findActiveUnassignedTrainers(assignedTrainers)).thenReturn(unassignedTrainers);

        // Mock static DTOFactory.createBasicTrainerDTO for converting Trainer to TrainerBasicDTO
        try (var mockedDTOFactory = mockStatic(DTOFactory.class)) {
            // Define the mock behavior of the static method
            mockedDTOFactory.when(() -> DTOFactory.createBasicTrainerDTO(unassignedTrainers.get(0)))
                    .thenReturn(
                            new TrainerBasicDTO(
                            "",
                            "Unassigned1",
                            "Trainer",
                            "unassigned1"
                            ));

            // Act: Call the method under test
            List<TrainerBasicDTO> result = trainerService.findActiveUnassignedTrainers(assignedTrainers);

            // Assert: Verify the returned list of TrainerBasicDTO
            assertEquals(1, result.size());
            assertEquals("Unassigned1", result.get(0).firstName());

            // Verify interactions
            verify(trainerRepository, times(1))
                    .findActiveUnassignedTrainers(assignedTrainers);
            mockedDTOFactory.verify(
                    () -> DTOFactory.createBasicTrainerDTO(unassignedTrainers.get(0)),
                    times(1)
            );
        }
    }

    @Test
    void testFindById_TrainerExists() {
        // Given
        Trainer trainer = new Trainer(
                "John",
                "Doe",
                null
        );

        when(trainerRepository.findById(1L)).thenReturn(Optional.of(trainer));

        // When
        Trainer result = trainerService.findById(1L);

        // Then
        assertEquals(trainer, result);
    }

    @Test
    void testFindById_TrainerDoesNotExist() {
        // Given
        when(trainerRepository.findById(1L)).thenReturn(Optional.empty());

        // Then
        assertThrows(ProfileNotFoundException.class, () -> {
            // When
            trainerService.findById(1L);
        });
    }

    @Test
    void testDeleteById() {
        // Given
        Long trainerId = 1L;

        Trainer trainer = new Trainer();
        trainer.setId(trainerId);
        trainer.setFirstname("John");
        trainer.setLastname("Doe");
        trainer.setActive(true);
        trainer.setTrainings(new HashSet<>());

        // When
        when(trainerRepository.getReferenceById(trainerId)).thenReturn(trainer);

        // Then
        trainerService.deleteById(trainerId);

        // Verify
        verify(trainerRepository).deleteById(trainerId); // Verify the repository's deleteById method was called with the correct ID
        verify(statisticsCommunicationService, times(1)).deleteTrainings(any());
    }

    @Test
    void testFindByUsername() {
        // Given
        String username = "johndoe";
        Trainer trainer = new Trainer(
                "John",
                "Doe",
                null
        );

        when(trainerRepository.findTrainerByUsername(username)).thenReturn(Optional.of(trainer));

        // When
        Trainer result = trainerService.findByUsername(username);

        // Then
        assertEquals(trainer, result);
        verify(trainerRepository).findTrainerByUsername(username); // Verify the repository method was called
    }

    @Test
    void testCreateTraining() {
        // Giventrue, Tra
        Trainer trainer = new Trainer("John",
                "Doe",
                null
        );
        trainer.setId(1L);

        TrainerTrainingCreateDTO trainingDTO = new TrainerTrainingCreateDTO(
                "traineeUser",
                "Workout",
                null,
                60
        );

        Trainee trainee = new Trainee("Jane", "Smith", null, null);
        trainee.setTrainersAssigned(new HashSet<>());
        when(traineeRepository.findTraineeByUsername(trainingDTO.traineeUsername())).thenReturn(Optional.of(trainee));

        // When
        trainerService.createTraining(trainer, trainingDTO);

        // Then
        assertTrue(trainee.getTrainersAssigned().contains(trainer)); // Check that the trainer was added to trainee's assigned trainers
        verify(traineeRepository).findTraineeByUsername(trainingDTO.traineeUsername()); // Verify trainee lookup
        verify(trainingService).save(any(Training.class)); // Verify the training was saved
    }

    @Test
    void testCreateTraining_TraineeNotFound() {
        // Given
        var trainer = new Trainer(
                "John",
                "Doe",
                null
        );
        var trainingDTO = new TrainerTrainingCreateDTO(
                "nonExistentTrainee",
                "Workout",
                null,
                60
        );

        // Mocking that no trainee is found
        when(traineeRepository.findTraineeByUsername(trainingDTO.traineeUsername())).thenReturn(Optional.empty());

        // When & Then
        var exception = assertThrows(
                InternalErrorException.class,
                () -> trainerService.createTraining(trainer, trainingDTO)
        );

        String expectedMessage = "No Trainee Found with Username: " + trainingDTO.traineeUsername();
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage)); // Verify the exception message
        verify(traineeRepository).findTraineeByUsername(trainingDTO.traineeUsername()); // Verify trainee lookup
        verify(trainingService, never()).save(any(Training.class)); // Ensure save is not called
    }
}
