package com.epam.wca.gym.facade;

import com.epam.wca.gym.entity.Trainee;
import com.epam.wca.gym.facade.service.AuthenticationService;
import com.epam.wca.gym.facade.service.TrainingFacadeService;
import com.epam.wca.gym.facade.service.UserFacadeService;
import com.epam.wca.gym.facade.user.UserSession;
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
    @NonNull
    private final UserSession userSession;
    private boolean appRunning;

    public void run() {
        try (Scanner scanner = new Scanner(System.in)) {
            log.info("Application Started Successfully!");

            appRunning = true;

            while (appRunning) {
                if (userSession.isLoggedIn()) {
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
            case "l" -> login(scanner);
            case "r" -> authenticationService.registerUser();
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
        log.info("ta - list all trainings");
        log.info("tc - list trainings with criteria");
        log.info("l - log out");

        String choice = scanner.nextLine();

        switch (choice) {
            case "c" -> trainingFacadeService.createTraining();
            case "u" -> userFacadeService.updateUserInformation();
            case "g" -> userFacadeService.getUserInformation();
            case "d" -> delete();
            case "f" -> trainingFacadeService.findTrainingInfo();
            case "ta" -> trainingFacadeService.findAllTrainings();
            case "tc" -> trainingFacadeService.findTrainingByCriteria();
            case "l" -> userSession.logOut();
            default -> log.info("Invalid option, please try again.");
        }
    }

    private void login(Scanner scanner) {
        String authenticatedUsername = InputHandler.promptForInput(scanner, "Enter username:");

        userSession.setUser(authenticationService.login(authenticatedUsername));
    }

    private void delete() {
        if (! (userSession.getUser() instanceof Trainee)) {
            log.info("You are not Trainee.");
            return;
        }
        traineeService.deleteById(userSession.getUser().getId());
    }
}
