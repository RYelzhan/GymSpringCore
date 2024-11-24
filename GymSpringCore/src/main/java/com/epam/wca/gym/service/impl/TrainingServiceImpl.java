package com.epam.wca.gym.service.impl;

import com.epam.wca.gym.entity.Training;
import com.epam.wca.gym.repository.TrainingRepository;
import com.epam.wca.gym.communication.StatisticsCommunicationService;
import com.epam.wca.gym.service.TrainingService;
import com.epam.wca.gym.util.DTOFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TrainingServiceImpl implements TrainingService {
    private final TrainingRepository trainingRepository;
    private final StatisticsCommunicationService statisticsCommunicationService;

    @Override
    @Transactional
    public void save(Training training) {
        trainingRepository.save(training);

        var request = DTOFactory.createTrainerTrainingAddDTO(training);

        log.info("Sending request to Statistics Service: {}", request);

        statisticsCommunicationService.addNewTraining(request);
    }
}
