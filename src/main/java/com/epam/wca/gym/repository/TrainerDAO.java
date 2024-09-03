package com.epam.wca.gym.repository;

import com.epam.wca.gym.entity.Trainer;

public interface TrainerDAO extends GenericDAO<Trainer, Long> {
    void updateByUsername(String username, Trainer trainer);
    Trainer findByUsername(String username);
}
