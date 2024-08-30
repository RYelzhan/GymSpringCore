package com.epam.wca.gym.repository.impl;

import com.epam.wca.gym.entity.Training;
import com.epam.wca.gym.repository.TrainingDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class TrainingDAOImpl implements TrainingDAO {
    private Map<Long, Training> trainingMap;
    private long currentMaxId = 0;
    @Autowired
    public void setTrainingMap(Map<Long, Training> trainingMap) {
        this.trainingMap = trainingMap;
        if (!trainingMap.isEmpty()) {
            currentMaxId = trainingMap.keySet().stream().max(Long::compare).orElse(0L);
        }
    }

    public void save(Training training) {
        training.setTrainingId(++ currentMaxId);
        trainingMap.put(currentMaxId, training);
    }

    public Training findById(long id) {
        return trainingMap.get(id);
    }
    public Map<Long, Training> getAll() {
        return trainingMap;
    }
}
