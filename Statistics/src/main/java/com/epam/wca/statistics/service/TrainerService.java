package com.epam.wca.statistics.service;

import com.epam.wca.statistics.dto.TrainerTrainingAddDTO;
import com.epam.wca.statistics.dto.TrainerWorkloadSummary;

public interface TrainerService {
    void addNewTraining(TrainerTrainingAddDTO trainingAddDTO);
    TrainerWorkloadSummary getWorkload(String username);
}
