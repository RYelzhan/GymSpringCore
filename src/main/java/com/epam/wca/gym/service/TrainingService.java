package com.epam.wca.gym.service;

import com.epam.wca.gym.entity.Training;

public interface TrainingService {
    void createTraining(Training training);
    Training findTrainingById(long id);
}
