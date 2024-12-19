package com.epam.wca.statistics.service.impl;

import com.epam.wca.common.gymcommon.statistics_dto.TrainerTrainingAddDTO;
import com.epam.wca.common.gymcommon.statistics_dto.TrainersTrainingsDeleteDTO;
import com.epam.wca.common.gymcommon.util.AppConstants;
import com.epam.wca.statistics.entity.TrainerTrainingSummary;
import com.epam.wca.statistics.exception.BadDataException;
import com.epam.wca.statistics.exception.NoDataException;
import com.epam.wca.statistics.repository.TrainerTrainingSummaryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TrainerServiceImplTest {
    @Mock
    private TrainerTrainingSummaryRepository trainingSummaryRepository;
    @InjectMocks
    private TrainerServiceImpl trainerService;

    @Test
    void addNewTraining_ShouldAddNewTraining_WhenTrainerWorkloadExists() {
        // Arrange
        final int YEAR = 2024;
        final int MONTH = 12;
        final int DURATION_EXISTING = 10;
        final int DURATION_TO_ADD = 20;

        TrainerTrainingAddDTO trainingAddDTO = new TrainerTrainingAddDTO(
                "ethan.white",
                "Ethan",
                "White",
                true,
                ZonedDateTime.parse("01.%s.%s 00:00:00 ".formatted(MONTH, YEAR) + ZoneId.systemDefault(),
                        DateTimeFormatter.ofPattern(AppConstants.DEFAULT_DATE_FORMAT)),
                DURATION_TO_ADD
        );

        Map<Integer, Integer> monthSummary = new HashMap<>();
        monthSummary.put(MONTH, DURATION_EXISTING);
        Map<Integer, Map<Integer, Integer>> yearSummary = new HashMap<>();
        yearSummary.put(YEAR, monthSummary);

        TrainerTrainingSummary existingWorkload = new TrainerTrainingSummary(
                "ethan.white",
                "ethan",
                "white",
                true,
                yearSummary
        );

        when(trainingSummaryRepository.findTrainerTrainingSummaryByUsername(
                trainingAddDTO.username()
        )).thenReturn(Optional.of(existingWorkload));

        // Act
        trainerService.addNewTraining(trainingAddDTO);

        // Assert
        Mockito.verify(trainingSummaryRepository).save(
                Mockito.argThat(
                        workload ->
                                workload.getSummary().get(YEAR).get(MONTH) == DURATION_EXISTING + DURATION_TO_ADD
                )
        );
    }

    @Test
    void addNewTraining_ShouldCreateNewTrainingWorkload_WhenTrainerWorkloadDoesNotExist() {
        // Arrange
        final int YEAR = 2024;
        final int MONTH = 12;
        final int DURATION_TO_ADD = 20;

        TrainerTrainingAddDTO trainingAddDTO = new TrainerTrainingAddDTO(
                "ethan.white",
                "Ethan",
                "White",
                true,
                ZonedDateTime.parse("01.%s.%s 00:00:00 ".formatted(MONTH, YEAR) + ZoneId.systemDefault(),
                        DateTimeFormatter.ofPattern(AppConstants.DEFAULT_DATE_FORMAT)),
                DURATION_TO_ADD
        );

        when(trainingSummaryRepository.findTrainerTrainingSummaryByUsername(
                trainingAddDTO.username()
        )).thenReturn(Optional.empty());

        // Act
        trainerService.addNewTraining(trainingAddDTO);

        // Assert
        ArgumentCaptor<TrainerTrainingSummary> captor = ArgumentCaptor.forClass(TrainerTrainingSummary.class);
        verify(trainingSummaryRepository).insert(captor.capture());

        TrainerTrainingSummary actualSummary = captor.getValue();
        assertNotNull(actualSummary);
        assertEquals(DURATION_TO_ADD, actualSummary.getSummary().get(YEAR).get(MONTH));
    }

    @Test
    void getWorkload_ShouldReturnWorkloadSummary_WithExistingTrainings() {
        // Arrange
        final int YEAR_1 = 2024;
        final int MONTH_1 = 12;
        final int DURATION_EXISTING_1 = 10;
        final int YEAR_2 = 2025;
        final int MONTH_2 = 10;
        final int DURATION_EXISTING_2 = 15;
        final int COUNT_OF_SUMMARIES = 2;

        final String USERNAME = "ethan.white";

        Map<Integer, Integer> monthSummary1 = new HashMap<>();
        monthSummary1.put(MONTH_1, DURATION_EXISTING_1);
        Map<Integer, Map<Integer, Integer>> yearSummary = new HashMap<>();
        yearSummary.put(YEAR_1, monthSummary1);

        Map<Integer, Integer> monthSummary2 = new HashMap<>();
        monthSummary2.put(MONTH_2, DURATION_EXISTING_2);
        yearSummary.put(YEAR_2, monthSummary2);

        var existingWorkload = new TrainerTrainingSummary(
                USERNAME,
                "ethan",
                "white",
                true,
                yearSummary
        );

        when(trainingSummaryRepository.findTrainerTrainingSummaryByUsername(USERNAME))
                .thenReturn(Optional.of(existingWorkload));

        // Act
        var summary = trainerService.getWorkload(USERNAME);

        // Assert
        assertNotNull(summary);
        assertEquals(COUNT_OF_SUMMARIES, summary.trainingSummary().size());
        assertEquals(DURATION_EXISTING_1, summary.trainingSummary().get(YEAR_1).get(MONTH_1));
        assertEquals(DURATION_EXISTING_2, summary.trainingSummary().get(YEAR_2).get(MONTH_2));
    }

    @Test
    void getWorkload_ShouldReturnWorkloadSummary_WithoutExistingTrainings() {
        // Arrange
        final String USERNAME = "ethan.white";

        when(trainingSummaryRepository.findTrainerTrainingSummaryByUsername(USERNAME))
                .thenReturn(Optional.empty());

        // Act
        var summary = trainerService.getWorkload(USERNAME);

        // Assert
        assertNotNull(summary);
        assertTrue(summary.trainingSummary().isEmpty());
    }

    @Test
    void deleteTrainings_ShouldDeleteTraining_WhenDurationMatchesExactly() {
        // Arrange
        final String USERNAME = "ethan.white";

        final int YEAR = 2024;
        final int MONTH = 12;
        final int DURATION_EXISTING = 5;
        final int DURATION_TO_SUBTRACT = 5;

        var deleteDTO = new TrainersTrainingsDeleteDTO(Map.of(
                USERNAME, Map.of(
                        ZonedDateTime.parse("01.%s.%s 00:00:00 ".formatted(MONTH, YEAR) + ZoneId.systemDefault(),
                                DateTimeFormatter.ofPattern(AppConstants.DEFAULT_DATE_FORMAT)),
                        DURATION_TO_SUBTRACT
                )
        ));

        Map<Integer, Integer> monthSummary = new HashMap<>();
        monthSummary.put(MONTH, DURATION_EXISTING);
        Map<Integer, Map<Integer, Integer>> yearSummary = new HashMap<>();
        yearSummary.put(YEAR, monthSummary);

        var workload = new TrainerTrainingSummary(
                        String.valueOf(1L),
                        USERNAME,
                        "ethan",
                        "white",
                        true,
                        yearSummary
        );

        when(trainingSummaryRepository.findTrainerTrainingSummaryByUsername(
                USERNAME
        )).thenReturn(Optional.of(workload));

        // Act
        trainerService.deleteTrainings(deleteDTO);

        // Assert
        verify(trainingSummaryRepository).save(workload);
        assertNull(workload.getSummary().get(YEAR).get(MONTH));
    }

    @Test
    void deleteTrainings_ShouldReduceTrainingDuration_WhenDurationIsLessThanExisting() {
        // Arrange
        final String USERNAME = "ethan.white";

        final int YEAR = 2024;
        final int MONTH = 12;
        final int DURATION_EXISTING = 5;
        final int DURATION_TO_SUBTRACT = 3;

        TrainersTrainingsDeleteDTO deleteDTO = new TrainersTrainingsDeleteDTO(Map.of(
                USERNAME, Map.of(
                        ZonedDateTime.parse("01.%s.%s 00:00:00 ".formatted(MONTH, YEAR) + ZoneId.systemDefault(),
                                DateTimeFormatter.ofPattern(AppConstants.DEFAULT_DATE_FORMAT)),
                        DURATION_TO_SUBTRACT
                )
        ));

        Map<Integer, Integer> monthSummary = new HashMap<>();
        monthSummary.put(MONTH, DURATION_EXISTING);
        Map<Integer, Map<Integer, Integer>> yearSummary = new HashMap<>();
        yearSummary.put(YEAR, monthSummary);

        var workload = new TrainerTrainingSummary(
                USERNAME,
                "ethan",
                "white",
                true,
                yearSummary
        );

        when(trainingSummaryRepository.findTrainerTrainingSummaryByUsername(
                USERNAME
        )).thenReturn(Optional.of(workload));

        // Act
        trainerService.deleteTrainings(deleteDTO);

        // Assert
        Mockito.verify(trainingSummaryRepository).save(Mockito.argThat(
                w -> w.getSummary().get(YEAR).get(MONTH).equals(DURATION_EXISTING - DURATION_TO_SUBTRACT)
        ));
    }

    @Test
    void deleteTrainings_ShouldThrowException_WhenDurationExceedsExisting() {
        // Arrange
        final String USERNAME = "ethan.white";

        final int YEAR = 2024;
        final int MONTH = 12;
        final int DURATION_EXISTING = 5;
        final int DURATION_TO_SUBTRACT = 10;

        TrainersTrainingsDeleteDTO deleteDTO = new TrainersTrainingsDeleteDTO(Map.of(
                USERNAME, Map.of(
                        ZonedDateTime.parse("01.%s.%s 00:00:00 ".formatted(MONTH, YEAR) + ZoneId.systemDefault(),
                                DateTimeFormatter.ofPattern(AppConstants.DEFAULT_DATE_FORMAT)),
                        DURATION_TO_SUBTRACT
                )
        ));

        Map<Integer, Integer> monthSummary = new HashMap<>();
        monthSummary.put(MONTH, DURATION_EXISTING);
        Map<Integer, Map<Integer, Integer>> yearSummary = new HashMap<>();
        yearSummary.put(YEAR, monthSummary);

        TrainerTrainingSummary workload = new TrainerTrainingSummary(
                USERNAME,
                "ethan",
                "white",
                true,
                yearSummary
        );

        when(trainingSummaryRepository.findTrainerTrainingSummaryByUsername(
                USERNAME
        )).thenReturn(Optional.of(workload));

        // Act & Assert
        Exception exception = Assertions.assertThrows(BadDataException.class, () -> {
            trainerService.deleteTrainings(deleteDTO);
        });
        assertEquals(
                TrainerServiceImpl.
                        SUMMARY_DURATION_LESS_THAN_SUBTRACTING.formatted(DURATION_EXISTING, DURATION_TO_SUBTRACT),
                exception.getMessage()
        );
    }

    @Test
    void deleteTrainings_ShouldThrowException_WhenNoTrainingFound() {
        // Arrange
        final String USERNAME = "ethan.white";

        final int YEAR = 2024;
        final int MONTH = 12;
        final int DURATION_TO_SUBTRACT = 5;

        TrainersTrainingsDeleteDTO deleteDTO = new TrainersTrainingsDeleteDTO(Map.of(
                USERNAME, Map.of(
                        ZonedDateTime.parse("01.%s.%s 00:00:00 ".formatted(MONTH, YEAR) + ZoneId.systemDefault(),
                                DateTimeFormatter.ofPattern(AppConstants.DEFAULT_DATE_FORMAT)),
                        DURATION_TO_SUBTRACT
                )
        ));

        when(trainingSummaryRepository.findTrainerTrainingSummaryByUsername(
                USERNAME
        )).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = Assertions.assertThrows(NoDataException.class, () -> {
            trainerService.deleteTrainings(deleteDTO);
        });
        assertEquals(
                TrainerServiceImpl.NO_TRAINER_SUMMARY_FOUND_FOR_TRAINER.formatted(USERNAME),
                exception.getMessage()
        );
    }

    @Test
    void deleteTrainings_ShouldThrowException_WhenNoSummaryForYearFound() {
        // Arrange
        final String USERNAME = "ethan.white";

        final int YEAR = 2024;
        final int MONTH = 12;
        final int DURATION_TO_SUBTRACT = 10;

        TrainersTrainingsDeleteDTO deleteDTO = new TrainersTrainingsDeleteDTO(Map.of(
                USERNAME, Map.of(
                        ZonedDateTime.parse("01.%s.%s 00:00:00 ".formatted(MONTH, YEAR) + ZoneId.systemDefault(),
                                DateTimeFormatter.ofPattern(AppConstants.DEFAULT_DATE_FORMAT)),
                        DURATION_TO_SUBTRACT
                )
        ));

        Map<Integer, Map<Integer, Integer>> yearSummary = new HashMap<>();

        TrainerTrainingSummary workload = new TrainerTrainingSummary(
                USERNAME,
                "ethan",
                "white",
                true,
                yearSummary
        );

        when(trainingSummaryRepository.findTrainerTrainingSummaryByUsername(
                USERNAME
        )).thenReturn(Optional.of(workload));

        // Act & Assert
        Exception exception = Assertions.assertThrows(NoDataException.class, () -> {
            trainerService.deleteTrainings(deleteDTO);
        });
        assertEquals(
                TrainerServiceImpl.
                        EMPTY_YEAR.formatted(YEAR),
                exception.getMessage()
        );
    }

    @Test
    void deleteTrainings_ShouldThrowException_WhenNoSummaryForMonthFound() {
        // Arrange
        final String USERNAME = "ethan.white";

        final int YEAR = 2024;
        final int MONTH = 12;
        final int DURATION_TO_SUBTRACT = 10;

        TrainersTrainingsDeleteDTO deleteDTO = new TrainersTrainingsDeleteDTO(Map.of(
                USERNAME, Map.of(
                        ZonedDateTime.parse("01.%s.%s 00:00:00 ".formatted(MONTH, YEAR) + ZoneId.systemDefault(),
                                DateTimeFormatter.ofPattern(AppConstants.DEFAULT_DATE_FORMAT)),
                        DURATION_TO_SUBTRACT
                )
        ));

        Map<Integer, Integer> monthSummary = new HashMap<>();
        Map<Integer, Map<Integer, Integer>> yearSummary = new HashMap<>();
        yearSummary.put(YEAR, monthSummary);

        TrainerTrainingSummary workload = new TrainerTrainingSummary(
                USERNAME,
                "ethan",
                "white",
                true,
                yearSummary
        );

        when(trainingSummaryRepository.findTrainerTrainingSummaryByUsername(
                USERNAME
        )).thenReturn(Optional.of(workload));

        // Act & Assert
        Exception exception = Assertions.assertThrows(NoDataException.class, () -> {
            trainerService.deleteTrainings(deleteDTO);
        });
        assertEquals(
                TrainerServiceImpl.
                        NO_SUMMARY_FOR_YEAR_AND_MONTH.formatted(YEAR, MONTH),
                exception.getMessage()
        );
    }
}
