package com.epam.wca.gym.facade.service;

import com.epam.wca.gym.entity.Trainee;
import com.epam.wca.gym.entity.Trainer;
import com.epam.wca.gym.entity.TrainingType;
import com.epam.wca.gym.entity.User;
import com.epam.wca.gym.facade.user.UserSession;
import com.epam.wca.gym.util.AppConstants;
import com.epam.wca.gym.util.deprecated.DateParser;
import com.epam.wca.gym.util.deprecated.InputHandler;
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
public class UserFacadeService {
    @NonNull
    private final TrainingTypeFacadeService trainingTypeFacadeService;
    @NonNull
    private final UserSession userSession;
    @NonNull
    private final Scanner scanner;

    public void updateUserInformation() {
        if (userSession.getUser() instanceof Trainee trainee) {
            updateTrainee(trainee);
        } else if (userSession.getUser() instanceof Trainer trainer) {
            updateTrainer(trainer);
        } else {
            log.info("User not found.");
        }
    }

    private void updatePassword(User user) {
        log.info("Current Password: " + user.getPassword());
        String newPassword = InputHandler.promptForPassword(scanner);

        user.setPassword(newPassword);
    }

    private void updateTrainee(Trainee trainee) {
        updatePassword(trainee);

        updateActivity(trainee);

        log.info("Current date of birth: " + trainee.getDateOfBirth());
        ZonedDateTime newDateOfBirth = DateParser.parseDate(scanner,
                "Enter new date of birth: (" + AppConstants.DEFAULT_DATE_FORMAT + ")");

        trainee.setDateOfBirth(newDateOfBirth);

        log.info("Current address: " + trainee.getAddress());
        String newAddress = InputHandler.promptForInput(scanner,
                "Enter new address:");

        trainee.setAddress(newAddress);

        log.info("Trainee information updated successfully!");
    }

    private void updateTrainer(Trainer trainer) {
        updatePassword(trainer);

        updateActivity(trainer);

        log.info("Current Specialization: " + trainer.getSpecialization());
        TrainingType newSpecialization = trainingTypeFacadeService.selectTrainingType();

        trainer.setSpecialization(newSpecialization);

        log.info("Trainer information updated successfully!");
    }

    private void updateActivity(User user) {
        log.info("Active: " + user.isActive());
        boolean activity = InputHandler.promptForBoolean(scanner);

        user.setActive(activity);
    }

    public void getUserInformation() {
        if (userSession.getUser() instanceof Trainee trainee) {
            displayTraineeInfo(trainee);
        } else if (userSession.getUser() instanceof Trainer trainer) {
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
}