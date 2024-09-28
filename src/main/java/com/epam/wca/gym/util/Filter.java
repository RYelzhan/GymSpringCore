package com.epam.wca.gym.util;

import com.epam.wca.gym.dto.training.TraineeTrainingDTO;
import com.epam.wca.gym.dto.training.TrainerTrainingDTO;
import com.epam.wca.gym.entity.Training;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Filter {
    public static List<Training> filterTraineeTrainings(Set<Training> trainings,
                                                        TraineeTrainingDTO traineeTrainingDTO) {
        return trainings.stream()
                .filter(training -> traineeTrainingDTO.dateFrom() == null ||
                        training.getTrainingDate().isAfter(traineeTrainingDTO.dateFrom()))
                .filter(training -> traineeTrainingDTO.dateTo() == null ||
                        training.getTrainingDate().isBefore(traineeTrainingDTO.dateTo()))
                .filter(training -> traineeTrainingDTO.trainerName() == null ||
                        training.getTrainer().getUserName().equals(traineeTrainingDTO.trainerName()))
                .filter(training -> traineeTrainingDTO.trainingType() == null ||
                        training.getTrainingType().toString().equals(traineeTrainingDTO.trainingType()))
                .collect(Collectors.toList());
    }

    public static List<Training> filterTrainerTrainings(Set<Training> trainings,
                                                        TrainerTrainingDTO trainerTrainingDTO) {
        return trainings.stream()
                .filter(training -> trainerTrainingDTO.dateFrom() == null ||
                        training.getTrainingDate().isAfter(trainerTrainingDTO.dateFrom()))
                .filter(training -> trainerTrainingDTO.dateTo() == null ||
                        training.getTrainingDate().isBefore(trainerTrainingDTO.dateTo()))
                .filter(training -> trainerTrainingDTO.traineeName() == null ||
                        training.getTrainee().getUserName().equals(trainerTrainingDTO.traineeName()))
                .collect(Collectors.toList());
    }
}
