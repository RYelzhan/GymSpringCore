package com.epam.wca.gym.repository.database;

import com.epam.wca.gym.entity.Trainee;
import com.epam.wca.gym.entity.Trainer;
import com.epam.wca.gym.entity.Training;
import com.epam.wca.gym.entity.TrainingType;
import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

@Component
@Slf4j
public class DatabaseInitializationPostProcessor implements BeanPostProcessor {
    private Resource dataFileResource;
    @Autowired
    public void setDataFilePath(@Value("${data.file.path}") Resource dataFileResource) {
        this.dataFileResource = dataFileResource;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof InMemoryDatabase) {
            initializeData((InMemoryDatabase) bean);
        }
        return bean;
    }

    private void initializeData(InMemoryDatabase database) {
        log.info("Started Initialising In-Memory Database");
        CSVParser parser = new CSVParserBuilder()
                .withSeparator(';')
                .build();
        try (Reader reader = new InputStreamReader(dataFileResource.getInputStream());
             CSVReader csvReader = new CSVReaderBuilder(reader)
                     .withSkipLines(1)
                     .withCSVParser(parser)
                     .build()) {

            String[] parts;
            while ((parts = csvReader.readNext()) != null) {
                if (isMalformedOrEmpty(parts)) {
                    log.debug("Skipping malformed or empty line: " + String.join(",", parts));
                    continue;
                }

                String type = parts[0];
                long id = Long.parseLong(parts[1]);

                switch (type) {
                    case "Trainer":
                        processTrainer(parts, id, database.getTrainerStorage(), database.getUsernameStorage(), database.getUsernameToIdStorage());
                        break;
                    case "Trainee":
                        processTrainee(parts, id, database.getTraineeStorage(), database.getUsernameStorage(), database.getUsernameToIdStorage());
                        break;
                    case "Training":
                        processTraining(parts, id, database.getTrainingStorage());
                        break;
                    default:
                        System.err.println("Unknown type found in CSV: " + type);
                        break;
                }
            }
        } catch (IOException e) {
            log.error("Error reading data file: " + e.getMessage());
            e.printStackTrace();
        } catch (CsvValidationException | ParseException e) {
            log.error("Error parsing data file: " + e.getMessage());
            e.printStackTrace();
        }
        log.info("Finished Initialising In-Memory Database");
    }

    private boolean isMalformedOrEmpty(String[] parts) {
        return parts.length < 2 || parts[0].isEmpty();
    }

    private void updateUsernameStorage(String firstName, String lastName, String userName, Map<String, Integer> usernameStorage) {
        int lengthOfDefault = firstName.length() + lastName.length() + 1;
        if (lengthOfDefault == userName.length()) {
            // first person with such username
            usernameStorage.put(userName, 1);
        } else {
            // getting integers that were added to the end of default username
            usernameStorage.put(userName, Integer.parseInt(userName.substring(lengthOfDefault)));
        }
    }

    private void updateUsernameToIdStorage(String userName, Long id, Map<String, Long> usernameToIdStorage) {
        usernameToIdStorage.put(userName, id);
    }
    private void processTrainer(String[] parts, long id, Map<Long, Trainer> trainerStorage,
                                Map<String, Integer> usernameStorage, Map<String, Long> userToIdStorage) throws ParseException {
        if (parts.length < 8) {
            log.debug("Skipping malformed Trainer line: " + String.join(",", parts));
            return;
        }
        String firstName = parts[2];
        String lastName = parts[3];
        String userName = parts[4];
        String password = parts[5];
        boolean isActive = Boolean.parseBoolean(parts[6]);
        TrainingType specialization = TrainingType.valueOf(parts[7].toUpperCase());
        Trainer trainer = new Trainer(firstName, lastName, userName, password, isActive, specialization, id);
        trainerStorage.put(id, trainer);

        updateUsernameStorage(firstName, lastName, userName, usernameStorage);
        updateUsernameToIdStorage(userName, id, userToIdStorage);
    }

    private void processTrainee(String[] parts, long id, Map<Long, Trainee> traineeStorage,
                                Map<String, Integer> usernameStorage, Map<String, Long> userToIdStorage) throws ParseException {
        if (parts.length < 9) {
            log.debug("Skipping malformed Trainee line: " + String.join(",", parts));
            return;
        }
        String firstName = parts[2];
        String lastName = parts[3];
        String userName = parts[4];
        String password = parts[5];
        boolean isActive = Boolean.parseBoolean(parts[6]);
        Date dateOfBirth = new SimpleDateFormat("yyyy.MM.dd").parse(parts[7]);
        String address = parts[8];
        Trainee trainee = new Trainee(firstName, lastName, userName, password, isActive, dateOfBirth, address, id);
        traineeStorage.put(id, trainee);

        updateUsernameStorage(firstName, lastName, userName, usernameStorage);
        updateUsernameToIdStorage(userName, id, userToIdStorage);
    }

    private void processTraining(String[] parts, long id, Map<Long, Training> trainingStorage) throws ParseException {
        if (parts.length < 7) {
            log.debug("Skipping malformed Training line: " + String.join(",", parts));
            return;
        }
        long traineeId = Long.parseLong(parts[2]);
        long trainerId = Long.parseLong(parts[3]);
        String trainingName = parts[4];
        TrainingType trainingType = TrainingType.valueOf(parts[5].toUpperCase());
        Date trainingDate = new SimpleDateFormat("yyyy.MM.dd").parse(parts[6]);
        int trainingDuration = Integer.parseInt(parts[7]);
        Training training = new Training(id, traineeId, trainerId, trainingName, trainingType, trainingDate, trainingDuration);
        trainingStorage.put(id, training);
    }
}
