package com.epam.wca.gym.repository.impl;

import com.epam.wca.gym.entity.Training;
import com.epam.wca.gym.repository.TrainingDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Map;

@Component
public class TrainingDAOImpl implements TrainingDAO {
    private Map<Long, Training> trainingMap;
    private Long[] currentMaxId;

    @Autowired
    public void setTrainingMap(Map<Long, Training> trainingMap) {
        this.trainingMap = trainingMap;
    }

    @Autowired
    public void setCurrentMaxId(Long[] currentMaxId) {
        this.currentMaxId = currentMaxId;
    }

    @Override
    public Training save(Training training) {
        training.setTrainingId(++currentMaxId[0]);
        trainingMap.put(currentMaxId[0], training);

        return training;
    }

    @Override
    public void updateById(Long aLong, Training entity) {
        // TODO document why this method is empty
    }

    @Override
    public void deleteById(Long aLong) {
        // TODO document why this method is empty
    }

    @Override
    public Training findById(Long id) {
        if (trainingMap.containsKey(id)) {
            return trainingMap.get(id);
        }
        return null;
    }

    public Map<Long, Training> getAll() {
        return Collections.unmodifiableMap(trainingMap);
    }

    public long getCurrentMaxId() {
        return currentMaxId[0];
    }
}
