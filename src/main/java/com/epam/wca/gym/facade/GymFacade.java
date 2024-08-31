package com.epam.wca.gym.facade;

import com.epam.wca.gym.dto.TraineeDTO;
import com.epam.wca.gym.dto.TrainerDTO;
import com.epam.wca.gym.dto.TrainingDTO;
import com.epam.wca.gym.entity.Trainee;
import com.epam.wca.gym.entity.Trainer;
import com.epam.wca.gym.entity.Training;
import com.epam.wca.gym.entity.TrainingType;
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

    @Autowired
    public GymFacade(TraineeService traineeService, TrainerService trainerService, TrainingService trainingService) {
        this.traineeService = traineeService;
        this.trainerService = trainerService;
        this.trainingService = trainingService;
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);
        log.info("Application Started Successfully!");
        while (true) {
            System.out.println("Choose action:");
            if (!loggedIn) {
                System.out.println("l - login");
                System.out.println("r - register");
                System.out.println("q - quit");

                String choice = scanner.nextLine();

                switch (choice) {
                    case "l" -> loggedIn = login(scanner);
                    case "r" -> register(scanner);
                    case "q" -> {
                        System.out.println("Exiting...");
                        return;
                    }
                    default -> System.out.println("Invalid option, please try again.");
                }
            } else {
                System.out.println("c - create training");
                System.out.println("u - update user information");
                System.out.println("d - delete Trainee");
                System.out.println("f - find Training Info");
                System.out.println("l - log out");

                String choice = scanner.nextLine();

                switch (choice) {
                    case "c" -> createTraining(scanner);
                    case "u" -> updateUser(scanner);
                    case "d" -> delete();
                    case "f" -> findTrainingInfo(scanner);
                    case "l" -> loggedIn = false;
                    default -> System.out.println("Invalid option, please try again.");
                }
            }
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

        if ("1".equals(choice)) {
            System.out.println("Enter first name:");
            String firstName = scanner.nextLine();
            System.out.println("Enter last name:");
            String lastName = scanner.nextLine();
            System.out.println("Enter date of birth (" + AppConstants.DEFAULT_DATE_FORMAT + "):");
            String dobInput = scanner.nextLine();
            System.out.println("Enter address:");
            String address = scanner.nextLine();
            try {
                Date dateOfBirth = new SimpleDateFormat(AppConstants.DEFAULT_DATE_FORMAT).parse(dobInput); // Parsing date
                TraineeDTO traineeDTO = new TraineeDTO(firstName, lastName, dateOfBirth, address);
                Trainee newTrainee = traineeService.create(traineeDTO);

                System.out.println("Trainee registered successfully!");
                System.out.println("Username: " + newTrainee.getUserName());
                System.out.println("Password: " + newTrainee.getPassword());

                log.info("New User Registered: " + newTrainee.getUserName());
            } catch (IllegalArgumentException | ParseException e) {
                System.out.println("Invalid date format. Please try again.");
            }

        } else if ("2".equals(choice)) {
            System.out.println("Enter first name:");
            String firstName = scanner.nextLine();
            System.out.println("Enter last name:");
            String lastName = scanner.nextLine();
            System.out.println("Select training type:");
            for (TrainingType type : TrainingType.values()) {
                System.out.println(type.ordinal() + 1 + " - " + type.name());
            }
            String trainingTypeChoice = scanner.nextLine();

            try {
                int trainingTypeIndex = Integer.parseInt(trainingTypeChoice) - 1;
                TrainingType trainingType = TrainingType.values()[trainingTypeIndex];
                TrainerDTO trainerDTO = new TrainerDTO(firstName, lastName, trainingType);
                Trainer newTrainer = trainerService.create(trainerDTO);

                System.out.println("Trainer registered successfully!");
                System.out.println("Username: " + newTrainer.getUserName());
                System.out.println("Password: " + newTrainer.getPassword());

                log.info("New User Registered: " + newTrainer.getUserName());
            } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                System.out.println("Invalid training type choice. Please try again.");
            }

        } else {
            System.out.println("Invalid choice, please try again.");
        }
    }

    private void createTraining(Scanner scanner) {
        System.out.println("Creating a new training session...");

        // Gather necessary inputs
        System.out.println("Enter training name:");
        String trainingName = scanner.nextLine();

        System.out.println("Select training type:");
        for (TrainingType type : TrainingType.values()) {
            System.out.println(type.ordinal() + 1 + " - " + type.name());
        }
        String trainingTypeChoice = scanner.nextLine();

        System.out.println("Enter trainee ID:");
        String traineeIdInput = scanner.nextLine();

        System.out.println("Enter trainer ID:");
        String trainerIdInput = scanner.nextLine();

        System.out.println("Enter training date (" + AppConstants.DEFAULT_DATE_FORMAT + "):");
        String trainingDateInput = scanner.nextLine();

        System.out.println("Enter training duration (in minutes):");
        String trainingDurationInput = scanner.nextLine();

        try {
            // Parse and validate inputs
            long traineeId = Long.parseLong(traineeIdInput);
            long trainerId = Long.parseLong(trainerIdInput);
            int trainingDuration = Integer.parseInt(trainingDurationInput);
            Date trainingDate = new SimpleDateFormat(AppConstants.DEFAULT_DATE_FORMAT).parse(trainingDateInput); // Parsing date

            int trainingTypeIndex = Integer.parseInt(trainingTypeChoice) - 1;
            TrainingType trainingType = TrainingType.values()[trainingTypeIndex];

            // Create a new Training object
            TrainingDTO trainingDTO = new TrainingDTO(traineeId, trainerId, trainingName, trainingType, trainingDate, trainingDuration);

            // Save the training session
            Training newTraining = trainingService.createTraining(trainingDTO);

            System.out.println("Training session created successfully!");
            System.out.println("Training Id: " + newTraining.getTrainingId());
        } catch (ArrayIndexOutOfBoundsException | IllegalArgumentException e) {
            System.out.println("Invalid input. Please try again.");
        } catch (Exception e) {
            System.out.println("An error occurred while creating the training session: " + e.getMessage());
        }
    }
    private void updateUser(Scanner scanner) {
        Trainee trainee = traineeService.findByUsername(username);
        Trainer trainer = trainerService.findByUsername(username);

        if (trainee != null) {
            System.out.println("Update date of birth (current: " + trainee.getDateOfBirth() + ") or press enter to keep it:");
            String dobInput = scanner.nextLine();
            if (!dobInput.trim().isEmpty()) {
                try {
                    Date dateOfBirth = new SimpleDateFormat(AppConstants.DEFAULT_DATE_FORMAT).parse(dobInput);
                    trainee.setDateOfBirth(dateOfBirth);
                } catch (IllegalArgumentException | ParseException e) {
                    System.out.println("Invalid date format. Date of birth not updated.");
                }
            }

            System.out.println("Update address (current: " + trainee.getAddress() + ") or press enter to keep it:");
            String address = scanner.nextLine();
            if (!address.trim().isEmpty()) {
                trainee.setAddress(address);
            }

            traineeService.updateByUsername(username, trainee);
            System.out.println("Trainee information updated successfully!");
        } else {
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

            trainerService.updateByUsername(username, trainer);
            System.out.println("Trainer information updated successfully!");
        }
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
