package com.epam.wca.gym.util;

import com.epam.wca.gym.dto.trainee.TraineeTrainingCreateDTO;
import com.epam.wca.gym.dto.trainer.TrainerTrainingCreateDTO;
import com.epam.wca.gym.entity.Trainee;
import com.epam.wca.gym.entity.Trainer;
import com.epam.wca.gym.entity.Training;
import lombok.experimental.UtilityClass;

@UtilityClass
public class TrainingFactory {
    public static Training createTraining(TraineeTrainingCreateDTO trainingDTO,
                                          Trainee trainee,
                                          Trainer trainer) {
        return new Training(
                trainee,
                trainer,
                trainingDTO.trainingName(),
                trainer.getSpecialization(),
                trainingDTO.date(),
                trainingDTO.trainingDuration()
        );
    }

    public static Training createTraining(TrainerTrainingCreateDTO trainingDTO,
                                          Trainee trainee,
                                          Trainer trainer) {
        return new Training(
                trainee,
                trainer,
                trainingDTO.trainingName(),
                trainer.getSpecialization(),
                trainingDTO.date(),
                trainingDTO.trainingDuration()
        );
    }
}
