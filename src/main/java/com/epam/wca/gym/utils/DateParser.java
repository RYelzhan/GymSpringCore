package com.epam.wca.gym.utils;

import lombok.experimental.UtilityClass;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
@UtilityClass
public class DateParser {

    public static LocalDate parseDate(Scanner scanner, String prompt) {
        while (true){
            System.out.println(prompt);
            String input = scanner.nextLine().trim();
            try {
                return LocalDate.parse(input,
                        DateTimeFormatter.ofPattern(AppConstants.DEFAULT_DATE_FORMAT));
            } catch (Exception e) {
                System.out.println("Invalid Date format. Please try again.");
            }
        }
    }
}
