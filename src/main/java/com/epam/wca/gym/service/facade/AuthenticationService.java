package com.epam.wca.gym.service.facade;

import com.epam.wca.gym.dto.TraineeDTO;
import com.epam.wca.gym.dto.TrainerDTO;
import com.epam.wca.gym.entity.Trainee;
import com.epam.wca.gym.entity.Trainer;
import com.epam.wca.gym.entity.TrainingType;
import com.epam.wca.gym.entity.User;
import com.epam.wca.gym.service.TraineeService;
import com.epam.wca.gym.service.TrainerService;
import com.epam.wca.gym.utils.AppConstants;
import com.epam.wca.gym.utils.DateParser;
import com.epam.wca.gym.utils.InputHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Scanner;
@Slf4j
@Component
@RequiredArgsConstructor
public class AuthenticationService {
    private final TraineeService traineeService;
    private final TrainerService trainerService;

    public boolean login(Scanner scanner, String username) {
        String password = InputHandler.promptForInput(scanner, "Enter password:");

        Trainee trainee = traineeService.findByUsername(username);
        Trainer trainer = trainerService.findByUsername(username);

        if (trainee != null && trainee.getPassword().equals(password) ||
                trainer != null && trainer.getPassword().equals(password)) {
            System.out.println("Successful login.");

            log.info("User authenticated: " + username);

            return true;
        }

        log.info("Unsuccessful authentication attempt!");

        System.out.println("Invalid user information. Please try again.");

        return false;
    }

    public void registerUser(Scanner scanner) {
        System.out.println("Register as:");
        System.out.println("1 - Trainee");
        System.out.println("2 - Trainer");
        String choice = scanner.nextLine();

        switch (choice) {
            case "1" -> registerTrainee(scanner);
            case "2" -> registerTrainer(scanner);
            default -> System.out.println("Invalid choice, please try again.");
        }
    }
    private void registerTrainee(Scanner scanner) {
        String firstName = InputHandler.promptForInput(scanner, "Enter first name:");
        String lastName = InputHandler.promptForInput(scanner, "Enter last name:");
        LocalDate dateOfBirth = DateParser.parseDate(scanner,
                "Enter date of birth (" + AppConstants.DEFAULT_DATE_FORMAT + "):");
        String address = InputHandler.promptForInput(scanner, "Enter address:");

        if (dateOfBirth != null) {
            TraineeDTO traineeDTO = new TraineeDTO(firstName, lastName, dateOfBirth, address);
            printUserDetails(traineeService.create(traineeDTO));
        } else {
            System.out.println("Invalid Date Format. Trainee not created");
        }
    }

    private void registerTrainer(Scanner scanner) {
        String firstName = InputHandler.promptForInput(scanner, "Enter first name:");
        String lastName = InputHandler.promptForInput(scanner, "Enter last name:");
        TrainingType trainingType = InputHandler.selectTrainingType(scanner);

        if (trainingType != null) {
            TrainerDTO trainerDTO = new TrainerDTO(firstName, lastName, trainingType);
            printUserDetails(trainerService.create(trainerDTO));
        } else {
            System.out.println("Invalid Date Format. Trainer not created");
        }
    }

    private void printUserDetails(User newUser) {
        System.out.println("User registered successfully!");
        System.out.println("Username: " + newUser.getUserName());
        System.out.println("Password: " + newUser.getPassword());
        log.info("New User Registered: " + newUser.getUserName());
    }
}
