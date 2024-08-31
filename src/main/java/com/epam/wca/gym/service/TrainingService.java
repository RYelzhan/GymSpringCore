package com.epam.wca.gym.service;

import com.epam.wca.gym.dto.TrainingDTO;
import com.epam.wca.gym.entity.Training;

public interface TrainingService {
    Training createTraining(TrainingDTO trainingDTO);
    Training findById(long id);
}
