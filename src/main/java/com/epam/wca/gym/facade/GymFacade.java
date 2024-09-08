package com.epam.wca.gym.facade;

import com.epam.wca.gym.entity.Trainee;
import com.epam.wca.gym.facade.service.AuthenticationService;
import com.epam.wca.gym.facade.service.TrainingFacadeService;
import com.epam.wca.gym.facade.service.UserFacadeService;
import com.epam.wca.gym.service.impl.TraineeService;
import com.epam.wca.gym.util.InputHandler;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Slf4j
@Component
@RequiredArgsConstructor
public class GymFacade {
    @NonNull
    private AuthenticationService authenticationService;
    @NonNull
    private TrainingFacadeService trainingFacadeService;
    @NonNull
    private UserFacadeService userFacadeService;
    @NonNull
    private final TraineeService traineeService;
    private boolean loggedIn = false;
    private String username;
    private boolean appRunning;

    public void run() {
        try (Scanner scanner = new Scanner(System.in)) {
            log.info("Application Started Successfully!");

            appRunning = true;

            while (appRunning) {
                if (loggedIn) {
                    handleLoggedInState(scanner);
                } else {
                    handleLoggedOutState(scanner);
                }
            }
        }
    }

    private void handleLoggedOutState(Scanner scanner) {
        log.info("Choose action:");
        log.info("l - login");
        log.info("r - register");
        log.info("q - quit");

        String choice = scanner.nextLine();

        switch (choice) {
            case "l" -> loggedIn = login(scanner);
            case "r" -> authenticationService.registerUser(scanner);
            case "q" -> {
                log.info("Exiting...");
                appRunning = false;
            }
            default -> log.info("Invalid option, please try again.");
        }
    }

    private void handleLoggedInState(Scanner scanner) {
        log.info("Choose action:");
        log.info("c - create training");
        log.info("u - update user information");
        log.info("g - get user information");
        log.info("d - delete Trainee");
        log.info("f - find Training Info");
        log.info("l - log out");

        String choice = scanner.nextLine();

        switch (choice) {
            case "c" -> trainingFacadeService.createTraining(scanner);
            case "u" -> userFacadeService.updateUserInformation(username, scanner);
            case "g" -> userFacadeService.getUserInformation(username);
            case "d" -> delete();
            case "f" -> trainingFacadeService.findTrainingInfo(scanner);
            case "l" -> loggedIn = false;
            default -> log.info("Invalid option, please try again.");
        }
    }

    private boolean login(Scanner scanner) {
        String authenticatedUsername = InputHandler.promptForInput(scanner, "Enter username:");

        loggedIn = authenticationService.login(scanner, authenticatedUsername);
        if (loggedIn) {
            this.username = authenticatedUsername;
        }

        return loggedIn;
    }

    private void delete() {
        Trainee trainee = traineeService.findByUniqueName(username);
        if (trainee != null) {
            traineeService.deleteById(trainee.getId());
            loggedIn = false;
        } else {
            log.info("You are not Trainee.");
        }
    }
}
