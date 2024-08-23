package com.epam.wca.gym.repository.impl;

import com.epam.wca.gym.entity.Training;
import com.epam.wca.gym.repository.TrainingDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public class TrainingDAOImpl implements TrainingDAO {
    private Map<Long, Training> trainingMap;
    @Autowired
    public void setTrainingMap(Map<Long, Training> trainingMap) {
        this.trainingMap = trainingMap;
    }

    public void save(Training training) {

    }

    public Training findById(long id) {
        return null;
    }
}
