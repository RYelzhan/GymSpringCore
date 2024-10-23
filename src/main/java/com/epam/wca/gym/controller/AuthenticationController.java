package com.epam.wca.gym.controller;

import com.epam.wca.gym.dto.trainee.TraineeRegistrationDTO;
import com.epam.wca.gym.dto.trainer.TrainerRegistrationDTO;
import com.epam.wca.gym.dto.user.UserAuthenticatedDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

@RequestMapping(value = "/authentication", consumes = MediaType.APPLICATION_JSON_VALUE)
public interface AuthenticationController {
    @PostMapping(value = "/login", consumes = MediaType.ALL_VALUE)
    String login(@AuthenticationPrincipal UserDetails user);

    @PostMapping(value = "/logout", consumes = MediaType.ALL_VALUE)
    String logout();

    @PostMapping(value = "/account/trainee")
    @ResponseStatus(HttpStatus.CREATED)
    UserAuthenticatedDTO registerTrainee(@RequestBody @Valid TraineeRegistrationDTO traineeDTO);

    @PostMapping(value = "/account/trainer")
    @ResponseStatus(HttpStatus.CREATED)
    UserAuthenticatedDTO registerTrainer(@RequestBody @Valid TrainerRegistrationDTO trainerDTO);
}
