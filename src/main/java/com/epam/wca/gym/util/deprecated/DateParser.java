package com.epam.wca.gym.util.deprecated;

import com.epam.wca.gym.util.AppConstants;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

/**
 * @deprecated This class is deprecated. It was used in console version of application.
 */

@Deprecated(since = "2.0")

@Slf4j
@UtilityClass
public class DateParser {

    public static ZonedDateTime parseDate(Scanner scanner, String prompt) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss z");
        while (true) {
            log.info(prompt);
            String input = scanner.nextLine().trim();
            try {
                ZonedDateTime dateInput = ZonedDateTime.parse(input + " " + ZoneId.systemDefault(),
                        DateTimeFormatter.ofPattern(AppConstants.DEFAULT_DATE_FORMAT));

                dateInput.format(formatter);

                return dateInput;
            } catch (Exception e) {
                log.info("Invalid Date format. Please use the correct format: dd-MM-yyyy HH:mm:ss");
            }
        }
    }
}
