package com.epam.wca.gym.repository;

import com.epam.wca.gym.entity.Training;

public interface TrainingDAO {
    void save(Training training);
    Training findById(long id);
}
