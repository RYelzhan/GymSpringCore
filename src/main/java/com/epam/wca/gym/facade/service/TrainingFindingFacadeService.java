package com.epam.wca.gym.facade.service;

import com.epam.wca.gym.entity.Trainee;
import com.epam.wca.gym.entity.Trainer;
import com.epam.wca.gym.entity.Training;
import com.epam.wca.gym.entity.TrainingType;
import com.epam.wca.gym.entity.User;
import com.epam.wca.gym.facade.user.UserSession;
import com.epam.wca.gym.service.impl.TraineeService;
import com.epam.wca.gym.service.impl.TrainerService;
import com.epam.wca.gym.service.impl.TrainingService;
import com.epam.wca.gym.util.AppConstants;
import com.epam.wca.gym.util.deprecated.DateParser;
import com.epam.wca.gym.util.deprecated.InputHandler;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

/**
 * @deprecated This class is deprecated. It was used in console version of application.
 */

@Deprecated(since = "2.0")

@Slf4j
@Component
@RequiredArgsConstructor
public class TrainingFindingFacadeService {
    @NonNull
    private final TraineeService traineeService;
    @NonNull
    private final TrainerService trainerService;
    @NonNull
    private final TrainingService trainingService;
    @NonNull
    private final TrainingTypeFacadeService trainingTypeFacadeService;
    @NonNull
    private final UserSession userSession;
    @NonNull

    private final Scanner scanner;

    public void findAllTrainings() {
        User user = userSession.getUser();
        Set<Training> allTrainings;
        if (user instanceof Trainee trainee) {
            allTrainings = traineeService.findAllTrainingsById(trainee.getId());
        } else if (user instanceof Trainer trainer) {
            allTrainings = trainerService.findAllTrainingsById(trainer.getId());
        } else {
            return;
        }

        if (allTrainings.isEmpty()) {
            log.info("No trainings found.");
        } else {
            log.info("Found trainings: " + allTrainings);
        }
    }

    public void findTrainingByCriteria() {
        ZonedDateTime fromDateInput = DateParser.parseDate(scanner,
                "Enter the start date (" + AppConstants.DEFAULT_DATE_FORMAT + "):");

        ZonedDateTime toDateInput = DateParser.parseDate(scanner,
                "Enter the end date (" + AppConstants.DEFAULT_DATE_FORMAT + "):");

        TrainingType trainingType = trainingTypeFacadeService.selectTrainingType();

        User user = userSession.getUser();
        List<Training> trainings;
        if (user instanceof Trainee trainee) {
            String trainerName = InputHandler.promptForInput(scanner,
                    "Enter the trainer's username:");

            trainings = traineeService.findTrainingByCriteria(trainee.getUserName(),
                    fromDateInput,
                    toDateInput,
                    trainerName,
                    trainingType);
        } else if (user instanceof Trainer trainer) {
            String traineeName = InputHandler.promptForInput(scanner,
                    "Enter the trainee's username:");

            trainings = trainerService.findTrainingByCriteria(trainer.getUserName(),
                    fromDateInput,
                    toDateInput,
                    traineeName,
                    trainingType);
        } else {
            return;
        }

        if (trainings.isEmpty()) {
            log.info("No trainings found for the given criteria.");
        } else {
            log.info("Found trainings: " + trainings);
        }
    }

    public void findTrainingInfo() {
        long trainingId = InputHandler.promptForLong(scanner, "Enter Training ID:");
        Training training = trainingService.findById(trainingId);

        if (training != null) {
            log.info("Training found: " + training);
        } else {
            log.info("Training with ID " + trainingId + " not found.");
        }
    }
}
