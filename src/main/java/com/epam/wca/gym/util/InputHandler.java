package com.epam.wca.gym.util;

import com.epam.wca.gym.entity.TrainingType;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.util.Scanner;

@Slf4j
@UtilityClass
public class InputHandler {
    public static String promptForInput(Scanner scanner, String prompt) {
        String input;
        do {
            log.info(prompt);
            input = scanner.nextLine().trim();
            if (input.isEmpty()) {
                log.info("Input cannot be empty. Please try again.");
            }
        } while (input.isEmpty());
        return input;
    }

    public static Long promptForLong(Scanner scanner, String prompt) {
        while (true) {
            log.info(prompt);
            try {
                return Long.parseLong(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                log.info("Invalid input. Please try again.");
            }
        }
    }

    public static Integer promptForInt(Scanner scanner, String prompt) {
        while (true) {
            log.info(prompt);
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                log.info("Invalid input. Please try again.");
            }
        }
    }

    public static TrainingType selectTrainingType(Scanner scanner) {
        while (true) {
            log.info("Select training type:");
            for (TrainingType type : TrainingType.values()) {
                log.info(type.ordinal() + 1 + " - " + type.name());
            }
            String choice = scanner.nextLine();

            try {
                return TrainingType.values()[Integer.parseInt(choice) - 1];
            } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                log.info("Invalid training type choice. Please try again.");
            }
        }
    }
}
