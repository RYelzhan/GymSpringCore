package com.epam.wca.gym.repository.impl;

import com.epam.wca.gym.entity.Trainee;
import com.epam.wca.gym.repository.TraineeDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public class TraineeDAOImpl implements TraineeDAO {
    private Map<Long, Trainee> traineeMap;
    @Autowired
    public void setTraineeMap(Map<Long, Trainee> traineeMap) {
        this.traineeMap = traineeMap;
    }

    public void save(Trainee trainee) {

    }

    public void updateById(long id, Trainee trainee) {

    }

    public void deleteById(long id) {

    }

    public Trainee findById(long id) {
        return null;
    }
}
