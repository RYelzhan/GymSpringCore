package com.epam.wca.gym.controller;

import com.epam.wca.gym.dto.trainee.TraineeSendDTO;
import com.epam.wca.gym.dto.trainee.TraineeTrainersUpdateDTO;
import com.epam.wca.gym.dto.trainee.TraineeTrainingCreateDTO;
import com.epam.wca.gym.dto.trainee.TraineeUpdateDTO;
import com.epam.wca.gym.dto.trainer.TrainerBasicDTO;
import com.epam.wca.gym.dto.training.TraineeTrainingQuery;
import com.epam.wca.gym.dto.training.TrainingBasicDTO;
import com.epam.wca.gym.util.ResponseMessages;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
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

@SecurityRequirement(name = "jwtToken")
@RequestMapping(value = "/user/trainee", consumes = MediaType.APPLICATION_JSON_VALUE)
public interface TraineeController {
    @Operation(
            summary = "Get Trainee Profile",
            description = "Retrieves the profile of a trainee by authentication details."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Successful retrieval of the trainee's profile."
    )
    @ApiResponse(
            responseCode = "401",
            description = ResponseMessages.UNAUTHORIZED_ACCESS_DESCRIPTION
    )
    @GetMapping(value = "/profiles", consumes = MediaType.ALL_VALUE)
    TraineeSendDTO getTraineeProfile(HttpServletRequest request);

    @Operation(
            summary = "Update Trainee Profile",
            description = "Updates the profile of a trainee."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Successfully updated the trainee profile."
    )
    @ApiResponse(
            responseCode = "400",
            description = ResponseMessages.INVALID_INPUT_DESCRIPTION
    )
    @ApiResponse(
            responseCode = "401",
            description = ResponseMessages.UNAUTHORIZED_ACCESS_DESCRIPTION
    )
    @PutMapping(value = "/profiles")
    TraineeSendDTO updateTraineeProfile(
            @RequestBody @Valid TraineeUpdateDTO traineeDTO,
            HttpServletRequest request
    );

    @Operation(
            summary = "Delete Trainee Profile",
            description = "Deletes the trainee profile of the authenticated trainee."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Successfully deleted the trainee profile."
    )
    @ApiResponse(
            responseCode = "401",
            description = ResponseMessages.UNAUTHORIZED_ACCESS_DESCRIPTION
    )
    @DeleteMapping(value = "/profiles", consumes = MediaType.ALL_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteTrainee(HttpServletRequest request);

    @Operation(
            summary = "Get Available Trainers for a Trainee",
            description = "Retrieves a list of trainers that are not assigned to the specified trainee."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Successfully retrieved the list of available trainers."
    )
    @ApiResponse(
            responseCode = "401",
            description = ResponseMessages.UNAUTHORIZED_ACCESS_DESCRIPTION
    )
    @GetMapping(value = "/trainers/available", consumes = MediaType.ALL_VALUE)
    List<TrainerBasicDTO> getNotAssignedTrainers(HttpServletRequest request);

    @Operation(
            summary = "Update Trainer List for a Trainee",
            description = "Adds specified trainers to the trainee's profile."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Successfully updated the trainer list."
    )
    @ApiResponse(
            responseCode = "400",
            description = ResponseMessages.INVALID_INPUT_DESCRIPTION
    )
    @ApiResponse(
            responseCode = "401",
            description = ResponseMessages.UNAUTHORIZED_ACCESS_DESCRIPTION
    )
    @PutMapping(value = "/trainers/add", consumes = MediaType.APPLICATION_JSON_VALUE)
    List<TrainerBasicDTO> updateTrainerList(
            @RequestBody @Valid TraineeTrainersUpdateDTO traineeTrainersUpdateDTO,
            HttpServletRequest request
    );

    @Operation(
            summary = "Retrieve a list of trainings with filter",
            description = "Retrieve a list of trainings for a specific trainee based on filter criteria."
    )
    @ApiResponse(
            responseCode = "400",
            description = ResponseMessages.INVALID_INPUT_DESCRIPTION
    )
    @ApiResponse(
            responseCode = "401",
            description = ResponseMessages.UNAUTHORIZED_ACCESS_DESCRIPTION
    )
    @GetMapping("/trainings/filter")
    List<TrainingBasicDTO> getTraineeTrainingsList(
            @RequestBody @Valid TraineeTrainingQuery traineeTrainingQuery,
            HttpServletRequest request
    );

    @Operation(
            summary = "Creates training for trainee",
            description = "Creates training with trainee specific input data."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Successfully created new training."
    )
    @ApiResponse(
            responseCode = "400",
            description = ResponseMessages.INVALID_INPUT_DESCRIPTION
    )
    @ApiResponse(
            responseCode = "401",
            description = ResponseMessages.UNAUTHORIZED_ACCESS_DESCRIPTION
    )
    @PostMapping("/trainings")
    @ResponseStatus(HttpStatus.CREATED)
    String createTraineeTraining(
            @RequestBody @Valid TraineeTrainingCreateDTO trainingDTO,
            HttpServletRequest request
    );
}
