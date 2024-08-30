package com.epam.wca.gym.service;

import com.epam.wca.gym.dto.TrainerDTO;
import com.epam.wca.gym.entity.Trainer;

public interface TrainerService {
    void createTrainer(TrainerDTO trainer);
    void updateTrainerByUsername(String username, Trainer trainer);
    Trainer findByUsername(String username);
}
