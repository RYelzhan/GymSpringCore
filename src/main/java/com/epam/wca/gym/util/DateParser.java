package com.epam.wca.gym.util;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
@Slf4j
@UtilityClass
public class DateParser {

    public static LocalDate parseDate(Scanner scanner, String prompt) {
        while (true) {
            log.info(prompt);
            String input = scanner.nextLine().trim();
            try {
                return LocalDate.parse(input,
                        DateTimeFormatter.ofPattern(AppConstants.DEFAULT_DATE_FORMAT));
            } catch (Exception e) {
                log.info("Invalid Date format. Please try again.");
            }
        }
    }
}
