package com.epam.wca.gym.service.impl;

import com.epam.wca.gym.entity.Training;
import com.epam.wca.gym.repository.TrainingRepository;
import com.epam.wca.gym.service.TrainingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TrainingServiceImpl implements TrainingService {
    private final TrainingRepository trainingRepository;

    @Override
    public void save(Training training) {
        trainingRepository.save(training);
    }
}
