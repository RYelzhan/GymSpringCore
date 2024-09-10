package com.epam.wca.gym.facade.service;

import com.epam.wca.gym.dto.TrainingDTO;
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
import java.util.Scanner;
import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class TrainingFacadeService {
    private final TraineeService traineeService;
    private final TrainerService trainerService;
    private final TrainingService trainingService;
    private final TrainingTypeService trainingTypeService;
    private final UserSession userSession;
    private final Scanner scanner;

    public void createTraining() {
        String trainingName = InputHandler.promptForInput(scanner, "Enter training name:");
        TrainingType trainingType = selectTrainingType();
        Long traineeId = InputHandler.promptForLong(scanner, "Enter trainee ID:");
        Long trainerId = InputHandler.promptForLong(scanner, "Enter trainer ID:");
        ZonedDateTime trainingDate = DateParser.parseDate(scanner,
                "Enter training date (" + AppConstants.DEFAULT_DATE_FORMAT + "):");
        Integer trainingDuration = InputHandler.promptForInt(scanner, "Enter duration (in minutes):");

        TrainingDTO trainingDTO = new TrainingDTO(traineeId, trainerId, trainingName, trainingType, trainingDate, trainingDuration);
        try {
            Training newTraining = trainingService.save(trainingDTO);

            log.info("Training created successfully. ID: " + newTraining.getId());
        } catch (IllegalStateException e) {
            log.info("Invalid participant details. Try Again.");
        }
    }

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

        log.info("All trainings: " + allTrainings.toString());
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
                log.info(i + 1 + " - " + type.getType());
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
