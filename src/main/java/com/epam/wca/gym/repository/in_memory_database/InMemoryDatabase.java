package com.epam.wca.gym.repository.in_memory_database;

import com.epam.wca.gym.entity.Trainee;
import com.epam.wca.gym.entity.Trainer;
import com.epam.wca.gym.entity.Training;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

/**
 * A simple in-memory storage class for managing data related to trainees, trainers, and trainings.
 * This class uses several Maps to store entity data and user-related mappings (username and ID).
 *
 * <p>This class provides storage for:
 * <ul>
 *   <li>{@link Trainee} entities mapped by their unique IDs.</li>
 *   <li>{@link Trainer} entities mapped by their unique IDs.</li>
 *   <li>{@link Training} entities mapped by their unique IDs.</li>
 *   <li>Username uniqueness tracking via {@code usernameStorage}, mapping usernames to a count
 *   to handle duplicates.</li>
 *   <li>Mapping usernames to their respective user IDs via {@code usernameToIdStorage}.</li>
 * </ul>
 *
 * <p>The class relies on Spring’s {@link Autowired} annotations to inject the Maps used for storing
 * these entities. This class is typically used in conjunction with a CSV file for initialization
 * via {@link DatabaseInitializationPostProcessor}.
 *
 * <p><strong>Note:</strong> This class is deprecated and will be removed in future versions as the project
 * transitions to a relational database with JPA/Hibernate.</p>
 *
 * @deprecated Since version 1.1. Please use JPA repositories for database access.
 * @see DatabaseInitializationPostProcessor
 */

@Deprecated(since = "1.1", forRemoval = false)

@Getter
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
}
