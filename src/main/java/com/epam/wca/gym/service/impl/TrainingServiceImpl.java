package com.epam.wca.gym.service.impl;

import com.epam.wca.gym.entity.Training;
import com.epam.wca.gym.repository.TrainingDAO;
import com.epam.wca.gym.repository.database.InMemoryDatabase;
import com.epam.wca.gym.service.TrainingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TrainingServiceImpl implements TrainingService {
    @Autowired
    private TrainingDAO trainingDAO;
    @Autowired
    private InMemoryDatabase inMemoryDatabase;

    public void createTraining(Training training) {
        trainingDAO.save(training);
    }

    public Training findTrainingById(long id) {
        return trainingDAO.findById(id);
    }
}
