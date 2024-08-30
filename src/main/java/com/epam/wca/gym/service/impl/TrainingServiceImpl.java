package com.epam.wca.gym.service.impl;

import com.epam.wca.gym.dto.TrainingDTO;
import com.epam.wca.gym.entity.Training;
import com.epam.wca.gym.repository.TrainingDAO;
import com.epam.wca.gym.service.TrainingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TrainingServiceImpl implements TrainingService {
    @Autowired
    private TrainingDAO trainingDAO;
    public void createTraining(TrainingDTO trainingDTO) {
        Training training = new Training(0, trainingDTO.traineeId(), trainingDTO.trainerId(), trainingDTO.trainingName(),
                trainingDTO.trainingType(), trainingDTO.trainingDate(), trainingDTO.trainingDuration());
        trainingDAO.save(training);
    }

    public com.epam.wca.gym.entity.Training findTrainingById(long id) {
        return trainingDAO.findById(id);
    }
}