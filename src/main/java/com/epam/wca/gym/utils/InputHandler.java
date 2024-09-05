package com.epam.wca.gym.utils;

import com.epam.wca.gym.entity.TrainingType;
import lombok.experimental.UtilityClass;

import java.util.Scanner;
@UtilityClass
public class InputHandler {
    public static String promptForInput(Scanner scanner, String prompt) {
        String input;
        do {
            System.out.println(prompt);
            input = scanner.nextLine().trim();
            if (input.isEmpty()) {
                System.out.println("Input cannot be empty. Please try again.");
            }
        } while (input.isEmpty());
        return input;
    }

    public static Long promptForLong(Scanner scanner, String prompt) {
        while (true) {
            System.out.println(prompt);
            try {
                return Long.parseLong(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please try again.");
            }
        }
    }

    public static Integer promptForInt(Scanner scanner, String prompt) {
        while (true) {
            System.out.println(prompt);
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please try again.");
            }
        }
    }

    public static TrainingType selectTrainingType(Scanner scanner) {
        while (true) {
            System.out.println("Select training type:");
            for (TrainingType type : TrainingType.values()) {
                System.out.println(type.ordinal() + 1 + " - " + type.name());
            }
            String choice = scanner.nextLine();

            try {
                return TrainingType.values()[Integer.parseInt(choice) - 1];
            } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                System.out.println("Invalid training type choice. Please try again.");
            }
        }
    }
}
