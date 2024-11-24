package com.epam.wca.gym.communication.impl;

import com.epam.wca.common.gymcommon.statistics_dto.TrainerTrainingAddDTO;
import com.epam.wca.common.gymcommon.statistics_dto.TrainerWorkloadSummary;
import com.epam.wca.common.gymcommon.statistics_dto.TrainersTrainingsDeleteDTO;
import com.epam.wca.gym.communication.feign.StatisticsClient;
import com.epam.wca.gym.communication.StatisticsCommunicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StatisticsCommunicationServiceImpl implements StatisticsCommunicationService {
    private final StatisticsClient statisticsClient;

    @Override
    public void deleteTrainings(TrainersTrainingsDeleteDTO trainingsDeleteDTO) {
        statisticsClient.deleteTrainings(trainingsDeleteDTO);
    }

    @Override
    public void addNewTraining(TrainerTrainingAddDTO trainingAddDTO) {
        statisticsClient.addNewTraining(trainingAddDTO);
    }

    @Override
    public TrainerWorkloadSummary getWorkload(String username) {
        return statisticsClient.getWorkload(username);
    }
}
