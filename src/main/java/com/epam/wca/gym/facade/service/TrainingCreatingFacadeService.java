package com.epam.wca.gym.facade.service;

import com.epam.wca.gym.dto.TrainingDTO;
import com.epam.wca.gym.entity.Trainee;
import com.epam.wca.gym.entity.Trainer;
import com.epam.wca.gym.entity.Training;
import com.epam.wca.gym.entity.TrainingType;
import com.epam.wca.gym.facade.user.UserSession;
import com.epam.wca.gym.service.impl.TrainerService;
import com.epam.wca.gym.service.impl.TrainingService;
import com.epam.wca.gym.util.AppConstants;
import com.epam.wca.gym.util.DateParser;
import com.epam.wca.gym.util.InputHandler;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

/**
 * @deprecated This class is deprecated. It was used in console version of application.
 */

@Deprecated(since = "2.0")

@Slf4j
@Component
@RequiredArgsConstructor
public class TrainingCreatingFacadeService {
    @NonNull
    private final TrainerService trainerService;
    @NonNull
    private final TrainingService trainingService;
    @NonNull
    private final UserSession userSession;
    @NonNull
    private final Scanner scanner;

    public List<Trainer> getListOfNotAssignedTrainers(Set<Trainer> assignedTrainers,
                                                      List<Trainer> allTrainers) {
        List<Trainer> listOfTrainers = new ArrayList<>();
        for (Trainer trainer : allTrainers) {
            if (!assignedTrainers.contains(trainer)) {
                listOfTrainers.add(trainer);
            }
        }

        return listOfTrainers;
    }

    public void addTrainer() {
        if (userSession.getUser() instanceof Trainee trainee) {
            List<Trainer> listOfTrainers = getListOfNotAssignedTrainers(trainee.getTrainersAssigned(),
                    trainerService.findAll());

            Trainer newTrainer = chooseAvailableTrainer(listOfTrainers);

            if (newTrainer != null) {
                trainee.getTrainersAssigned().add(newTrainer);
            } else {
                log.info("No Available Trainers.");
            }
        } else {
            log.info("Trainer Can not add Trainer.");
        }
    }

    private Trainer chooseAvailableTrainer(List<Trainer> listOfTrainers) {
        if (listOfTrainers.isEmpty()) {
            return null;
        }

        while (true) {
            log.info("Available Trainers: ");

            for (int i = 0; i < listOfTrainers.size(); i++) {
                log.info("Number: " + (i + 1) + "\nTrainer Information:\n" + listOfTrainers.get(i));
            }

            int trainerNumber = InputHandler.promptForInt(scanner, "Enter trainer number:");
            if (trainerNumber <= 0 || listOfTrainers.size() < trainerNumber) {
                log.info("Wrong Number. Try Again.");
            } else {
                return listOfTrainers.get(trainerNumber - 1);
            }
        }
    }

    public Trainer chooseAssignedTrainer(Trainee trainee) {
        Set<Trainer> assignedTrainers = trainee.getTrainersAssigned();

        List<Trainer> listOfTrainers = new ArrayList<>(assignedTrainers);

        return chooseAvailableTrainer(listOfTrainers);
    }

    public void createTraining() {
        if (userSession.getUser() instanceof Trainee trainee) {
            if (trainee.getTrainersAssigned().isEmpty()) {
                log.info("Need to add Trainers first");
                return;
            }

            Trainer trainer = chooseAssignedTrainer(trainee);

            String trainingName = InputHandler.promptForInput(scanner, "Enter training name:");

            TrainingType trainingType = trainer.getSpecialization();

            ZonedDateTime trainingDate = DateParser.parseDate(scanner,
                    "Enter training date (" + AppConstants.DEFAULT_DATE_FORMAT + "):");

            Integer trainingDuration = InputHandler.promptForInt(scanner, "Enter duration (in minutes):");

            TrainingDTO trainingDTO = new TrainingDTO(trainee.getId(),
                    trainer.getId(),
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
}
