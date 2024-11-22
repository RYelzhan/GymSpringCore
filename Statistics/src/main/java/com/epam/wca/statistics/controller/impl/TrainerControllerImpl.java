package com.epam.wca.statistics.controller.impl;

import com.epam.wca.statistics.controller.TrainerController;
import com.epam.wca.statistics.dto.TrainerTrainingAddDTO;
import com.epam.wca.statistics.dto.TrainerWorkloadSummary;
import com.epam.wca.statistics.service.TrainerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class TrainerControllerImpl implements TrainerController {
    private final TrainerService trainerService;

    @Override
    public void addNewTraining(TrainerTrainingAddDTO trainingAddDTO) {
        trainerService.addNewTraining(trainingAddDTO);
    }

    @Override
    public TrainerWorkloadSummary getWorkload(String username) {
        return trainerService.getWorkload(username);
    }
}
