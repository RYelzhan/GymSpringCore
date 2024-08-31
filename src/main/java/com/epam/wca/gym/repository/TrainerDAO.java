package com.epam.wca.gym.repository;

import com.epam.wca.gym.entity.Trainer;

public interface TrainerDAO {
    Trainer save(Trainer trainer);
    void updateByUsername(String username, Trainer trainer);
    Trainer findByUsername(String username);
    Trainer findById(long id);
}
