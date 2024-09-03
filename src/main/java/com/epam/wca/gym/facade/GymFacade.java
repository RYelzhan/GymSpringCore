package com.epam.wca.gym.facade;

import com.epam.wca.gym.dto.TraineeDTO;
import com.epam.wca.gym.dto.TrainerDTO;
import com.epam.wca.gym.dto.TrainingDTO;
import com.epam.wca.gym.entity.*;
import com.epam.wca.gym.service.TraineeService;
import com.epam.wca.gym.service.TrainerService;
import com.epam.wca.gym.service.TrainingService;
import com.epam.wca.gym.utils.AppConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
@Slf4j
@Component
public class GymFacade {

    private final TraineeService traineeService;
    private final TrainerService trainerService;
    private final TrainingService trainingService;
    private boolean loggedIn = false;
    private String username;
    private boolean appRunning;

    @Autowired
    public GymFacade(TraineeService traineeService, TrainerService trainerService, TrainingService trainingService) {
        this.traineeService = traineeService;
        this.trainerService = trainerService;
        this.trainingService = trainingService;
    }

    public void run() {
        try (Scanner scanner = new Scanner(System.in)) {
            log.info("Application Started Successfully!");

            appRunning = true;

            while (appRunning) {
                if (!loggedIn) {
                    handleLoggedOutState(scanner);
                } else {
                    handleLoggedInState(scanner);
                }
            }
        }
    }

    private void handleLoggedOutState(Scanner scanner) {
        System.out.println("Choose action:");
        System.out.println("l - login");
        System.out.println("r - register");
        System.out.println("q - quit");

        String choice = scanner.nextLine();

        switch (choice) {
            case "l" -> loggedIn = login(scanner);
            case "r" -> register(scanner);
            case "q" -> {
                System.out.println("Exiting...");
                appRunning = false;
            }
            default -> System.out.println("Invalid option, please try again.");
        }
    }

    private void handleLoggedInState(Scanner scanner) {
        System.out.println("Choose action:");
        System.out.println("c - create training");
        System.out.println("u - update user information");
        System.out.println("g - get user information");
        System.out.println("d - delete Trainee");
        System.out.println("f - find Training Info");
        System.out.println("l - log out");

        String choice = scanner.nextLine();

        switch (choice) {
            case "c" -> createTraining(scanner);
            case "u" -> updateUser(scanner);
            case "g" -> getUserInformation();
            case "d" -> delete();
            case "f" -> findTrainingInfo(scanner);
            case "l" -> loggedIn = false;
            default -> System.out.println("Invalid option, please try again.");
        }
    }

    private boolean login(Scanner scanner) {
        System.out.println("Enter username:");
        String username = scanner.nextLine();
        System.out.println("Enter password:");
        String password = scanner.nextLine();

        Trainee trainee = traineeService.findByUsername(username);
        Trainer trainer = trainerService.findByUsername(username);

        if (trainee != null && trainee.getPassword().equals(password) ||
                trainer != null && trainer.getPassword().equals(password)) {
            System.out.println("Successful login.");

            log.info("User authenticated: " + username);

            this.username = username;
            return true;
        }

        log.info("Unsuccessful authentication attempt!");

        System.out.println("Invalid user information. Please try again.");
        return false;
    }

    private void register(Scanner scanner) {
        System.out.println("Register as:");
        System.out.println("1 - Trainee");
        System.out.println("2 - Trainer");
        String choice = scanner.nextLine();

        switch (choice) {
            case "1" -> registerTrainee(scanner);
            case "2" -> registerTrainer(scanner);
            default -> System.out.println("Invalid choice, please try again.");
        }
    }

    private void registerTrainee(Scanner scanner) {
        String firstName = promptForInput(scanner, "Enter first name:");
        String lastName = promptForInput(scanner, "Enter last name:");
        Date dateOfBirth = parseDate(scanner, "Enter date of birth (" + AppConstants.DEFAULT_DATE_FORMAT + "):");
        String address = promptForInput(scanner, "Enter address:");

        if (dateOfBirth != null) {
            TraineeDTO traineeDTO = new TraineeDTO(firstName, lastName, dateOfBirth, address);
            Trainee newTrainee = traineeService.create(traineeDTO);
            printUserDetails(newTrainee);
        }
    }

