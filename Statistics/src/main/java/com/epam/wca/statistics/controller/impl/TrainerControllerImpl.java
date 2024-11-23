package com.epam.wca.statistics.controller.impl;

import com.epam.wca.statistics.aop.Logging;
import com.epam.wca.statistics.controller.TrainerController;
import com.epam.wca.statistics.dto.TrainerTrainingAddDTO;
import com.epam.wca.statistics.dto.TrainerWorkloadSummary;
import com.epam.wca.statistics.service.TrainerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TrainerControllerImpl implements TrainerController {
    private final TrainerService trainerService;

    @Override
    @Logging
    public void addNewTraining(@RequestBody @Valid TrainerTrainingAddDTO trainingAddDTO) {
        trainerService.addNewTraining(trainingAddDTO);
    }

    @Override
    @Logging
    public TrainerWorkloadSummary getWorkload(@PathVariable("username") String username) {
        return trainerService.getWorkload(username);
    }
}
