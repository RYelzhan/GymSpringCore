package com.epam.wca.gym.facade.service;

import com.epam.wca.gym.entity.*;
import com.epam.wca.gym.facade.user.UserSession;
import com.epam.wca.gym.service.impl.TraineeService;
import com.epam.wca.gym.service.impl.TrainerService;
import com.epam.wca.gym.service.impl.TrainingService;
import com.epam.wca.gym.service.impl.TrainingTypeService;
import com.epam.wca.gym.util.AppConstants;
import com.epam.wca.gym.util.DateParser;
import com.epam.wca.gym.util.InputHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class TrainingFindingFacadeService {
    private final TraineeService traineeService;
    private final TrainerService trainerService;
    private final TrainingService trainingService;
    private final TrainingTypeService trainingTypeService;
    private final UserSession userSession;
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

        TrainingType trainingType = selectTrainingType();

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

    public TrainingType selectTrainingType() {
        while (true) {
            log.info("Select training type:");
            int i = 1;
            for (TrainingType type : trainingTypeService.findAll()) {
                log.info(i + " - " + type.getType());
                i++;
            }
            String choice = scanner.nextLine();

            try {
                return trainingTypeService.findAll().get(Integer.parseInt(choice) - 1);
            } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                log.info("Invalid training type choice. Please try again.");
            }
        }
    }
}
