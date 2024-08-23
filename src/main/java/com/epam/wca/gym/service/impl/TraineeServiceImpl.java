package com.epam.wca.gym.service.impl;

import com.epam.wca.gym.entity.Trainee;
import com.epam.wca.gym.repository.TraineeDAO;
import com.epam.wca.gym.repository.database.InMemoryDatabase;
import com.epam.wca.gym.service.TraineeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TraineeServiceImpl implements TraineeService {
    @Autowired
    private TraineeDAO traineeDAO;
    @Autowired
    private InMemoryDatabase inMemoryDatabase;

    public Trainee createTrainee(Trainee trainee) {
        return null;
    }

    public void updateTraineeById(long traineeId, Trainee trainee) {

    }

    public void deleteTraineeById(long traineeId) {

    }

    public Trainee findTraineeById(long traineeId) {
        return null;
    }
}
