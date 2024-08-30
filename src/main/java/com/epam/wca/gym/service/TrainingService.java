package com.epam.wca.gym.service;

import com.epam.wca.gym.dto.TrainingDTO;
import com.epam.wca.gym.entity.Training;

public interface TrainingService {
    void createTraining(TrainingDTO trainingDTO);
    Training findTrainingById(long id);
}
