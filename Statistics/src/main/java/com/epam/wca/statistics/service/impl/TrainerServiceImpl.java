package com.epam.wca.statistics.service.impl;

import com.epam.wca.common.gymcommon.aop.Logging;
import com.epam.wca.common.gymcommon.statistics_dto.TrainerTrainingAddDTO;
import com.epam.wca.common.gymcommon.statistics_dto.TrainerWorkloadSummary;
import com.epam.wca.common.gymcommon.statistics_dto.TrainersTrainingsDeleteDTO;
import com.epam.wca.statistics.exception.BadDataException;
import com.epam.wca.statistics.repository.TrainerTrainingSummaryRepository;
import com.epam.wca.statistics.service.TrainerService;
import com.epam.wca.statistics.util.DTOFactory;
import com.epam.wca.statistics.util.EntityFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class TrainerServiceImpl implements TrainerService {
    public static final String TRAINING_DURATION_FOR_DELETION_CAN_NOT_BE_BIGGER_THAN_EXISTING_TRAINING_DURATION =
            "Training duration for deletion can not be bigger than training duration of trainer on that day.";
    public static final String NO_TRAINING_INFO_FOUND_FOR_TRAINER_ON_GIVEN_YEAR_AND_MONTH =
            "No training info found for trainer \"%s\" on year \"%s\" and month \"%s\"";

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
        trainingsDeleteDTO.getTrainingsInfo().forEach((username, trainerDetails) -> {
            var trainerTrainingSummaryHolder =
                    trainingSummaryRepository.findTrainerTrainingSummaryByUsername(username);

            if (trainerTrainingSummaryHolder.isEmpty() && !trainerDetails.isEmpty()) {
                throw new BadDataException("No Trainer Summary found for trainer: " + username);
            }

            if (trainerTrainingSummaryHolder.isEmpty()) {
                return;
            }

            var trainerTrainingSummary = trainerTrainingSummaryHolder.get();

            Map<Integer, Map<Integer,Integer>> yearSummary = trainerTrainingSummary.getSummary();

            trainerDetails.forEach((date, durationToSubtract) -> {
                Map<Integer, Integer> monthSummary = yearSummary.get(date.getYear());
                if (monthSummary == null) {
                    throw new BadDataException("No Training summary available for year: " + date.getYear());
                }

                Integer availableDuration = monthSummary.get(date.getMonthValue());
                if (availableDuration == null) {
                    throw new BadDataException("No Training summary available for year %s and month %s"
                            .formatted(date.getYear(), date.getMonthValue()));
                }

                int leftDuration = availableDuration - durationToSubtract;

                if (leftDuration < 0) {
                    throw new BadDataException(("Training summary duration is less than duration wanting to subtract." +
                            "Available duration: %s, Duration wanted to subtract: %s")
                            .formatted(availableDuration, durationToSubtract));
                }

                monthSummary.put(date.getMonthValue(), leftDuration);
            });

            trainingSummaryRepository.save(trainerTrainingSummary);
        });
    }
}
