package com.epam.wca.gym.service.impl;

import com.epam.wca.common.gymcommon.auth_dto.UserAuthenticatedDTO;
import com.epam.wca.common.gymcommon.auth_dto.UserRegistrationDTO;
import com.epam.wca.common.gymcommon.exception.InternalErrorException;
import com.epam.wca.common.gymcommon.util.AppConstants;
import com.epam.wca.gym.communication.AuthenticationCommunicationService;
import com.epam.wca.gym.communication.StatisticsCommunicationService;
import com.epam.wca.gym.dto.trainee.TraineeRegistrationDTO;
import com.epam.wca.gym.dto.trainee.TraineeSendDTO;
import com.epam.wca.gym.dto.trainee.TraineeTrainersUpdateDTO;
import com.epam.wca.gym.dto.trainee.TraineeTrainingCreateDTO;
import com.epam.wca.gym.dto.trainee.TraineeUpdateDTO;
import com.epam.wca.gym.dto.trainer.TrainerBasicDTO;
import com.epam.wca.gym.dto.training.TraineeTrainingQuery;
import com.epam.wca.gym.dto.training.TrainingBasicDTO;
import com.epam.wca.gym.entity.Trainee;
import com.epam.wca.gym.entity.Trainer;
import com.epam.wca.gym.entity.Training;
import com.epam.wca.gym.entity.TrainingType;
import com.epam.wca.gym.exception.ProfileNotFoundException;
import com.epam.wca.gym.repository.TraineeRepository;
import com.epam.wca.gym.util.DTOFactory;
import com.epam.wca.gym.util.Filter;
import com.epam.wca.gym.util.UserFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TraineeServiceImplTest {
    @Mock
    private TraineeRepository traineeRepository;
    @Mock
    private TrainerServiceImpl trainerService;
    @Mock
    private TrainingServiceImpl trainingService;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private StatisticsCommunicationService statisticsCommunicationService;
    @Mock
    private AuthenticationCommunicationService authenticationCommunicationService;

    @InjectMocks
    private TraineeServiceImpl traineeService;

    @ParameterizedTest
    @CsvSource("John, Doe, John.Doe, password12")
    void testSave(String firstname, String lastname, String username, String password) {
        // arrange
        UserAuthenticatedDTO userAuthenticatedDTO = new UserAuthenticatedDTO(
                username,
                password
        );

        TraineeRegistrationDTO traineeRegistrationDTO = new TraineeRegistrationDTO(firstname,
                lastname,
                ZonedDateTime.parse("01.01.1990 00:00:00 " + ZoneId.systemDefault(),
                        DateTimeFormatter.ofPattern(AppConstants.DEFAULT_DATE_FORMAT)),
                "123 Street");

        when(authenticationCommunicationService.userRegister(any(UserRegistrationDTO.class)))
                .thenReturn(userAuthenticatedDTO);

        Trainee trainee = UserFactory.createTrainee(traineeRegistrationDTO, userAuthenticatedDTO.username());

        UserAuthenticatedDTO expectedAuthenticatedDTO = new UserAuthenticatedDTO(username, password);

        // Act
        UserAuthenticatedDTO authenticatedDTO = traineeService.save(traineeRegistrationDTO);

        // Then
        Mockito.verify(traineeRepository, times(1)).save(trainee);
        assertEquals(expectedAuthenticatedDTO, authenticatedDTO);  // Ensure the saved trainee is the one returned
    }

    @Test
    void testTrainingsFiltered() {
        // Mock input and return data
        Long traineeId = 1L;
        TraineeTrainingQuery traineeTrainingQuery = mock(TraineeTrainingQuery.class); // Mock DTO input

        // Create a mock Trainee object with a set of trainings
        Trainee trainee = mock(Trainee.class);
        Set<Training> trainings = new HashSet<>();
        Training training1 = mock(Training.class);  // Mock training
        trainings.add(training1);

        // When findById is called, return the mocked trainee
        when(traineeRepository.findById(traineeId)).thenReturn(Optional.of(trainee));
        when(trainee.getTrainings()).thenReturn(trainings);

        try (var mockFilter = mockStatic(Filter.class);
            var mockDTOFactory = mockStatic(DTOFactory.class)) {
            // Mock filtering method
            List<Training> filteredTrainings = List.of(training1); // Mock filtered trainings

            // When the static method is called, return the filteredTrainings
            mockFilter.when(() -> Filter.filterTraineeTrainings(trainings, traineeTrainingQuery))
                    .thenReturn(filteredTrainings);

            // Mock the DTOFactory to return a list of TrainingBasicDTO
            TrainingBasicDTO trainingBasicDTO = mock(TrainingBasicDTO.class);
            mockDTOFactory.when(() -> DTOFactory.createTraineeBasicTrainingDTO(training1))
                    .thenReturn(trainingBasicDTO);

            // Call the method under test
            List<TrainingBasicDTO> result = traineeService.findTrainingsFiltered(traineeId, traineeTrainingQuery);

            // Verify the result and behavior
            assertNotNull(result);  // Ensure the result is not null
            assertEquals(1, result.size());  // Ensure the result contains the correct number of items
            assertEquals(trainingBasicDTO, result.get(0));  // Verify the correct mapping was used

            // Verify the interactions with mocks
            verify(trainee, times(1)).getTrainings();
        }
    }

    @Test
    void testUpdateTrainee() {
        // Create test data
        Trainee trainee = new Trainee();
        trainee.setFirstname("John");
        trainee.setLastname("Doe");
        trainee.setDateOfBirth(
                ZonedDateTime.parse("01.01.1990 00:00:00 " + ZoneId.systemDefault(),
                        DateTimeFormatter.ofPattern(AppConstants.DEFAULT_DATE_FORMAT)));
        trainee.setAddress("123 Street");
        trainee.setActive(true);

        TraineeUpdateDTO traineeUpdateDTO = new TraineeUpdateDTO(
                "Jane",
                "Smith",
                ZonedDateTime.parse("05.05.1995 00:00:00 " + ZoneId.systemDefault(),
                DateTimeFormatter.ofPattern(AppConstants.DEFAULT_DATE_FORMAT)),
                "456 Avenue"
        );

        // Mock repository behavior
        when(traineeRepository.save(trainee)).thenReturn(trainee);

        // Mock DTOFactory
        TraineeSendDTO traineeSendDTO = mock(TraineeSendDTO.class);

        try (var mockDTOFactory = mockStatic(DTOFactory.class)) {
            mockDTOFactory.when(() -> DTOFactory.createTraineeSendDTO(trainee))
                    .thenReturn(traineeSendDTO);

            // Call the method under test
            TraineeSendDTO result = traineeService.update(trainee, traineeUpdateDTO);

            // Verify that the trainee fields were updated correctly
            assertEquals("Jane", trainee.getFirstname());
            assertEquals("Smith", trainee.getLastname());
            assertEquals(
                    ZonedDateTime.parse(
                            "05.05.1995 00:00:00 " + ZoneId.systemDefault(),
                            DateTimeFormatter.ofPattern(AppConstants.DEFAULT_DATE_FORMAT)
                    ),
                    trainee.getDateOfBirth()
            );
            assertEquals("456 Avenue", trainee.getAddress());

            // Verify that the save method was called
            verify(traineeRepository).save(trainee);

            // Verify that the correct DTO is returned
            assertEquals(traineeSendDTO, result);
        }
    }

    @Test
    void testGetListOfNotAssignedTrainers() {
        // Create test data
        Trainee trainee = new Trainee();
        Set<Trainer> assignedTrainers = new HashSet<>();

        Trainer trainer1 = new Trainer("John.Doe", "John", "Doe", new TrainingType("YOGA"));
        trainer1.setId(1L);

        Trainer trainer2 = new Trainer("Jane.Smith", "Jane", "Smith", new TrainingType("CROSSFIT"));
        trainer2.setId(2L);

        assignedTrainers.add(trainer1);
        assignedTrainers.add(trainer2);

        trainee.setTrainersAssigned(assignedTrainers);

        // Expected result: list of unassigned trainers (mocked)
        List<TrainerBasicDTO> expectedUnassignedTrainers = Arrays.asList(
                new TrainerBasicDTO("Michael.Jordan", "Michael", "Jordan", "ORDINARY"),
                new TrainerBasicDTO("Sara.Connor", "Sara", "Connor", "CALISTHENICS")
        );

        // Mock the behavior of trainerService
        when(trainerService.findActiveUnassignedTrainers(assignedTrainers)).thenReturn(expectedUnassignedTrainers);

        // Call the method under test
        List<TrainerBasicDTO> result = traineeService.getListOfNotAssignedTrainers(trainee);

        // Assert the result
        assertEquals(expectedUnassignedTrainers, result);

        // Verify that the correct interaction occurred
        verify(trainerService).findActiveUnassignedTrainers(assignedTrainers);
    }

    @Test
    void testDeleteById() {
        // Given
        Long traineeId = 1L;

        Trainee trainee = new Trainee();
        trainee.setId(traineeId);
        trainee.setFirstname("John");
        trainee.setLastname("Doe");
        trainee.setDateOfBirth(
                ZonedDateTime.parse("01.01.1990 00:00:00 " + ZoneId.systemDefault(),
                        DateTimeFormatter.ofPattern(AppConstants.DEFAULT_DATE_FORMAT)));
        trainee.setAddress("123 Street");
        trainee.setActive(true);
        trainee.setTrainings(new HashSet<>());

        // When
        when(traineeRepository.getReferenceById(traineeId)).thenReturn(trainee);

        // Then
        traineeService.deleteById(traineeId);

        // Verify
        verify(traineeRepository, times(1)).deleteById(traineeId);
        verify(statisticsCommunicationService, times(1)).deleteTrainings(any());
    }

    @Test
    void testAddTrainersSuccess() {
        // Given
        Trainee trainee = new Trainee();
        trainee.setTrainersAssigned(new HashSet<>());
        TraineeTrainersUpdateDTO dto = new TraineeTrainersUpdateDTO(Arrays.asList("trainer1", "trainer2"));

        Trainer trainer1 = new Trainer();
        trainer1.setId(1L);
        trainer1.setSpecialization(new TrainingType("YOGA"));

        Trainer trainer2 = new Trainer();
        trainer2.setId(2L);
        trainer2.setSpecialization(new TrainingType("ORDINARY"));

        when(trainerService.findByUsername("trainer1")).thenReturn(trainer1);
        when(trainerService.findByUsername("trainer2")).thenReturn(trainer2);

        // When
        List<TrainerBasicDTO> result = traineeService.addTrainers(trainee, dto);

        // Then
        // Verify that the trainers were added
        assertEquals(2, result.size());
        verify(trainerService, times(1)).findByUsername("trainer1");
        verify(trainerService, times(1)).findByUsername("trainer2");
        verify(traineeRepository, times(1)).save(trainee);
    }

    @Test
    void testAddTrainersThrowsExceptionWhenTrainerNotFound() {
        // Given
        Trainee trainee = new Trainee();
        TraineeTrainersUpdateDTO dto = new TraineeTrainersUpdateDTO(Arrays.asList("trainer1", "invalidTrainer"));

        Trainer trainer1 = new Trainer();

        when(trainerService.findByUsername("trainer1")).thenReturn(trainer1);
        when(trainerService.findByUsername("invalidTrainer")).thenReturn(null);

        // When & Then
        InternalErrorException exception = assertThrows(
                InternalErrorException.class,
                () -> traineeService.addTrainers(trainee, dto)
        );

        // Verify exception message
        assertEquals(
                "No Trainer Found with Username: invalidTrainer. No Trainers Added",
                exception.getMessage()
        );

        // Ensure that save was not called since an exception was thrown
        verify(traineeRepository, never()).save(trainee);
    }

    @Test
    void testFindByIdSuccess() {
        // Given
        Long traineeId = 1L;
        Trainee expectedTrainee = new Trainee();
        expectedTrainee.setId(traineeId);

        when(traineeRepository.findById(traineeId)).thenReturn(Optional.of(expectedTrainee));

        // When
        Trainee result = traineeService.findById(traineeId);

        // Then
        assertNotNull(result);
        assertEquals(expectedTrainee, result);
        verify(traineeRepository, times(1)).findById(traineeId);
    }

    @Test
    void testFindByIdThrowsExceptionWhenNotFound() {
        // Given
        Long traineeId = 1L;

        when(traineeRepository.findById(traineeId)).thenReturn(Optional.empty());

        // When & Then
        ProfileNotFoundException exception = assertThrows(
                ProfileNotFoundException.class,
                () -> traineeService.findById(traineeId)
        );

        // Verify exception message
        assertEquals("No Trainee Found with Id: 1", exception.getMessage());
        verify(traineeRepository, times(1)).findById(traineeId);
    }

    @Test
    void testCreateTrainingSuccess() {
        // Given
        Trainee trainee = new Trainee();
        TraineeTrainingCreateDTO trainingDTO = new TraineeTrainingCreateDTO(
                "trainerUser",
                "b",
                ZonedDateTime.parse("05.05.1995 00:00:00 " + ZoneId.systemDefault(),
                DateTimeFormatter.ofPattern(AppConstants.DEFAULT_DATE_FORMAT)),
                1);

        Trainer trainer = new Trainer();
        when(trainerService.findByUsername("trainerUser")).thenReturn(trainer);

        // When
        traineeService.createTraining(trainee, trainingDTO);

        // Then
        verify(trainerService, times(1)).findByUsername("trainerUser");
        verify(trainingService, times(1)).save(any(Training.class));
    }

    @Test
    void testCreateTrainingThrowsExceptionWhenTrainerNotFound() {
        // Given
        var trainee = new Trainee();
        var trainingDTO = new TraineeTrainingCreateDTO(
                "invalidTrainerUser",
                "b",
                ZonedDateTime.parse("05.05.1995 00:00:00 " + ZoneId.systemDefault(),
                        DateTimeFormatter.ofPattern(AppConstants.DEFAULT_DATE_FORMAT)),
                1);

        var expectedException =
                new InternalErrorException("No Trainer Found with Username: invalidTrainerUser");

        when(trainerService.findByUsername("invalidTrainerUser")).thenThrow(expectedException);

        // When & Then
        var exception = assertThrows(
                InternalErrorException.class,
                () -> traineeService.createTraining(trainee, trainingDTO)
        );

        // Verify exception message
        assertEquals("No Trainer Found with Username: invalidTrainerUser", exception.getMessage());
        verify(trainerService, times(1)).findByUsername("invalidTrainerUser");
        verify(trainingService, never()).save(any(Training.class)); // ensure that save is not called
    }
}
