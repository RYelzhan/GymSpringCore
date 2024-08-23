package com.epam.wca.gym.repository;

import com.epam.wca.gym.entity.Trainer;

public interface TrainerDAO {
    void save(Trainer trainer);
    void updateById(long id, Trainer trainer);
    Trainer findById(long id);
}
