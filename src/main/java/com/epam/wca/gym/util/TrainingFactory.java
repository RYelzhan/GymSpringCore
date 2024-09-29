package com.epam.wca.gym.util;

import com.epam.wca.gym.dto.training.TrainingGettingDTO;
import com.epam.wca.gym.entity.Trainee;
import com.epam.wca.gym.entity.Trainer;
import com.epam.wca.gym.entity.Training;

public class TrainingFactory {
    public static Training createTraining(TrainingGettingDTO trainingGettingDTO,
                                             Trainee trainee,
                                             Trainer trainer) {
        return new Training(
                trainee,
                trainer,
                trainingGettingDTO.trainingName(),
                trainer.getSpecialization(),
                trainingGettingDTO.date(),
                trainingGettingDTO.trainingDuration()
        );
    }
}
