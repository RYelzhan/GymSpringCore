package com.epam.wca.gym.facade.service;

import com.epam.wca.gym.dto.TrainingDTO;
import com.epam.wca.gym.entity.Trainee;
import com.epam.wca.gym.entity.Trainer;
import com.epam.wca.gym.entity.Training;
import com.epam.wca.gym.entity.TrainingType;
import com.epam.wca.gym.facade.user.UserSession;
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
public class TrainingCreatingFacadeService {
    private final TrainerService trainerService;
    private final TrainingService trainingService;
    private final TrainingTypeService trainingTypeService;
    private final UserSession userSession;
    private final Scanner scanner;

    public void listAllTrainers(Trainee trainee) {
        List<Trainer> allTrainers = trainerService.findAll();
        Set<Trainer> assignedTrainers = trainee.getTrainersAssigned();

        for (Trainer trainer : allTrainers) {
            log.info("Trainer: " + trainer +
                    " Assigned: " + assignedTrainers.contains(trainer));
        }
    }

    public void addTrainer() {
        if (userSession.getUser() instanceof Trainee trainee) {
            listAllTrainers(trainee);

            Long trainerId = InputHandler.promptForLong(scanner, "Enter trainer ID to add: ");
            Trainer trainer = trainerService.findById(trainerId);
            if (trainer == null) {
                log.info("No such Trainer!");
                return;
            }

            trainee.getTrainersAssigned().add(trainer);
        }
    }

    public void listAvailableTrainers(Trainee trainee) {
        Set<Trainer> assignedTrainers = trainee.getTrainersAssigned();

        log.info("Available Trainers: ");

        for (Trainer trainer : assignedTrainers) {
            log.info("Trainer: " + trainer);
        }
    }
    public void createTraining() {
        if (userSession.getUser() instanceof Trainee trainee) {
            if (trainee.getTrainersAssigned().isEmpty()) {
                log.info("Need to add Trainers first");
                return;
            }

            String trainingName = InputHandler.promptForInput(scanner, "Enter training name:");

            TrainingType trainingType = selectTrainingType();

            listAvailableTrainers(trainee);
            Long trainerId = InputHandler.promptForLong(scanner, "Enter trainer ID:");

            ZonedDateTime trainingDate = DateParser.parseDate(scanner,
                    "Enter training date (" + AppConstants.DEFAULT_DATE_FORMAT + "):");

            Integer trainingDuration = InputHandler.promptForInt(scanner, "Enter duration (in minutes):");

            TrainingDTO trainingDTO = new TrainingDTO(trainee.getId(),
                    trainerId,
                    trainingName,
                    trainingType,
                    trainingDate,
                    trainingDuration);

            try {
                Training newTraining = trainingService.save(trainingDTO);

                log.info("Training created successfully. ID: " + newTraining.getId());
            } catch (IllegalStateException e) {
                log.info("Invalid participant details. Try Again.");
            }
        } else {
            log.info("You're not a Trainee");
        }
    }

    public TrainingType selectTrainingType() {
        while (true) {
            log.info("Select training type:");
            int i = 1;
            for (TrainingType type : trainingTypeService.findAll()) {
                log.info(i + " - " + type.getType());
                i ++;
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
