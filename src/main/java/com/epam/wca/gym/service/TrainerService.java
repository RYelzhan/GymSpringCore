package com.epam.wca.gym.service;

import com.epam.wca.gym.dto.TrainerDTO;
import com.epam.wca.gym.entity.Trainer;

public interface TrainerService {
    Trainer create(TrainerDTO trainer);

    void updateByUsername(String username, Trainer trainer);

    Trainer findByUsername(String username);

    Trainer findById(long id);
}
