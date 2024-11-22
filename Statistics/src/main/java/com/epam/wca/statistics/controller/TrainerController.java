package com.epam.wca.statistics.controller;

import com.epam.wca.statistics.dto.TrainerTrainingAddDTO;
import com.epam.wca.statistics.dto.TrainerWorkloadSummary;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

@RequestMapping("/trainer")
public interface TrainerController {
    @PostMapping("/training")
    @ResponseStatus(HttpStatus.OK)
    void addNewTraining(TrainerTrainingAddDTO trainingAddDTO);

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    TrainerWorkloadSummary getWorkload(String username);
}
