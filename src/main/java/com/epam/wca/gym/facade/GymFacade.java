package com.epam.wca.gym.facade;

import com.epam.wca.gym.dto.TraineeDTO;
import com.epam.wca.gym.dto.TrainerDTO;
import com.epam.wca.gym.dto.TrainingDTO;
import com.epam.wca.gym.entity.Trainee;
import com.epam.wca.gym.entity.Trainer;
import com.epam.wca.gym.entity.TrainingType;
import com.epam.wca.gym.service.TraineeService;
import com.epam.wca.gym.service.TrainerService;
import com.epam.wca.gym.service.TrainingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Scanner;
@Component
public class GymFacade {

    private final TraineeService traineeService;
    private final TrainerService trainerService;
    private final TrainingService trainingService;
    @Autowired
    public GymFacade(TraineeService traineeService, TrainerService trainerService, TrainingService trainingService) {
        this.traineeService = traineeService;
        this.trainerService = trainerService;
        this.trainingService = trainingService;
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);
        boolean loggedIn = false;
        while (true) {
            System.out.println("Choose action:");
            if (!loggedIn) {
                System.out.println("l - login");
                System.out.println("r - register");
                System.out.println("q - quit");

                String choice = scanner.nextLine();

                switch (choice) {
                    case "l":
                        loggedIn = login(scanner);
                        break;
                    case "r":
                        register(scanner);
                        break;
                    case "q":
                        System.out.println("Exiting...");
                        return;
                    default:
                        System.out.println("Invalid option, please try again.");
                }
            } else {
                System.out.println("c - create training");

                String choice = scanner.nextLine();

                switch (choice) {
                    case "c":
                        createTraining(scanner);
                        break;
                    default:
                        System.out.println("Invalid option, please try again.");
                }
            }
        }
    }

    private boolean login(Scanner scanner) {
        System.out.println("Enter username:");
        String username = scanner.nextLine();
        System.out.println("Enter password:");
        String password = scanner.nextLine();

        Trainee trainee = traineeService.findTraineeByUsername(username);
        Trainer trainer = trainerService.findByUsername(username);

        if (trainee != null && trainee.getPassword().equals(password)) {
            System.out.println("Successful login.");
            return true;
        } else if (trainer != null && trainer.getPassword().equals(password)) {
            System.out.println("Successful login.");
            return true;
        }
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
            System.out.println("Enter date of birth (yyyy-mm-dd):");
            String dobInput = scanner.nextLine();
            System.out.println("Enter address:");
            String address = scanner.nextLine();

            try {
                Date dateOfBirth = java.sql.Date.valueOf(dobInput); // Parsing date
                TraineeDTO traineeDTO = new TraineeDTO(firstName, lastName, dateOfBirth, address);
                traineeService.createTrainee(traineeDTO);
                System.out.println("Trainee registered successfully!");
            } catch (IllegalArgumentException e) {
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
                trainerService.createTrainer(trainerDTO);
                System.out.println("Trainer registered successfully!");
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

        System.out.println("Enter training date (yyyy.mm.dd):");
        String trainingDateInput = scanner.nextLine();

        System.out.println("Enter training duration (in minutes):");
        String trainingDurationInput = scanner.nextLine();

        try {
            // Parse and validate inputs
            long traineeId = Long.parseLong(traineeIdInput);
            long trainerId = Long.parseLong(trainerIdInput);
            int trainingDuration = Integer.parseInt(trainingDurationInput);
            Date trainingDate = java.sql.Date.valueOf(trainingDateInput); // Parsing date

            int trainingTypeIndex = Integer.parseInt(trainingTypeChoice) - 1;
            TrainingType trainingType = TrainingType.values()[trainingTypeIndex];

            // Create a new Training object
            TrainingDTO trainingDTO = new TrainingDTO(traineeId, trainerId, trainingName, trainingType, trainingDate, trainingDuration);

            // Save the training session
            trainingService.createTraining(trainingDTO);
            System.out.println("Training session created successfully!");

        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            System.out.println("Invalid input. Please try again.");
        } catch (Exception e) {
            System.out.println("An error occurred while creating the training session: " + e.getMessage());
        }
    }

}