    private void registerTrainer(Scanner scanner) {
        String firstName = promptForInput(scanner, "Enter first name:");
        String lastName = promptForInput(scanner, "Enter last name:");
        TrainingType trainingType = selectTrainingType(scanner);

        if (trainingType != null) {
            TrainerDTO trainerDTO = new TrainerDTO(firstName, lastName, trainingType);
            Trainer newTrainer = trainerService.create(trainerDTO);
            printUserDetails(newTrainer);
        }
    }

    private String promptForInput(Scanner scanner, String prompt) {
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

    private Date parseDate(Scanner scanner, String prompt) {
        System.out.println(prompt);
        String dobInput = scanner.nextLine();
        try {
            return new SimpleDateFormat(AppConstants.DEFAULT_DATE_FORMAT).parse(dobInput);
        } catch (ParseException e) {
            System.out.println("Invalid date format. Please try again.");
            return null;
        }
    }

    private TrainingType selectTrainingType(Scanner scanner) {
        System.out.println("Select training type:");
        for (TrainingType type : TrainingType.values()) {
            System.out.println(type.ordinal() + 1 + " - " + type.name());
        }
        String trainingTypeChoice = scanner.nextLine();

        try {
            int trainingTypeIndex = Integer.parseInt(trainingTypeChoice) - 1;
            return TrainingType.values()[trainingTypeIndex];
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            System.out.println("Invalid training type choice. Please try again.");
            return null;
        }
    }

    private void printUserDetails(User newUser) {
        System.out.println("User registered successfully!");
        System.out.println("Username: " + newUser.getUserName());
        System.out.println("Password: " + newUser.getPassword());
        log.info("New User Registered: " + newUser.getUserName());
    }

    private void createTraining(Scanner scanner) {
        System.out.println("Creating a new training session...");

        // Gather necessary inputs
        String trainingName = promptForInput(scanner, "Enter training name:");
        TrainingType trainingType = selectTrainingType(scanner);
        Long traineeId = promptForLong(scanner, "Enter trainee ID:");
        Long trainerId = promptForLong(scanner, "Enter trainer ID:");
        Date trainingDate = parseDate(scanner, "Enter training date (" + AppConstants.DEFAULT_DATE_FORMAT + "):");
        Integer trainingDuration = promptForInt(scanner, "Enter training duration (in minutes):");

        if (trainingName == null || trainingType == null || traineeId == null || trainerId == null || trainingDate == null || trainingDuration == null) {
            System.out.println("Invalid input. Training session creation aborted.");
            return;
        }

        try {
            // Create a new Training object
            TrainingDTO trainingDTO = new TrainingDTO(traineeId, trainerId, trainingName, trainingType, trainingDate, trainingDuration);

            // Save the training session
            Training newTraining = trainingService.createTraining(trainingDTO);

            System.out.println("Training session created successfully!");
            System.out.println("Training Id: " + newTraining.getTrainingId());
        } catch (Exception e) {
            System.out.println("An error occurred while creating the training session: " + e.getMessage());
        }
    }

    private Long promptForLong(Scanner scanner, String prompt) {
        System.out.println(prompt);
        String input = scanner.nextLine().trim();
        try {
            return Long.parseLong(input);
        } catch (NumberFormatException e) {
            System.out.println("Invalid number format. Please try again.");
            return null;
        }
    }

    private Integer promptForInt(Scanner scanner, String prompt) {
        System.out.println(prompt);
        String input = scanner.nextLine().trim();
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            System.out.println("Invalid number format. Please try again.");
            return null;
        }
    }

    private void updateUser(Scanner scanner) {
        Trainee trainee = traineeService.findByUsername(username);
        Trainer trainer = trainerService.findByUsername(username);

        if (trainee != null) {
            updateTrainee(scanner, trainee);
        } else if (trainer != null) {
            updateTrainer(scanner, trainer);
        } else {
            System.out.println("No user found with the provided username.");
        }
    }

