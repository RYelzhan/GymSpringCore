package com.epam.wca.gym.util;

import com.epam.wca.gym.dto.training.TraineeTrainingQuery;
import com.epam.wca.gym.dto.training.TrainerTrainingQuery;
import com.epam.wca.gym.entity.Training;
import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.Set;

@UtilityClass
public class Filter {
    public static List<Training> filterTraineeTrainings(Set<Training> trainings,
                                                        TraineeTrainingQuery traineeTrainingQuery) {
        return trainings.stream()
                .filter(training -> traineeTrainingQuery.dateFrom() == null ||
                        training.getTrainingDate().isAfter(traineeTrainingQuery.dateFrom()))
                .filter(training -> traineeTrainingQuery.dateTo() == null ||
                        training.getTrainingDate().isBefore(traineeTrainingQuery.dateTo()))
                .filter(training -> traineeTrainingQuery.trainerName() == null ||
                        training.getTrainer().getUsername().equals(traineeTrainingQuery.trainerName()))
                .filter(training -> traineeTrainingQuery.trainingType() == null ||
                        training.getTrainingType().toString().equals(traineeTrainingQuery.trainingType()))
                .toList();
    }

    public static List<Training> filterTrainerTrainings(Set<Training> trainings,
                                                        TrainerTrainingQuery trainerTrainingQuery) {
        return trainings.stream()
                .filter(training -> trainerTrainingQuery.dateFrom() == null ||
                        training.getTrainingDate().isAfter(trainerTrainingQuery.dateFrom()))
                .filter(training -> trainerTrainingQuery.dateTo() == null ||
                        training.getTrainingDate().isBefore(trainerTrainingQuery.dateTo()))
                .filter(training -> trainerTrainingQuery.traineeName() == null ||
                        training.getTrainee().getUsername().equals(trainerTrainingQuery.traineeName()))
                .toList();
    }
}
