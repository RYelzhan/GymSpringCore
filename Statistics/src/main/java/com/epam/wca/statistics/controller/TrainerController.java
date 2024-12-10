package com.epam.wca.statistics.controller;

import com.epam.wca.common.gymcommon.statistics_dto.TrainerTrainingAddDTO;
import com.epam.wca.common.gymcommon.statistics_dto.TrainerWorkloadSummary;
import com.epam.wca.common.gymcommon.statistics_dto.TrainersTrainingsDeleteDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

@RequestMapping(value = "/trainers", consumes = MediaType.ALL_VALUE)
public interface TrainerController {
    @PostMapping(value = "/trainings", consumes = MediaType.APPLICATION_JSON_VALUE)
    void addNewTraining(@RequestBody @Valid TrainerTrainingAddDTO trainingAddDTO);

    @GetMapping("/trainings")
    TrainerWorkloadSummary getWorkload();

    @DeleteMapping("/trainings")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteTrainings(@RequestBody @Valid TrainersTrainingsDeleteDTO trainingsDeleteDTO);
}