    private void updateTrainee(Scanner scanner, Trainee trainee) {
        updateDateOfBirth(scanner, trainee);
        updateAddress(scanner, trainee);

        traineeService.updateByUsername(username, trainee);
        System.out.println("Trainee information updated successfully!");
    }

    private void updateTrainer(Scanner scanner, Trainer trainer) {
        updateSpecialization(scanner, trainer);

        trainerService.updateByUsername(username, trainer);
        System.out.println("Trainer information updated successfully!");
    }

    private void updateDateOfBirth(Scanner scanner, Trainee trainee) {
        System.out.println("Update date of birth (current: " + trainee.getDateOfBirth() + ") or press enter to keep it (format: " + AppConstants.DEFAULT_DATE_FORMAT + "):");
        String dobInput = scanner.nextLine();
        if (!dobInput.trim().isEmpty()) {
            try {
                Date dateOfBirth = new SimpleDateFormat(AppConstants.DEFAULT_DATE_FORMAT).parse(dobInput);
                trainee.setDateOfBirth(dateOfBirth);
            } catch (IllegalArgumentException | ParseException e) {
                System.out.println("Invalid date format. Date of birth not updated.");
            }
        }
    }

    private void updateAddress(Scanner scanner, Trainee trainee) {
        System.out.println("Update address (current: " + trainee.getAddress() + ") or press enter to keep it:");
        String address = scanner.nextLine();
        if (!address.trim().isEmpty()) {
            trainee.setAddress(address);
        }
    }

    private void updateSpecialization(Scanner scanner, Trainer trainer) {
        System.out.println("Update specialization (current: " + trainer.getSpecialization() + ") or press enter to keep it:");
        for (TrainingType type : TrainingType.values()) {
            System.out.println(type.ordinal() + 1 + " - " + type.name());
        }
        String specializationInput = scanner.nextLine();
        if (!specializationInput.trim().isEmpty()) {
            try {
                int trainingTypeIndex = Integer.parseInt(specializationInput) - 1;
                TrainingType specialization = TrainingType.values()[trainingTypeIndex];
                trainer.setSpecialization(specialization);
            } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                System.out.println("Invalid specialization choice. Specialization not updated.");
            }
        }
    }

    private void getUserInformation() {
        Trainee trainee = traineeService.findByUsername(username);
        Trainer trainer = trainerService.findByUsername(username);

        if (trainee != null) {
            displayTraineeInformation(trainee);
        } else if (trainer != null) {
            displayTrainerInformation(trainer);
        } else {
            System.out.println("No user found with the provided username.");
        }
    }

    private void displayTraineeInformation(Trainee trainee) {
        System.out.println("Trainee Information:");
        System.out.println("Id: " + trainee.getUserId());
        System.out.println("Username: " + trainee.getUserName());
        System.out.println("First Name: " + trainee.getFirstName());
        System.out.println("Last Name: " + trainee.getLastName());
        System.out.println("Date of Birth: " + trainee.getDateOfBirth());
        System.out.println("Address: " + trainee.getAddress());
    }

    private void displayTrainerInformation(Trainer trainer) {
        System.out.println("Trainer Information:");
        System.out.println("Id: " + trainer.getUserId());
        System.out.println("Username: " + trainer.getUserName());
        System.out.println("First Name: " + trainer.getFirstName());
        System.out.println("Last Name: " + trainer.getLastName());
        System.out.println("Specialization: " + trainer.getSpecialization());
    }

    private void delete() {
        Trainee trainee = traineeService.findByUsername(username);
        if (trainee != null) {
            traineeService.deleteByUsername(username);
            loggedIn = false;
        } else {
            System.out.println("You are not Trainee.");
        }
    }

    private void findTrainingInfo(Scanner scanner) {
        System.out.println("Enter Training Id: ");
        String trainingIdInput = scanner.nextLine();
        try {
            long trainingId = Long.parseLong(trainingIdInput);
            Training training = trainingService.findById(trainingId);

            if (training != null) {
                System.out.println("Training found:");
                System.out.println(training);
            } else {
                System.out.println("Training with ID " + trainingId + " not found.");
            }

        } catch (NumberFormatException e) {
            System.out.println("Invalid Training ID format. Please enter a numeric value.");
        }
    }
}
