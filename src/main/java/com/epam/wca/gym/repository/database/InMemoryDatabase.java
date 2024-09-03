package com.epam.wca.gym.repository.database;

import com.epam.wca.gym.entity.Trainee;
import com.epam.wca.gym.entity.Trainer;
import com.epam.wca.gym.entity.Training;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
@Getter
@Component
public class InMemoryDatabase {
    private Map<Long, Trainee> traineeStorage;
    private Map<Long, Trainer> trainerStorage;
    private Map<Long, Training> trainingStorage;
    private Map<String, Integer> usernameStorage;
    private Map<String, Long> usernameToIdStorage;
    @Autowired
    public void setTraineeStorage(Map<Long, Trainee> traineeStorage) {
        this.traineeStorage = traineeStorage;
    }
    @Autowired
    public void setTrainerStorage(Map<Long, Trainer> trainerStorage) {
        this.trainerStorage = trainerStorage;
    }
    @Autowired
    public void setTrainingStorage(Map<Long, Training> trainingStorage) {
        this.trainingStorage = trainingStorage;
    }
    @Autowired
    public void setUsernameStorage(Map<String, Integer> usernameStorage) {
        this.usernameStorage = usernameStorage;
    }
    @Autowired
    public void setUsernameToIdStorage(Map<String, Long> usernameToIdStorage) {
        this.usernameToIdStorage = usernameToIdStorage;
    }

    /*
    Later if I will have motivation
    @PreDestroy
    public void updateData() {
        System.out.println("Entered Pre Destroy Method");
        try (Writer writer = new OutputStreamWriter(Files.newOutputStream(dataFileResource.getFile().toPath()));
             CSVWriter csvWriter = new CSVWriter(writer, ';', CSVWriter.NO_QUOTE_CHARACTER,
                     CSVWriter.DEFAULT_ESCAPE_CHARACTER, CSVWriter.DEFAULT_LINE_END)) {

            // Write header if needed
            csvWriter.writeNext(new String[]{"type", "id", "firstName", "lastName", "userName", "password", "isActive", "specialization/dateOfBirth", "address"});

            // Write all Trainer data
            for (Trainer trainer : trainerStorage.values()) {
                String[] trainerData = {
                        "Trainer",
                        String.valueOf(trainer.getUserId()),
                        trainer.getFirstName(),
                        trainer.getLastName(),
                        trainer.getUserName(),
                        trainer.getPassword(),
                        String.valueOf(trainer.isActive()),
                        trainer.getSpecialization().name()
                };
                csvWriter.writeNext(trainerData);
            }

            // Write all Trainee data
            for (Trainee trainee : traineeStorage.values()) {
                String[] traineeData = {
                        "Trainee",
                        String.valueOf(trainee.getUserId()),
                        trainee.getFirstName(),
                        trainee.getLastName(),
                        trainee.getUserName(),
                        trainee.getPassword(),
                        String.valueOf(trainee.isActive()),
                        new SimpleDateFormat("yyyy-MM-dd").format(trainee.getDateOfBirth()),
                        trainee.getAddress()
                };
                csvWriter.writeNext(traineeData);
            }

            // Write all Training data
            for (Training training : trainingStorage.values()) {
                String[] trainingData = {
                        "Training",
                        String.valueOf(training.getTrainingId()),
                        String.valueOf(training.getTraineeId()),
                        String.valueOf(training.getTrainerId()),
                        training.getTrainingName(),
                        training.getTrainingType().name(),
                        new SimpleDateFormat("yyyy-MM-dd").format(training.getTrainingDate()),
                        String.valueOf(training.getTrainingDuration())
                };
                csvWriter.writeNext(trainingData);
            }

        } catch (IOException e) {
            System.err.println("Error updating data file: " + e.getMessage());
            e.printStackTrace();
        }
    }
*/
}
