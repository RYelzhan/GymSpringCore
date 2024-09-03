package com.epam.wca.gym.utils;

import com.epam.wca.gym.dto.TraineeDTO;
import com.epam.wca.gym.dto.TrainerDTO;
import com.epam.wca.gym.entity.Trainee;
import com.epam.wca.gym.entity.Trainer;

public class UserFactory {
    public static Trainee createTrainee(TraineeDTO traineeDTO) {
        return new Trainee(traineeDTO.firstName(), traineeDTO.lastName(), traineeDTO.dateOfBirth(), traineeDTO.address());
    }

    public static Trainer createTrainer(TrainerDTO trainerDTO) {
        return new Trainer(trainerDTO.firstName(), trainerDTO.lastName(), trainerDTO.trainingType());
    }
}
