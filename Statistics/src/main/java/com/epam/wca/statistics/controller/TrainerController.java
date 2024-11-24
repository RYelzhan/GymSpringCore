package com.epam.wca.statistics.controller;

import com.epam.wca.common.gymcommon.statistics_dto.TrainerTrainingAddDTO;
import com.epam.wca.common.gymcommon.statistics_dto.TrainerWorkloadSummary;
import com.epam.wca.common.gymcommon.statistics_dto.TrainersTrainingsDeleteDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

@RequestMapping("/trainers")
public interface TrainerController {
    @PostMapping("/trainings")
    void addNewTraining(@RequestBody @Valid TrainerTrainingAddDTO trainingAddDTO);

    @GetMapping("/trainings/{username}")
    TrainerWorkloadSummary getWorkload(@PathVariable(name = "username") String username);

    @DeleteMapping("/trainings")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteTrainings(@RequestBody @Valid TrainersTrainingsDeleteDTO trainingsDeleteDTO);
}
