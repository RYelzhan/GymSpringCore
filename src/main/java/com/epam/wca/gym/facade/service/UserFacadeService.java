package com.epam.wca.gym.facade.service;

import com.epam.wca.gym.entity.Trainee;
import com.epam.wca.gym.entity.Trainer;
import com.epam.wca.gym.entity.TrainingType;
import com.epam.wca.gym.service.impl.TraineeService;
import com.epam.wca.gym.service.impl.TrainerService;
import com.epam.wca.gym.service.impl.TrainingTypeService;
import com.epam.wca.gym.util.AppConstants;
import com.epam.wca.gym.util.DateParser;
import com.epam.wca.gym.util.InputHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Scanner;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserFacadeService {
    private final TraineeService traineeService;
    private final TrainerService trainerService;
    private final TrainingTypeService trainingTypeService;

    public void updateUserInformation(String username, Scanner scanner) {
        Trainee trainee = traineeService.findByUniqueName(username);
        Trainer trainer = trainerService.findByUniqueName(username);

        if (trainee != null) {
            updateTrainee(scanner, trainee);
        } else if (trainer != null) {
            updateTrainer(scanner, trainer);
        } else {
            log.info("User not found.");
        }
    }

    private void updateTrainee(Scanner scanner, Trainee trainee) {
        log.info("Current date of birth: " + trainee.getDateOfBirth());
        LocalDate newDateOfBirth = DateParser.parseDate(scanner,
                "Enter new date of birth: (" + AppConstants.DEFAULT_DATE_FORMAT + ")");
        if (newDateOfBirth != null) {
            trainee.setDateOfBirth(newDateOfBirth);
        }

        log.info("Current address: " + trainee.getAddress());
        String newAddress = InputHandler.promptForInput(scanner,
                "Enter new address:");
        if (!newAddress.isEmpty()) {
            trainee.setAddress(newAddress);
        }

        traineeService.update(trainee);

        log.info("Trainee information updated successfully!");
    }

    private void updateTrainer(Scanner scanner, Trainer trainer) {
        TrainingType newSpecialization = selectTrainingType(scanner);
        if (newSpecialization != null) {
            trainer.setSpecialization(newSpecialization);
        }

        trainerService.update(trainer);

        log.info("Trainer information updated successfully!");
    }

    public void getUserInformation(String username) {
        Trainee trainee = traineeService.findByUniqueName(username);
        Trainer trainer = trainerService.findByUniqueName(username);

        if (trainee != null) {
            displayTraineeInfo(trainee);
        } else if (trainer != null) {
            displayTrainerInfo(trainer);
        } else {
            log.info("No user found with the provided username.");
        }
    }

    private void displayTraineeInfo(Trainee trainee) {
        log.info("Trainee Information:\n" + trainee);
    }

    private void displayTrainerInfo(Trainer trainer) {
        log.info("Trainer Information:\n" + trainer);
    }

    public TrainingType selectTrainingType(Scanner scanner) {
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