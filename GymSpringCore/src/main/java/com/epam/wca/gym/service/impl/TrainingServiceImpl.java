package com.epam.wca.gym.service.impl;

import com.epam.wca.gym.dto.statistics_dto.TrainerTrainingAddDTO;
import com.epam.wca.gym.entity.Trainer;
import com.epam.wca.gym.entity.Training;
import com.epam.wca.gym.feign.StatisticsClient;
import com.epam.wca.gym.repository.TrainingRepository;
import com.epam.wca.gym.service.TrainingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TrainingServiceImpl implements TrainingService {
    private final TrainingRepository trainingRepository;
    private final StatisticsClient statisticsClient;

    @Override
    public void save(Training training) {
        trainingRepository.save(training);

        Trainer trainer = training.getTrainer();

        TrainerTrainingAddDTO request = new TrainerTrainingAddDTO(
                trainer.getUsername(),
                trainer.getFirstName(),
                trainer.getLastName(),
                trainer.isActive(),
                training.getTrainingDate(),
                training.getTrainingDuration()
        );

        log.info("Sending request to Statistics Service: {}", request);

        statisticsClient.addNewTraining(request);
    }
}
