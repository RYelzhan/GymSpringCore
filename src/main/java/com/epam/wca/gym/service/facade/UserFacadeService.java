package com.epam.wca.gym.service.facade;

import com.epam.wca.gym.entity.Trainee;
import com.epam.wca.gym.entity.Trainer;
import com.epam.wca.gym.entity.TrainingType;
import com.epam.wca.gym.service.TraineeService;
import com.epam.wca.gym.service.TrainerService;
import com.epam.wca.gym.utils.AppConstants;
import com.epam.wca.gym.utils.DateParser;
import com.epam.wca.gym.utils.InputHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Scanner;

@Component
@RequiredArgsConstructor
public class UserFacadeService {
    private final TraineeService traineeService;
    private final TrainerService trainerService;

    public void updateUserInformation(String username, Scanner scanner) {
        Trainee trainee = traineeService.findByUsername(username);
        Trainer trainer = trainerService.findByUsername(username);

        if (trainee != null) {
            updateTrainee(scanner, trainee);
        } else if (trainer != null) {
            updateTrainer(scanner, trainer);
        } else {
            System.out.println("User not found.");
        }
    }

    private void updateTrainee(Scanner scanner, Trainee trainee) {
        System.out.println("Current date of birth: " + trainee.getDateOfBirth());
        LocalDate newDateOfBirth = DateParser.parseDate(scanner,
                "Enter new date of birth: (" + AppConstants.DEFAULT_DATE_FORMAT + ")");
        if (newDateOfBirth != null) {
            trainee.setDateOfBirth(newDateOfBirth);
        }

        System.out.println("Current address: " + trainee.getAddress());
        String newAddress = InputHandler.promptForInput(scanner,
                "Enter new address:");
        if (!newAddress.isEmpty()) {
            trainee.setAddress(newAddress);
        }

        traineeService.updateByUsername(trainee.getUserName(), trainee);

        System.out.println("Trainee information updated successfully!");
    }

    private void updateTrainer(Scanner scanner, Trainer trainer) {
        TrainingType newSpecialization = InputHandler.selectTrainingType(scanner);
        if (newSpecialization != null) {
            trainer.setSpecialization(newSpecialization);
        }

        trainerService.updateByUsername(trainer.getUserName(), trainer);

        System.out.println("Trainer information updated successfully!");
    }

    public void getUserInformation(String username) {
        Trainee trainee = traineeService.findByUsername(username);
        Trainer trainer = trainerService.findByUsername(username);

        if (trainee != null) {
            displayTraineeInfo(trainee);
        } else if (trainer != null) {
            displayTrainerInfo(trainer);
        } else {
            System.out.println("No user found with the provided username.");
        }
    }

    private void displayTraineeInfo(Trainee trainee) {
        System.out.println("Trainee Information:\n" + trainee);
    }

    private void displayTrainerInfo(Trainer trainer) {
        System.out.println("Trainer Information:\n" + trainer);
    }
}