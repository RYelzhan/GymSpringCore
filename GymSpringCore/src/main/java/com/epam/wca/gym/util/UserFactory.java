package com.epam.wca.gym.util;

import com.epam.wca.gym.dto.trainee.TraineeRegistrationDTO;
import com.epam.wca.gym.dto.trainer.TrainerRegistrationDTO;
import com.epam.wca.gym.entity.Trainee;
import com.epam.wca.gym.entity.Trainer;
import com.epam.wca.gym.entity.TrainingType;
import lombok.experimental.UtilityClass;

@UtilityClass
public class UserFactory {

    public static Trainee createTrainee(
            TraineeRegistrationDTO traineeRegistrationDTO,
            String username
    ) {
        //TODO: Use Username/Id somehow
        return new Trainee(
                username,
                traineeRegistrationDTO.firstName(),
                traineeRegistrationDTO.lastName(),
                traineeRegistrationDTO.dateOfBirth(),
                traineeRegistrationDTO.address()
        );
    }

    public static Trainer createTrainer(
            TrainerRegistrationDTO trainerDTO,
            TrainingType trainingType,
            String username) {
        //TODO: Use Username/Id somehow
        return new Trainer(
                username,
                trainerDTO.firstName(),
                trainerDTO.lastName(),
                trainingType
        );
    }
}
