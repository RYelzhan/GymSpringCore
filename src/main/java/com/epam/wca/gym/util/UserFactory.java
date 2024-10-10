package com.epam.wca.gym.util;

import com.epam.wca.gym.dto.trainee.TraineeRegistrationDTO;
import com.epam.wca.gym.dto.trainer.TrainerRegistrationDTO;
import com.epam.wca.gym.dto.trainer.TrainerSavingDTO;
import com.epam.wca.gym.entity.Trainee;
import com.epam.wca.gym.entity.Trainer;
import com.epam.wca.gym.entity.TrainingType;
import lombok.experimental.UtilityClass;

@UtilityClass
public class UserFactory {

    public static Trainee createTrainee(TraineeRegistrationDTO traineeRegistrationDTO) {
        return new Trainee(traineeRegistrationDTO.firstName(),
                traineeRegistrationDTO.lastName(),
                traineeRegistrationDTO.dateOfBirth(),
                traineeRegistrationDTO.address());
    }

    /**
     * @deprecated
     * Used for Trainer object creation
     * @return Obeject of Trainer
     */
    @Deprecated(since = "2.1")
    public static Trainer createTrainer(TrainerSavingDTO trainerDTO) {
        return new Trainer(trainerDTO.firstName(),
                trainerDTO.lastName(),
                trainerDTO.trainingType());
    }

    public static Trainer createTrainer(TrainerRegistrationDTO trainerDTO, TrainingType trainingType) {
        return new Trainer(trainerDTO.firstName(),
                trainerDTO.lastName(),
                trainingType);
    }
}
