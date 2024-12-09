package com.epam.wca.gym.controller;

import com.epam.wca.gym.aop.argument.InsertUser;
import com.epam.wca.gym.aop.argument.InsertUserId;
import com.epam.wca.gym.dto.trainee.TraineeSendDTO;
import com.epam.wca.gym.dto.trainee.TraineeTrainersUpdateDTO;
import com.epam.wca.gym.dto.trainee.TraineeTrainingCreateDTO;
import com.epam.wca.gym.dto.trainee.TraineeUpdateDTO;
import com.epam.wca.gym.dto.trainer.TrainerBasicDTO;
import com.epam.wca.gym.dto.training.TraineeTrainingQuery;
import com.epam.wca.gym.dto.training.TrainingBasicDTO;
import com.epam.wca.gym.entity.Trainee;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

@RequestMapping(value = "/users/trainees", consumes = MediaType.APPLICATION_JSON_VALUE)
public interface TraineeController {
    @GetMapping(value = "/profiles", consumes = MediaType.ALL_VALUE)
    TraineeSendDTO getProfile(
            @InsertUser Trainee authenticatedTrainee
    );

    @PutMapping(value = "/profiles")
    TraineeSendDTO updateProfile(
            @RequestBody @Valid TraineeUpdateDTO traineeDTO,
            @InsertUser Trainee authenticatedTrainee
    );

    @DeleteMapping(value = "/profiles", consumes = MediaType.ALL_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteProfile(
            @InsertUserId Long id
    );

    @GetMapping(value = "/trainers", consumes = MediaType.ALL_VALUE)
    List<TrainerBasicDTO> getNotAssignedTrainers(
            @InsertUser Trainee authenticatedTrainee
    );

    @PutMapping(value = "/trainers", consumes = MediaType.APPLICATION_JSON_VALUE)
    List<TrainerBasicDTO> updateTrainerList(
            @RequestBody @Valid TraineeTrainersUpdateDTO traineeTrainersUpdateDTO,
            @InsertUser Trainee authenticatedTrainee
    );

    @PostMapping(value = "/trainings", consumes = MediaType.ALL_VALUE)
    List<TrainingBasicDTO> getTrainings(
            @RequestBody @Valid TraineeTrainingQuery traineeTrainingQuery,
            @InsertUser Trainee authenticatedTrainee
    );

    @PostMapping("/trainings/new")
    @ResponseStatus(HttpStatus.CREATED)
    String createTraining(
            @RequestBody @Valid TraineeTrainingCreateDTO trainingDTO,
            @InsertUser Trainee authenticatedTrainee
    );
}
