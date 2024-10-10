package com.epam.wca.gym.controller;

import com.epam.wca.gym.aop.CheckTrainee;
import com.epam.wca.gym.dto.trainee.TraineeSendDTO;
import com.epam.wca.gym.dto.trainee.TraineeTrainersUpdateDTO;
import com.epam.wca.gym.dto.trainee.TraineeTrainingCreateDTO;
import com.epam.wca.gym.dto.trainee.TraineeUpdateDTO;
import com.epam.wca.gym.dto.trainer.TrainerBasicDTO;
import com.epam.wca.gym.dto.training.TraineeTrainingQuery;
import com.epam.wca.gym.dto.training.TrainingBasicDTO;
import com.epam.wca.gym.entity.Trainee;
import com.epam.wca.gym.service.TraineeService;
import com.epam.wca.gym.util.DTOFactory;
import com.epam.wca.gym.util.ResponseMessages;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@SecurityRequirement(name = "basicAuth")
@RestController
@RequestMapping(value = "/user/trainee")
@RequiredArgsConstructor
public class TraineeController {
    private final TraineeService traineeService;
    @Value("${gym.api.request.attribute.user}")
    private String authenticatedUserRequestAttributeName;

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
    @GetMapping("/profiles")
    @CheckTrainee
    @Transactional
    public TraineeSendDTO getTraineeProfile(HttpServletRequest request) {
        var trainee = (Trainee) request.getAttribute(authenticatedUserRequestAttributeName);

        return DTOFactory.createTraineeSendDTO(trainee);
    }

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
    @PutMapping(value = "/profiles", consumes = MediaType.APPLICATION_JSON_VALUE)
    @CheckTrainee
    public TraineeSendDTO updateTraineeProfile(
            @RequestBody @Valid TraineeUpdateDTO traineeDTO,
            HttpServletRequest request
    ) {
        var authenticatedTrainee = (Trainee) request.getAttribute(authenticatedUserRequestAttributeName);

        return traineeService.update(authenticatedTrainee, traineeDTO);
    }

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
    @DeleteMapping(value = "/profiles")
    @CheckTrainee
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTrainee(HttpServletRequest request) {
        var authenticatedTrainee = (Trainee) request.getAttribute(authenticatedUserRequestAttributeName);

        traineeService.deleteById(authenticatedTrainee.getId());
    }

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
    @GetMapping("/trainers/available")
    @CheckTrainee
    public List<TrainerBasicDTO> getNotAssignedTrainers(HttpServletRequest request) {
        var authenticatedTrainee = (Trainee) request.getAttribute(authenticatedUserRequestAttributeName);

        return traineeService.getListOfNotAssignedTrainers(authenticatedTrainee);
    }

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
    @CheckTrainee
    public List<TrainerBasicDTO> updateTrainerList(
            @RequestBody @Valid TraineeTrainersUpdateDTO traineeTrainersUpdateDTO,
            HttpServletRequest request
            ) {
        var authenticatedTrainee = (Trainee) request.getAttribute(authenticatedUserRequestAttributeName);

        return traineeService.addTrainers(authenticatedTrainee, traineeTrainersUpdateDTO);
    }

    //TODO: transfer all input to RequestParam
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
    @CheckTrainee
    public List<TrainingBasicDTO> getTraineeTrainingsList(
            @RequestBody @Valid TraineeTrainingQuery traineeTrainingQuery,
            HttpServletRequest request
            ) {
        var authenticatedTrainee = (Trainee) request.getAttribute(authenticatedUserRequestAttributeName);

        return traineeService.findTrainingsFiltered(authenticatedTrainee.getId(), traineeTrainingQuery);
    }

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
    @CheckTrainee
    @ResponseStatus(HttpStatus.CREATED)
    public String createTraineeTraining(
            @RequestBody @Valid TraineeTrainingCreateDTO trainingDTO,
            HttpServletRequest request
    ) {
        var authenticatedTrainee = (Trainee) request.getAttribute(authenticatedUserRequestAttributeName);

        traineeService.createTraining(authenticatedTrainee, trainingDTO);

        return "Training Created Successfully";
    }
}
