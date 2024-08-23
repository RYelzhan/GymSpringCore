package com.epam.wca.gym.service;

import com.epam.wca.gym.entity.Trainee;

public interface TraineeService {
    Trainee createTrainee(Trainee trainee);
    void updateTraineeById(long traineeId, Trainee trainee);
    void deleteTraineeById(long traineeId);
    Trainee findTraineeById(long traineeId);
}
