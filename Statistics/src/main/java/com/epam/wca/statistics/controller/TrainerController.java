package com.epam.wca.statistics.controller;

import com.epam.wca.statistics.dto.TrainerTrainingAddDTO;
import com.epam.wca.statistics.dto.TrainerWorkloadSummary;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

@RequestMapping("/trainers")
public interface TrainerController {
    @PostMapping("/trainings")
    @ResponseStatus(HttpStatus.OK)
    void addNewTraining(@RequestBody @Valid TrainerTrainingAddDTO trainingAddDTO);

    @GetMapping("/trainings/{username}")
    @ResponseStatus(HttpStatus.OK)
    TrainerWorkloadSummary getWorkload(@PathVariable(name = "username") String username);
}
