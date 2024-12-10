package com.epam.wca.gym.controller;

import com.epam.wca.gym.aop.argument.InsertUser;
import com.epam.wca.gym.aop.argument.InsertUserId;
import com.epam.wca.gym.dto.trainer.TrainerSendDTO;
import com.epam.wca.gym.dto.trainer.TrainerTrainingCreateDTO;
import com.epam.wca.gym.dto.trainer.TrainerUpdateDTO;
import com.epam.wca.gym.dto.training.TrainerTrainingQuery;
import com.epam.wca.gym.dto.training.TrainingBasicDTO;
import com.epam.wca.gym.entity.Trainer;
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

@RequestMapping(value = "/users/trainers", consumes = MediaType.APPLICATION_JSON_VALUE)
public interface TrainerController {
    @GetMapping(value = "/profiles", consumes = MediaType.ALL_VALUE)
    TrainerSendDTO getProfile(
            @InsertUser Trainer authenticatedTrainer
    );

    @PutMapping(value = "/profiles")
    TrainerSendDTO updateProfile(
            @RequestBody @Valid TrainerUpdateDTO trainerUpdateDTO,
            @InsertUser Trainer authenticatedTrainer
    );

    @DeleteMapping(value = "/profiles", consumes = MediaType.ALL_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteProfile(
            @InsertUserId Long id
    );

    @PostMapping(value = "/trainings")
    List<TrainingBasicDTO> getTrainings(
            @RequestBody @Valid TrainerTrainingQuery trainerTrainingQuery,
            @InsertUser Trainer authenticatedTrainer
    );

    @PostMapping(value = "/trainings/new")
    @ResponseStatus(HttpStatus.ACCEPTED)
    String createTraining(
            @RequestBody @Valid TrainerTrainingCreateDTO trainingDTO,
            @InsertUser Trainer authenticatedTrainer
    );
}

