package com.epam.wca.gym.service.impl;

import com.epam.wca.gym.dto.TraineeDTO;
import com.epam.wca.gym.entity.Trainee;
import com.epam.wca.gym.repository.TraineeDAO;
import com.epam.wca.gym.service.TraineeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TraineeServiceImpl implements TraineeService {
    @Autowired
    private TraineeDAO traineeDAO;

    public void createTrainee(TraineeDTO traineeDTO) {
        Trainee trainee = new Trainee(traineeDTO.getFirstName(), traineeDTO.getLastName(), traineeDTO.getDateOfBirth(), traineeDTO.getAddress());
        traineeDAO.save(trainee);
    }

    public void updateTraineeByUsername(String username, Trainee trainee) {
        traineeDAO.updateByUsername(username, trainee);
    }

    public void deleteTraineeByUsername(String username) {
        traineeDAO.deleteByUsername(username);
    }

    public Trainee findTraineeByUsername(String username) {
        return traineeDAO.findByUsername(username);
    }
}