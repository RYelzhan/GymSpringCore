package com.epam.wca.gym.util;

import com.epam.wca.gym.dto.TraineeDTO;
import com.epam.wca.gym.dto.TrainerSavingDTO;
import com.epam.wca.gym.entity.Trainee;
import com.epam.wca.gym.entity.Trainer;
import lombok.experimental.UtilityClass;

@UtilityClass
public class UserFactory {

    public static Trainee createTrainee(TraineeDTO traineeDTO) {
        return new Trainee(traineeDTO.firstName(),
                traineeDTO.lastName(),
                traineeDTO.dateOfBirth(),
                traineeDTO.address());
    }

    public static Trainer createTrainer(TrainerSavingDTO trainerDTO) {
        return new Trainer(trainerDTO.firstName(),
                trainerDTO.lastName(),
                trainerDTO.trainingType());
    }
}
