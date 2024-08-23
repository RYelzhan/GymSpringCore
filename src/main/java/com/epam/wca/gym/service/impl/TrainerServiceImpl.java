package com.epam.wca.gym.service.impl;

import com.epam.wca.gym.entity.Trainer;
import com.epam.wca.gym.repository.TrainerDAO;
import com.epam.wca.gym.repository.database.InMemoryDatabase;
import com.epam.wca.gym.service.TrainerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TrainerServiceImpl implements TrainerService {
    @Autowired
    private TrainerDAO trainerMap;
    @Autowired
    private InMemoryDatabase inMemoryDatabase;
    public void createTrainer(Trainer trainer) {

    }

    public void updateTrainerById(long id, Trainer trainer) {

    }

    public Trainer findTrainerById(long id) {
        return null;
    }
}
