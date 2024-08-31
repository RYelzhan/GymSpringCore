package com.epam.wca.gym.service;

import com.epam.wca.gym.dto.TraineeDTO;
import com.epam.wca.gym.entity.Trainee;

public interface TraineeService {
    Trainee create(TraineeDTO trainee);
    void updateByUsername(String username, Trainee trainee);
    void deleteByUsername(String username);
    Trainee findByUsername(String username);
    Trainee findById(long id);
}
