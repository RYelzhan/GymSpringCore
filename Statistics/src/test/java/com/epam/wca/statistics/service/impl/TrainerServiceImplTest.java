package com.epam.wca.statistics.service.impl;

import com.epam.wca.common.gymcommon.statistics_dto.TrainerTrainingAddDTO;
import com.epam.wca.common.gymcommon.statistics_dto.TrainersTrainingsDeleteDTO;
import com.epam.wca.statistics.exception.BadDataException;
import com.epam.wca.statistics.exception.NoDataException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TrainerServiceImplTest {
    @Mock
    private TrainerWorkloadRepository trainerWorkloadRepository;
    @InjectMocks
    private TrainerServiceImpl trainerService;

    @Test
    void addNewTraining_ShouldAddNewTraining_WhenTrainerWorkloadExists() {
        // Arrange
        TrainerTrainingAddDTO trainingAddDTO = new TrainerTrainingAddDTO(
                "ethan.white",
                "Ethan",
                "White",
                true,
                ZonedDateTime.now(),
                20
        );

        TrainerWorkload existingWorkload = new TrainerWorkload(
                "ethan.white",
                "ethan",
                "white",
                true,
                2024, 12, 0
        );

        when(trainerWorkloadRepository.findByUsernameAndYearAndMonth(
                trainingAddDTO.username(),
                trainingAddDTO.date().getYear(),
                trainingAddDTO.date().getMonthValue()
        )).thenReturn(Optional.of(existingWorkload));

        // Act
        trainerService.addNewTraining(trainingAddDTO);

        // Assert
        Mockito.verify(trainerWorkloadRepository).save(Mockito.argThat(
                workload -> workload.getDuration() == 20
        ));
    }

    @Test
    void addNewTraining_ShouldCreateNewTrainingWorkload_WhenTrainerWorkloadDoesNotExist() {
        // Arrange
        TrainerTrainingAddDTO trainingAddDTO = new TrainerTrainingAddDTO(
                "ethan.white",
                "Ethan",
                "White",
                true,
                ZonedDateTime.now(),
                20
        );

        when(trainerWorkloadRepository.findByUsernameAndYearAndMonth(
                trainingAddDTO.username(),
                trainingAddDTO.date().getYear(),
                trainingAddDTO.date().getMonthValue()
        )).thenReturn(Optional.empty());

        // Act
        trainerService.addNewTraining(trainingAddDTO);

        // Assert
        Mockito.verify(trainerWorkloadRepository).save(Mockito.any(TrainerWorkload.class));
    }

    @Test
    void getWorkload_ShouldReturnWorkloadSummary_ForExistingTrainer() {
        // Arrange
        List<TrainerWorkload> workloads = List.of(
                new TrainerWorkload(
                        "ethan.white",
                        "ethan",
                        "white",
                        true,
                        2024, 12, 5),
                new TrainerWorkload(
                        "ethan.white",
                        "ethan",
                        "white",
                        true,
                        2025, 11, 3)
        );
        when(trainerWorkloadRepository.findAllByUsername("ethan.white")).thenReturn(workloads);

        // Act
        var summary = trainerService.getWorkload("ethan.white");

        // Assert
        Assertions.assertNotNull(summary);
        Assertions.assertEquals(2, summary.trainingSummary().size());
    }

    @Test
    void deleteTrainings_ShouldDeleteTraining_WhenDurationMatchesExactly() {
        // Arrange
        TrainersTrainingsDeleteDTO deleteDTO = new TrainersTrainingsDeleteDTO(Map.of(
                "ethan.white", Map.of(ZonedDateTime.now(), 5)
        ));
        TrainerWorkload workload = new TrainerWorkload(
                        1L,
                        "ethan.white",
                        "ethan",
                        "white",
                        true,
                        2024, 12, 5
        );

        when(trainerWorkloadRepository.findByUsernameAndYearAndMonth(
                "ethan.white", 2024, 12
        )).thenReturn(Optional.of(workload));

        // Act
        trainerService.deleteTrainings(deleteDTO);

        // Assert
        verify(trainerWorkloadRepository).deleteById(1L);
    }

    @Test
    void deleteTrainings_ShouldReduceTrainingDuration_WhenDurationIsLessThanExisting() {
        // Arrange
        TrainersTrainingsDeleteDTO deleteDTO = new TrainersTrainingsDeleteDTO(Map.of(
                "ethan.white", Map.of(ZonedDateTime.now(), 3)
        ));

        TrainerWorkload workload = new TrainerWorkload(
                "ethan.white",
                "ethan",
                "white",
                true,
                2024, 12, 5
        );

        when(trainerWorkloadRepository.findByUsernameAndYearAndMonth(
                "ethan.white", 2024, 12
        )).thenReturn(Optional.of(workload));

        // Act
        trainerService.deleteTrainings(deleteDTO);

        // Assert
        Mockito.verify(trainerWorkloadRepository).save(Mockito.argThat(
                w -> w.getDuration() == 2
        ));
    }

    @Test
    void deleteTrainings_ShouldThrowException_WhenDurationExceedsExisting() {
        // Arrange
        TrainersTrainingsDeleteDTO deleteDTO = new TrainersTrainingsDeleteDTO(Map.of(
                "trainer1", Map.of(ZonedDateTime.now(), 10)
        ));

        TrainerWorkload workload = new TrainerWorkload(
                "ethan.white",
                "ethan",
                "white",
                true,
                2024, 12, 5
        );

        when(trainerWorkloadRepository.findByUsernameAndYearAndMonth(
                "trainer1", 2024, 12
        )).thenReturn(Optional.of(workload));

        // Act & Assert
        Exception exception = Assertions.assertThrows(BadDataException.class, () -> {
            trainerService.deleteTrainings(deleteDTO);
        });
        Assertions.assertEquals(
                TrainerServiceImpl.
                        TRAINING_DURATION_FOR_DELETION_CAN_NOT_BE_BIGGER_THAN_EXISTING_TRAINING_DURATION,
                exception.getMessage()
        );
    }

    @Test
    void deleteTrainings_ShouldThrowException_WhenNoTrainingFound() {
        // Arrange
        TrainersTrainingsDeleteDTO deleteDTO = new TrainersTrainingsDeleteDTO(Map.of(
                "trainer1", Map.of(ZonedDateTime.now(), 5)
        ));

        when(trainerWorkloadRepository.findByUsernameAndYearAndMonth(
                "trainer1", 2024, 12
        )).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = Assertions.assertThrows(NoDataException.class, () -> {
            trainerService.deleteTrainings(deleteDTO);
        });
        Assertions.assertTrue(exception.getMessage().contains("No training info found for trainer"));
    }
}
