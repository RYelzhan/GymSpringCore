package com.epam.wca.gym.service.facade;

import com.epam.wca.gym.dto.TrainingDTO;
import com.epam.wca.gym.entity.Training;
import com.epam.wca.gym.entity.TrainingType;
import com.epam.wca.gym.service.TrainingService;
import com.epam.wca.gym.utils.AppConstants;
import com.epam.wca.gym.utils.DateParser;
import com.epam.wca.gym.utils.InputHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Scanner;

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

            System.out.println("Training created successfully. ID: " + newTraining.getTrainingId());
        } catch (IllegalStateException e) {
            System.out.println("Invalid participant details. Try Again.");
        }
    }

    public void findTrainingInfo(Scanner scanner) {
        long trainingId = InputHandler.promptForLong(scanner, "Enter Training ID:");
        Training training = trainingService.findById(trainingId);

        if (training != null) {
            System.out.println("Training found: " + training);
        } else {
            System.out.println("Training with ID " + trainingId + " not found.");
        }
    }
}
