package com.epam.wca.gym.communication;

import com.epam.wca.common.gymcommon.statistics_dto.TrainerTrainingAddDTO;
import com.epam.wca.common.gymcommon.statistics_dto.TrainersTrainingsDeleteDTO;

public interface StatisticsCommunicationService {
    void deleteTrainings(TrainersTrainingsDeleteDTO trainingsDeleteDTO);

    void addNewTraining(TrainerTrainingAddDTO trainingAddDTO);
}
