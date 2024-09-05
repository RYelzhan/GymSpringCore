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
}
