package com.epam.wca.gym.service;

import com.epam.wca.gym.dto.TraineeDTO;
import com.epam.wca.gym.entity.Trainee;

public interface TraineeService {
    void createTrainee(TraineeDTO trainee);
    void updateTraineeByUsername(String username, Trainee trainee);
    void deleteTraineeByUsername(String username);
    Trainee findTraineeByUsername(String username);
}
