package com.epam.wca.statistics.service.impl;

import com.epam.wca.common.gymcommon.statistics_dto.TrainerTrainingAddDTO;
import com.epam.wca.common.gymcommon.statistics_dto.TrainerWorkloadSummary;
import com.epam.wca.common.gymcommon.statistics_dto.TrainersTrainingsDeleteDTO;
import com.epam.wca.statistics.aop.Logging;
import com.epam.wca.statistics.entity.TrainerWorkload;
import com.epam.wca.statistics.exception.BadDataException;
import com.epam.wca.statistics.exception.NoDataException;
import com.epam.wca.statistics.repository.TrainerWorkloadRepository;
import com.epam.wca.statistics.service.TrainerService;
import com.epam.wca.statistics.util.DTOFactory;
import com.epam.wca.statistics.util.EntityFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class TrainerServiceImpl implements TrainerService {
    public static final String TRAINING_DURATION_FOR_DELETION_CAN_NOT_BE_BIGGER_THAN_EXISTING_TRAINING_DURATION =
            "Training duration for deletion can not be bigger than training duration of trainer on that day.";
    public static final String NO_TRAINING_INFO_FOUND_FOR_TRAINER_ON_GIVEN_YEAR_AND_MONTH = "No training info found for trainer \"%s\" on year \"%s\" and month \"%s\"";

    private final TrainerWorkloadRepository trainerWorkloadRepository;

    @Override
    @Transactional
    public void addNewTraining(TrainerTrainingAddDTO trainingAddDTO) {
        var trainerWorkload = trainerWorkloadRepository.findByUsernameAndYearAndMonth(
                trainingAddDTO.username(),
                trainingAddDTO.date().getYear(),
                trainingAddDTO.date().getMonthValue()
        ).orElse(EntityFactory.covertToZeroDurationEntity(trainingAddDTO));

        trainerWorkload.setDuration(trainerWorkload.getDuration() + trainingAddDTO.duration());

        log.info("Saved Workload: " + trainerWorkload);

        trainerWorkloadRepository.save(trainerWorkload);
    }

    @Override
    @Logging
    public TrainerWorkloadSummary getWorkload(String username) {
        List<TrainerWorkload> trainerWorkloads = trainerWorkloadRepository.findAllByUsername(username);

        Map<Integer, Map<Integer, Integer>> trainingSummary = new HashMap<>();

        trainerWorkloads.forEach(trainerWorkload ->
                trainingSummary
                        .computeIfAbsent(trainerWorkload.getYear(), k -> new HashMap<>())
                        .merge(trainerWorkload.getMonth(), trainerWorkload.getDuration(), Integer::sum));

        return DTOFactory.convertToDto(username, trainingSummary);
    }

    @Override
    @Logging
    @Transactional
    public void deleteTrainings(TrainersTrainingsDeleteDTO trainingsDeleteDTO) {
        trainingsDeleteDTO.getTrainingsInfo().forEach((trainerUsername, trainingDetails) ->
                trainingDetails.forEach((trainingDate, duration) -> {
                    TrainerWorkload trainingWorkload = getTrainerWorkload(trainerUsername, trainingDate);
                    validateTrainingDuration(trainingWorkload, duration);

                    if (duration.equals(trainingWorkload.getDuration())) {
                        trainerWorkloadRepository.deleteById(trainingWorkload.getId());
                    } else {
                        reduceTrainingDuration(trainingWorkload, duration);
                    }
                })
        );
    }

    private TrainerWorkload getTrainerWorkload(String trainerUsername, ZonedDateTime trainingDate) {
        int year = trainingDate.getYear();
        int month = trainingDate.getMonthValue();

        return trainerWorkloadRepository.findByUsernameAndYearAndMonth(trainerUsername, year, month)
                .orElseThrow(() -> new NoDataException(
                        NO_TRAINING_INFO_FOUND_FOR_TRAINER_ON_GIVEN_YEAR_AND_MONTH
                                .formatted(trainerUsername, year, month)
                ));
    }

    private void validateTrainingDuration(TrainerWorkload trainingWorkload, Integer duration) {
        if (duration > trainingWorkload.getDuration()) {
            throw new BadDataException(
                    TRAINING_DURATION_FOR_DELETION_CAN_NOT_BE_BIGGER_THAN_EXISTING_TRAINING_DURATION
            );
        }
    }

    private void reduceTrainingDuration(TrainerWorkload trainingWorkload, Integer duration) {
        trainingWorkload.setDuration(trainingWorkload.getDuration() - duration);
        trainerWorkloadRepository.save(trainingWorkload);
    }
}
