package com.epam.wca.gym.service.impl;

import com.epam.wca.gym.dto.TrainingDTO;
import com.epam.wca.gym.entity.Training;
import com.epam.wca.gym.repository.TrainingDAO;
import com.epam.wca.gym.service.TraineeService;
import com.epam.wca.gym.service.TrainerService;
import com.epam.wca.gym.service.TrainingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TrainingServiceImpl implements TrainingService {
    @Autowired
    private TrainingDAO trainingDAO;
    @Autowired
    private TraineeService traineeService;
    @Autowired
    private TrainerService trainerService;

    public Training createTraining(TrainingDTO trainingDTO) {
        if (traineeService.findById(trainingDTO.traineeId()) == null ||
                trainerService.findById(trainingDTO.trainerId()) == null) {
            throw new IllegalStateException();
        }

        Training training = new Training(trainingDTO.traineeId(), trainingDTO.trainerId(), trainingDTO.trainingName(),
                trainingDTO.trainingType(), trainingDTO.trainingDate(), trainingDTO.trainingDuration());

        trainingDAO.save(training);

        log.info("New Training Created. ID: " + training.getTrainingId());

        return training;
    }

    public Training findById(long id) {
        return trainingDAO.findById(id);
    }
}