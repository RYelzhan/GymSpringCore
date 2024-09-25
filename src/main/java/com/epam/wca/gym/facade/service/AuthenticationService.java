package com.epam.wca.gym.facade.service;

import com.epam.wca.gym.dto.TraineeDTO;
import com.epam.wca.gym.entity.TrainingType;
import com.epam.wca.gym.entity.User;
import com.epam.wca.gym.service.impl.TraineeService;
import com.epam.wca.gym.service.impl.TrainerService;
import com.epam.wca.gym.service.impl.UserService;
import com.epam.wca.gym.util.AppConstants;
import com.epam.wca.gym.util.DateParser;
import com.epam.wca.gym.util.InputHandler;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.Scanner;

/**
 * @deprecated This class is deprecated. It was used in console version of application.
 */

@Deprecated(since = "2.0")

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthenticationService {
    @NonNull
    private final UserService userService;
    @NonNull
    private final TraineeService traineeService;
    @NonNull
    private final TrainerService trainerService;
    @NonNull
    private final TrainingTypeFacadeService trainingTypeFacadeService;
    @NonNull
    private final Scanner scanner;

    public User login(String username) {
        String password = InputHandler.promptForInput(scanner, "Enter password:");

        User user = userService.findByUniqueName(username);

        if (user != null && user.getPassword().equals(password)) {
            log.info("Successful login.");

            log.info("User authenticated: " + username);

            return user;
        }

        log.info("Unsuccessful authentication attempt!");

        log.info("Invalid user information. Please try again.");

        return null;
    }

    public void registerUser() {
        log.info("Register as:");
        log.info("1 - Trainee");
        log.info("2 - Trainer");
        String choice = scanner.nextLine();

        switch (choice) {
            case "1" -> registerTrainee();
            case "2" -> registerTrainer();
            default -> log.info("Invalid choice, please try again.");
        }
    }

    private void registerTrainee() {
        String firstName = InputHandler.promptForInput(scanner, "Enter first name:");
        String lastName = InputHandler.promptForInput(scanner, "Enter last name:");
        ZonedDateTime dateOfBirth = DateParser.parseDate(scanner,
                "Enter date of birth (" + AppConstants.DEFAULT_DATE_FORMAT + "):");
        String address = InputHandler.promptForInput(scanner, "Enter address:");

        TraineeDTO traineeDTO = new TraineeDTO(firstName, lastName, dateOfBirth, address);
        printUserDetails(traineeService.save(traineeDTO));
    }

    private void registerTrainer() {
        String firstName = InputHandler.promptForInput(scanner, "Enter first name:");
        String lastName = InputHandler.promptForInput(scanner, "Enter last name:");
        TrainingType trainingType = trainingTypeFacadeService.selectTrainingType();

        if (trainingType != null) {
//            TrainerDTO trainerDTO = new TrainerDTO(firstName, lastName, trainingType);
 //           printUserDetails(trainerService.save(trainerDTO));
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
}
