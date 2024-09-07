package com.epam.wca.gym.facade.service;

import com.epam.wca.gym.dto.TrainingDTO;
import com.epam.wca.gym.entity.Training;
import com.epam.wca.gym.entity.TrainingType;
import com.epam.wca.gym.service.TrainingService;
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
public class TrainingFacadeService {
    private final TrainingService trainingService;

    public void createTraining(Scanner scanner) {
        String trainingName = InputHandler.promptForInput(scanner, "Enter training name:");
        TrainingType trainingType = InputHandler.selectTrainingType(scanner);
        Long traineeId = InputHandler.promptForLong(scanner, "Enter trainee ID:");
        Long trainerId = InputHandler.promptForLong(scanner, "Enter trainer ID:");
        LocalDate trainingDate = DateParser.parseDate(scanner,
                "Enter training date (" + AppConstants.DEFAULT_DATE_FORMAT + "):");
        Integer trainingDuration = InputHandler.promptForInt(scanner, "Enter duration (in minutes):");

        TrainingDTO trainingDTO = new TrainingDTO(traineeId, trainerId, trainingName, trainingType, trainingDate, trainingDuration);
        try {
            Training newTraining = trainingService.createTraining(trainingDTO);

            log.info("Training created successfully. ID: " + newTraining.getTrainingId());
        } catch (IllegalStateException e) {
            log.info("Invalid participant details. Try Again.");
        }
    }

    public void findTrainingInfo(Scanner scanner) {
        long trainingId = InputHandler.promptForLong(scanner, "Enter Training ID:");
        Training training = trainingService.findById(trainingId);

        if (training != null) {
            log.info("Training found: " + training);
        } else {
            log.info("Training with ID " + trainingId + " not found.");
        }
    }
}
