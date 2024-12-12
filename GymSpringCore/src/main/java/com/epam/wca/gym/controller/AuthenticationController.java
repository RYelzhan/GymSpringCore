package com.epam.wca.gym.controller;

import com.epam.wca.gym.dto.trainee.TraineeRegistrationDTO;
import com.epam.wca.gym.dto.trainer.TrainerRegistrationDTO;
import com.epam.wca.common.gymcommon.auth_dto.UserAuthenticatedDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

@RequestMapping(value = "/authentication", consumes = MediaType.APPLICATION_JSON_VALUE)
public interface AuthenticationController {

    @PostMapping(value = "/account/trainee")
    @ResponseStatus(HttpStatus.CREATED)
    UserAuthenticatedDTO registerTrainee(@RequestBody @Valid TraineeRegistrationDTO traineeDTO);

    @PostMapping(value = "/account/trainer")
    @ResponseStatus(HttpStatus.CREATED)
    UserAuthenticatedDTO registerTrainer(@RequestBody @Valid TrainerRegistrationDTO trainerDTO);
}
