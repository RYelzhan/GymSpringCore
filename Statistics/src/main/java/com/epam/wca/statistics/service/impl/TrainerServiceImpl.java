package com.epam.wca.statistics.service.impl;

import com.epam.wca.common.gymcommon.aop.Logging;
import com.epam.wca.common.gymcommon.statistics_dto.TrainerTrainingAddDTO;
import com.epam.wca.common.gymcommon.statistics_dto.TrainerWorkloadSummary;
import com.epam.wca.common.gymcommon.statistics_dto.TrainersTrainingsDeleteDTO;
import com.epam.wca.statistics.exception.BadDataException;
import com.epam.wca.statistics.exception.NoDataException;
import com.epam.wca.statistics.repository.TrainerTrainingSummaryRepository;
import com.epam.wca.statistics.service.TrainerService;
import com.epam.wca.statistics.util.DTOFactory;
import com.epam.wca.statistics.util.EntityFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class TrainerServiceImpl implements TrainerService {
    public static final String SUMMARY_DURATION_LESS_THAN_SUBTRACTING = "Training summary duration is less than duration wanting to subtract." +
            " Available duration: %s, Duration wanted to subtract: %s";
    public static final String NO_TRAINER_SUMMARY_FOUND_FOR_TRAINER = "No Trainer Summary found for trainer: %s";
    public static final String EMPTY_YEAR = "No Training summary available for year: %s";
    public static final String NO_SUMMARY_FOR_YEAR_AND_MONTH = "No Training summary available for year %s and month %s";
    private final TrainerTrainingSummaryRepository trainingSummaryRepository;

    @Override
    @Logging
    @Transactional
    public void addNewTraining(TrainerTrainingAddDTO trainingAddDTO) {
        var trainerTrainingSummaryHolder =
                trainingSummaryRepository.findTrainerTrainingSummaryByUsername(
                        trainingAddDTO.username()
                );

        if (trainerTrainingSummaryHolder.isEmpty()) {
            trainingSummaryRepository.insert(EntityFactory.covertToDocument(trainingAddDTO));
            return;
        }

        var trainerTrainingSummary = trainerTrainingSummaryHolder.get();

        Map<Integer, Map<Integer, Integer>> yearSummary = trainerTrainingSummary.getSummary();

        Map<Integer, Integer> monthSummary = yearSummary
                .computeIfAbsent(
                        trainingAddDTO.date().getYear(),
                        k -> new HashMap<>()
                );

        monthSummary.merge(
                trainingAddDTO.date().getMonthValue(),
                trainingAddDTO.duration(),
                Integer::sum
        );

        log.info("Saved Workload: " + trainerTrainingSummary);

        trainingSummaryRepository.save(trainerTrainingSummary);
    }

    @Override
    @Logging
    public TrainerWorkloadSummary getWorkload(String username) {
        var trainerTrainingSummaryHolder =
                trainingSummaryRepository.findTrainerTrainingSummaryByUsername(username);

        if (trainerTrainingSummaryHolder.isEmpty()) {
            return DTOFactory.createEmptyDTO(username);
        }

        var trainerTrainingSummary = trainerTrainingSummaryHolder.get();

        return DTOFactory.convertToDto(trainerTrainingSummary);
    }

    @Override
    @Logging
    @Transactional
    public void deleteTrainings(TrainersTrainingsDeleteDTO trainingsDeleteDTO) {
        trainingsDeleteDTO.getTrainingsInfo().forEach(this::processTrainerDetails);
    }

    private void processTrainerDetails(String username, Map<ZonedDateTime, Integer> trainerDetails) {
        var trainerTrainingSummaryHolder =
                trainingSummaryRepository.findTrainerTrainingSummaryByUsername(username);

        if (trainerTrainingSummaryHolder.isEmpty() && !trainerDetails.isEmpty()) {
            throw new NoDataException(NO_TRAINER_SUMMARY_FOUND_FOR_TRAINER.formatted(username));
        }

        if (trainerTrainingSummaryHolder.isEmpty()) {
            return;
        }

        var trainerTrainingSummary = trainerTrainingSummaryHolder.get();
        Map<Integer, Map<Integer, Integer>> yearSummary = trainerTrainingSummary.getSummary();

        trainerDetails.forEach((date, durationToSubtract) ->
                updateYearAndMonthSummary(yearSummary, date, durationToSubtract)
        );

        trainingSummaryRepository.save(trainerTrainingSummary);
    }

    private void updateYearAndMonthSummary(
            Map<Integer, Map<Integer, Integer>> yearSummary,
            ZonedDateTime date,
            int durationToSubtract
    ) {
        Map<Integer, Integer> monthSummary = yearSummary.get(date.getYear());
        if (monthSummary == null) {
            throw new NoDataException(EMPTY_YEAR.formatted(date.getYear()));
        }

        Integer availableDuration = monthSummary.get(date.getMonthValue());
        if (availableDuration == null) {
            throw new NoDataException(NO_SUMMARY_FOR_YEAR_AND_MONTH
                    .formatted(date.getYear(), date.getMonthValue()));
        }

        adjustMonthSummary(monthSummary, date.getMonthValue(), availableDuration, durationToSubtract);

        if (monthSummary.isEmpty()) {
            yearSummary.remove(date.getYear());
        }
    }

    private void adjustMonthSummary(Map<Integer, Integer> monthSummary, int month,
                                    int availableDuration, int durationToSubtract) {
        int leftDuration = availableDuration - durationToSubtract;

        if (leftDuration < 0) {
            throw new BadDataException(SUMMARY_DURATION_LESS_THAN_SUBTRACTING
                    .formatted(availableDuration, durationToSubtract));
        }

        if (leftDuration == 0) {
            monthSummary.remove(month);
        } else {
            monthSummary.put(month, leftDuration);
        }
    }
}
