package com.epam.wca.gym.service.impl;

import com.epam.wca.gym.dto.TraineeDTO;
import com.epam.wca.gym.entity.Trainee;
import com.epam.wca.gym.repository.TraineeDAO;
import com.epam.wca.gym.service.TraineeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TraineeServiceImpl implements TraineeService {
    @Autowired
    private TraineeDAO traineeDAO;

    public Trainee create(TraineeDTO traineeDTO) {
        Trainee newTrainee = traineeDAO.save(new Trainee(traineeDTO.firstName(), traineeDTO.lastName(), traineeDTO.dateOfBirth(), traineeDTO.address()));

        log.info("Creating new Trainee: " + newTrainee.getUserName());

        return newTrainee;
    }

    public void updateByUsername(String username, Trainee trainee) {
        traineeDAO.updateByUsername(username, trainee);
    }

    public void deleteByUsername(String username) {
        traineeDAO.deleteByUsername(username);
    }

    public Trainee findByUsername(String username) {
        return traineeDAO.findByUsername(username);
    }

    public Trainee findById(long id) {
        return traineeDAO.findById(id);
    }
}
