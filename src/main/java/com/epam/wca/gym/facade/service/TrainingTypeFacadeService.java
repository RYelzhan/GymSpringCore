package com.epam.wca.gym.facade.service;

import com.epam.wca.gym.entity.TrainingType;
import com.epam.wca.gym.service.impl.TrainingTypeService;
import com.epam.wca.gym.util.deprecated.InputHandler;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Scanner;

/**
 * @deprecated This class is deprecated. It was used in console version of application.
 */

@Deprecated(since = "2.0")

@Slf4j
@Component
@RequiredArgsConstructor
public class TrainingTypeFacadeService {
    @NonNull
    private final TrainingTypeService trainingTypeService;
    @NonNull
    private final Scanner scanner;

    public TrainingType selectTrainingType() {
        List<TrainingType> trainingTypes = trainingTypeService.findAll();

        while (true) {
            log.info("Training types:");
            int i = 1;

            for (TrainingType type : trainingTypes) {
                log.info(i + " - " + type.getType());
                i++;
            }
            int trainingTypeNumberNumber = InputHandler.promptForInt(scanner, "Select training type:");

            if (trainingTypeNumberNumber <= 0 ||
                    trainingTypes.size() < trainingTypeNumberNumber) {
                log.info("Invalid training type choice. Please try again.");
            } else {
                return trainingTypes.get(trainingTypeNumberNumber - 1);
            }
        }
    }
}
