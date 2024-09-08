package com.epam.wca.gym.service;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class TrainingServiceTest {
    /*
    @Mock
    private TrainingDAO trainingDAO;

    @Mock
    private TraineeService traineeService;

    @Mock
    private TrainerService trainerService;

    @InjectMocks
    private TrainingService trainingService;

    private TrainingDTO trainingDTO;
    private Training training;

    @BeforeEach
    void setUp() {
        trainingDTO = new TrainingDTO(1L,
                2L,
                "Yoga",
                TrainingType.POWERLIFTING,
                LocalDate.parse("2024.08.31", DateTimeFormatter.ofPattern(AppConstants.DEFAULT_DATE_FORMAT)),
                60);

        training = new Training(1L,
                2L,
                "Yoga",
                TrainingType.POWERLIFTING,
                LocalDate.parse("2024.08.31", DateTimeFormatter.ofPattern(AppConstants.DEFAULT_DATE_FORMAT)),
                60);
    }

    @Test
    void testCreateTraining_Success() {
        when(traineeService.findById(1L))
                .thenReturn(new Trainee());

        when(trainerService.findById(2L))
                .thenReturn(new Trainer());

        when(trainingDAO.save(any(Training.class)))
                .thenReturn(new Training());

        Training createdTraining = trainingService.createTraining(trainingDTO);

        assertNotNull(createdTraining);
        assertEquals("Yoga", createdTraining.getTrainingName());

        verify(trainingDAO, times(1))
                .save(any(Training.class));
    }

    @Test
    void testCreateTraining_TraineeNotFound() {
        when(traineeService.findById(1L))
                .thenReturn(null);

        assertThrows(IllegalStateException.class, () -> trainingService.createTraining(trainingDTO));

        verify(trainingDAO, never()).save(any(Training.class));
    }

    @Test
    void testCreateTraining_TrainerNotFound() {
        when(traineeService.findById(1L))
                .thenReturn(new Trainee());

        when(trainerService.findById(2L))
                .thenReturn(null);

        assertThrows(IllegalStateException.class, () -> trainingService.createTraining(trainingDTO));

        verify(trainingDAO, never()).save(any(Training.class));
    }

    @Test
    void testFindById() {
        when(trainingDAO.findById(1L))
                .thenReturn(training);

        Training foundTraining = trainingService.findById(1L);

        assertNotNull(foundTraining);
        assertEquals("Yoga", foundTraining.getTrainingName());
    }

     */
}
