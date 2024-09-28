package com.epam.wca.gym.util;

import com.epam.wca.gym.dto.TraineeRegistrationDTO;
import com.epam.wca.gym.dto.TrainerSavingDTO;
import com.epam.wca.gym.entity.Trainee;
import com.epam.wca.gym.entity.Trainer;
import lombok.experimental.UtilityClass;

@UtilityClass
public class UserFactory {

    public static Trainee createTrainee(TraineeRegistrationDTO traineeRegistrationDTO) {
        return new Trainee(traineeRegistrationDTO.firstName(),
                traineeRegistrationDTO.lastName(),
                traineeRegistrationDTO.dateOfBirth(),
                traineeRegistrationDTO.address());
    }

    public static Trainer createTrainer(TrainerSavingDTO trainerDTO) {
        return new Trainer(trainerDTO.firstName(),
                trainerDTO.lastName(),
                trainerDTO.trainingType());
    }
}
