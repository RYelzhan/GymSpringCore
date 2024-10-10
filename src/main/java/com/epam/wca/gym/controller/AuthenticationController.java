package com.epam.wca.gym.controller;

import com.epam.wca.gym.aop.Logging;
import com.epam.wca.gym.dto.trainee.TraineeRegistrationDTO;
import com.epam.wca.gym.dto.trainer.TrainerRegistrationDTO;
import com.epam.wca.gym.dto.user.UserAuthenticatedDTO;
import com.epam.wca.gym.dto.user.UserLoginDTO;
import com.epam.wca.gym.service.AuthService;
import com.epam.wca.gym.service.TraineeService;
import com.epam.wca.gym.service.TrainerService;
import com.epam.wca.gym.util.ResponseMessages;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/authenticate", consumes = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthService authService;
    private final TraineeService traineeService;
    private final TrainerService trainerService;

    @Operation(
            summary = "User Login",
            description = "Authenticates a user based on the username and password."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Login successful"
    )
    @ApiResponse(
            responseCode = "400",
            description = ResponseMessages.INVALID_INPUT_DESCRIPTION
    )
    @PostMapping("/login")
    @Logging
    public String login(@RequestBody @Valid UserLoginDTO loginDTO) {
        authService.authenticate(loginDTO);

        return "Login Successful";
    }

    @Operation(
            summary = "Register a new trainee",
            description = "Registers a new trainee and returns their username and password."
    )
    @ApiResponse(
            responseCode = "201",
            description = ResponseMessages.SUCCESSFUL_REGISTRATION_DESCRIPTION
    )
    @ApiResponse(
            responseCode = "400",
            description = ResponseMessages.INVALID_INPUT_DESCRIPTION
    )
    @PostMapping(value = "/register/trainee")
    @ResponseStatus(HttpStatus.CREATED)
    public UserAuthenticatedDTO registerTrainee(@RequestBody @Valid TraineeRegistrationDTO traineeDTO) {
        return traineeService.save(traineeDTO);
    }

    @Operation(
            summary = "Register a new trainer",
            description = "Registers a new trainer and returns their username and password."
    )
    @ApiResponse(
            responseCode = "201",
            description = ResponseMessages.SUCCESSFUL_REGISTRATION_DESCRIPTION
    )
    @ApiResponse(
            responseCode = "400",
            description = ResponseMessages.INVALID_INPUT_DESCRIPTION
    )
    @PostMapping(value = "/register/trainer")
    @ResponseStatus(HttpStatus.CREATED)
    public UserAuthenticatedDTO registerTrainer(@RequestBody @Valid TrainerRegistrationDTO trainerDTO) {
        return trainerService.save(trainerDTO);
    }
}
