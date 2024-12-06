package com.epam.wca.statistics.service;

import com.epam.wca.common.gymcommon.statistics_dto.TrainerTrainingAddDTO;
import com.epam.wca.common.gymcommon.statistics_dto.TrainerWorkloadSummary;
import com.epam.wca.common.gymcommon.statistics_dto.TrainersTrainingsDeleteDTO;

public interface TrainerService {
    void addNewTraining(TrainerTrainingAddDTO trainingAddDTO);

    TrainerWorkloadSummary getWorkload(String username);

    void deleteTrainings(TrainersTrainingsDeleteDTO trainingsDeleteDTO);
}
