package com.epam.wca.gym.facade.service;

import com.epam.wca.gym.dto.TraineeDTO;
import com.epam.wca.gym.dto.TrainerDTO;
import com.epam.wca.gym.entity.TrainingType;
import com.epam.wca.gym.entity.User;
import com.epam.wca.gym.service.impl.TraineeService;
import com.epam.wca.gym.service.impl.TrainerService;
import com.epam.wca.gym.service.impl.TrainingTypeService;
import com.epam.wca.gym.service.impl.UserService;
import com.epam.wca.gym.util.AppConstants;
import com.epam.wca.gym.util.DateParser;
import com.epam.wca.gym.util.InputHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserService userService;
    private final TraineeService traineeService;
    private final TrainerService trainerService;
    private final TrainingTypeService trainingTypeService;

    public boolean login(Scanner scanner, String username) {
        String password = InputHandler.promptForInput(scanner, "Enter password:");

        User user = userService.findByUniqueName(username);

        if (user != null && user.getPassword().equals(password)) {
            log.info("Successful login.");

            log.info("User authenticated: " + username);

            return true;
        }

        log.info("Unsuccessful authentication attempt!");

        log.info("Invalid user information. Please try again.");

        return false;
    }

    public void registerUser(Scanner scanner) {
        log.info("Register as:");
        log.info("1 - Trainee");
        log.info("2 - Trainer");
        String choice = scanner.nextLine();

        switch (choice) {
            case "1" -> registerTrainee(scanner);
            case "2" -> registerTrainer(scanner);
            default -> log.info("Invalid choice, please try again.");
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
            printUserDetails(traineeService.save(traineeDTO));
        } else {
            log.info("Invalid Date Format. Trainee not created");
        }
    }

    private void registerTrainer(Scanner scanner) {
        String firstName = InputHandler.promptForInput(scanner, "Enter first name:");
        String lastName = InputHandler.promptForInput(scanner, "Enter last name:");
        TrainingType trainingType = selectTrainingType(scanner);

        if (trainingType != null) {
            TrainerDTO trainerDTO = new TrainerDTO(firstName, lastName, trainingType);
            printUserDetails(trainerService.save(trainerDTO));
        } else {
            log.info("Invalid Date Format. Trainer not created");
        }
    }

    private void printUserDetails(User newUser) {
        log.info("User registered successfully!");
        log.info("Username: " + newUser.getUserName());
        log.info("Password: " + newUser.getPassword());
        log.info("New User Registered: " + newUser.getUserName());
    }

    public TrainingType selectTrainingType(Scanner scanner) {
        while (true) {
            log.info("Select training type:");
            int i = 1;

            List<TrainingType> trainingTypes = trainingTypeService.findAll();

            for (TrainingType type : trainingTypes) {
                log.info(i + " - " + type.getType());
                i ++;
            }
            String choice = scanner.nextLine();

            try {
                return trainingTypes.get(Integer.parseInt(choice) - 1);
            } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                log.info("Invalid training type choice. Please try again.");
            }
        }
    }
}
