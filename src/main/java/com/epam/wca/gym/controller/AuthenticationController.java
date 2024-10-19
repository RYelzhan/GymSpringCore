package com.epam.wca.gym.controller;

import com.epam.wca.gym.dto.trainee.TraineeRegistrationDTO;
import com.epam.wca.gym.dto.trainer.TrainerRegistrationDTO;
import com.epam.wca.gym.dto.user.UserAuthenticatedDTO;
import com.epam.wca.gym.util.ResponseMessages;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

@RequestMapping(value = "/authenticate", consumes = MediaType.APPLICATION_JSON_VALUE)
public interface AuthenticationController {
    @Operation(
            summary = "User Login",
            description = "Authenticates a user based on the username and password."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Login successful"
    )
    @ApiResponse(
            responseCode = "401",
            description = ResponseMessages.UNAUTHORIZED_ACCESS_DESCRIPTION
    )
    @SecurityRequirement(name = "basicAuth")
    @PostMapping("/login")
    String login(@AuthenticationPrincipal UserDetails user);

    @Operation(
            summary = "User Logout",
            description = "Logouts a user based on the username and password."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Logout successful"
    )
    @ApiResponse(
            responseCode = "401",
            description = ResponseMessages.UNAUTHORIZED_ACCESS_DESCRIPTION
    )
    @SecurityRequirement(name = "basicAuth")
    @PostMapping("/logout")
    String logout();

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
    UserAuthenticatedDTO registerTrainee(@RequestBody @Valid TraineeRegistrationDTO traineeDTO);

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
    UserAuthenticatedDTO registerTrainer(@RequestBody @Valid TrainerRegistrationDTO trainerDTO);
}
