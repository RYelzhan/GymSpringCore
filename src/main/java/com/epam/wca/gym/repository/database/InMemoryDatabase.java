package com.epam.wca.gym.repository.database;

import com.epam.wca.gym.entity.Trainee;
import com.epam.wca.gym.entity.Trainer;
import com.epam.wca.gym.entity.Training;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.util.Map;
@Repository
public class InMemoryDatabase {
    private Map<Long, Trainee> traineeStorage;
    private Map<Long, Trainer> trainerStorage;
    private Map<Long, Training> trainingStorage;
    private String dataFilePath;
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
    public void setDataFilePath(@Value("${data.file.path}") String dataFilePath) {
        this.dataFilePath = dataFilePath;
    }

    /*
        Implement the ability to
        initialize storage with some prepared data from the file during the application start (use
        spring bean post-processing features). Path to the concrete file should be set using
        property placeholder and external property file. In other words, Every storage
        (java.util.Map) should be implemented as a separate spring bean
         */
    public Map<Long, Trainee> getTraineeStorage() {
        return traineeStorage;
    }

    public Map<Long, Trainer> getTrainerStorage() {
        return trainerStorage;
    }

    public Map<Long, Training> getTrainingStorage() {
        return trainingStorage;
    }
}
