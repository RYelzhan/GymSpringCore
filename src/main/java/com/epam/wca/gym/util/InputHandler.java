package com.epam.wca.gym.util;

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
                log.info("Invalid number. Please try again.");
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

    public static boolean promptForBoolean(Scanner scanner) {
        String prompt = "Change activity: (True|False)";
        while (true) {
            log.info(prompt);
            String input = scanner.nextLine().trim();
            if (input.equals("True")) {
                return true;
            } else if (input.equals("False")) {
                return false;
            } else {
                log.info("Invalid active type. Please try again.");
            }
        }
    }

    public static String promptForPassword(Scanner scanner) {
        String prompt = "Enter new Password: ";
        while (true) {
            log.info(prompt);
            String input = scanner.nextLine().trim();
            if (input.length() > 9) {
                return input;
            } else {
                log.info("Invalid input. Please try again.");
            }
        }
    }
}
