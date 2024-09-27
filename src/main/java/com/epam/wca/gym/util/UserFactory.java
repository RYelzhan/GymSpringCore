package com.epam.wca.gym.util;

import com.epam.wca.gym.dto.TraineeGettingDTO;
import com.epam.wca.gym.dto.TrainerSavingDTO;
import com.epam.wca.gym.entity.Trainee;
import com.epam.wca.gym.entity.Trainer;
import lombok.experimental.UtilityClass;

@UtilityClass
public class UserFactory {

    public static Trainee createTrainee(TraineeGettingDTO traineeGettingDTO) {
        return new Trainee(traineeGettingDTO.firstName(),
                traineeGettingDTO.lastName(),
                traineeGettingDTO.dateOfBirth(),
                traineeGettingDTO.address());
    }

    public static Trainer createTrainer(TrainerSavingDTO trainerDTO) {
        return new Trainer(trainerDTO.firstName(),
                trainerDTO.lastName(),
                trainerDTO.trainingType());
    }
}
