package com.epam.wca.gym.facade;

import com.epam.wca.gym.entity.Trainee;
import com.epam.wca.gym.service.TraineeService;
import com.epam.wca.gym.service.facade.AuthenticationService;
import com.epam.wca.gym.service.facade.TrainingFacadeService;
import com.epam.wca.gym.service.facade.UserFacadeService;
import com.epam.wca.gym.utils.InputHandler;
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
        System.out.println("Choose action:");
        System.out.println("l - login");
        System.out.println("r - register");
        System.out.println("q - quit");

        String choice = scanner.nextLine();

        switch (choice) {
            case "l" -> loggedIn = login(scanner);
            case "r" -> authenticationService.registerUser(scanner);
            case "q" -> {
                System.out.println("Exiting...");
                appRunning = false;
            }
            default -> System.out.println("Invalid option, please try again.");
        }
    }

    private void handleLoggedInState(Scanner scanner) {
        System.out.println("Choose action:");
        System.out.println("c - create training");
        System.out.println("u - update user information");
        System.out.println("g - get user information");
        System.out.println("d - delete Trainee");
        System.out.println("f - find Training Info");
        System.out.println("l - log out");

        String choice = scanner.nextLine();

        switch (choice) {
            case "c" -> trainingFacadeService.createTraining(scanner);
            case "u" -> userFacadeService.updateUserInformation(username, scanner);
            case "g" -> userFacadeService.getUserInformation(username);
            case "d" -> delete();
            case "f" -> trainingFacadeService.findTrainingInfo(scanner);
            case "l" -> loggedIn = false;
            default -> System.out.println("Invalid option, please try again.");
        }
    }

    private boolean login(Scanner scanner) {
        String username = InputHandler.promptForInput(scanner, "Enter username:");

        loggedIn = authenticationService.login(scanner, username);
        if (loggedIn) {
            this.username = username;
        }

        return loggedIn;
    }

    private void delete() {
        Trainee trainee = traineeService.findByUsername(username);
        if (trainee != null) {
            traineeService.deleteByUsername(username);
            loggedIn = false;
        } else {
            System.out.println("You are not Trainee.");
        }
    }
}
