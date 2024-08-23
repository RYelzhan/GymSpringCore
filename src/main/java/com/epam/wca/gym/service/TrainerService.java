package com.epam.wca.gym.service;

import com.epam.wca.gym.entity.Trainer;

public interface TrainerService {
    void createTrainer(Trainer trainer);
    void updateTrainerById(long id, Trainer trainer);
    Trainer findTrainerById(long id);
}
