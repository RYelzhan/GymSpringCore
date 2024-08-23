package com.epam.wca.gym.repository;

import com.epam.wca.gym.entity.Trainee;

public interface TraineeDAO {
    void save(Trainee trainee);
    void updateById(long id, Trainee trainee);
    void deleteById(long id);
    Trainee findById(long id);
}
