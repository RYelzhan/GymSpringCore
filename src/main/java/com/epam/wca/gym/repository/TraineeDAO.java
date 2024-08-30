package com.epam.wca.gym.repository;

import com.epam.wca.gym.entity.Trainee;

public interface TraineeDAO {
    void save(Trainee trainee);
    void updateByUsername(String username, Trainee trainee);
    void deleteByUsername(String username);
    Trainee findByUsername(String username);
}
