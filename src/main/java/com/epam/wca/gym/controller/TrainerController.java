package com.epam.wca.gym.controller;

import com.epam.wca.gym.aop.CheckTrainer;
import com.epam.wca.gym.dto.trainer.TrainerSendDTO;
import com.epam.wca.gym.dto.trainer.TrainerTrainingCreateDTO;
import com.epam.wca.gym.dto.trainer.TrainerUpdateDTO;
import com.epam.wca.gym.dto.training.TrainerTrainingQuery;
import com.epam.wca.gym.dto.training.TrainingBasicDTO;
import com.epam.wca.gym.entity.Trainer;
import com.epam.wca.gym.service.TrainerService;
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
@RequestMapping(value = "user/trainer")
@RequiredArgsConstructor
public class TrainerController {
    private final TrainerService trainerService;

    @Value("${gym.api.request.attribute.user}")
    private String authenticatedUserRequestAttributeName;

    @Operation(
            summary = "Get Trainer Profile",
            description = "Retrieves the profile of a trainer by authentication details."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Successful retrieval of the trainer's profile."
    )
    @ApiResponse(
            responseCode = "401",
            description = ResponseMessages.UNAUTHORIZED_ACCESS_DESCRIPTION
    )
    @GetMapping("/profiles")
    @CheckTrainer
    public TrainerSendDTO getTrainerProfile(HttpServletRequest request) {
        var authenticatedTrainer = (Trainer) request.getAttribute(authenticatedUserRequestAttributeName);

        return DTOFactory.createTrainerSendDTO(authenticatedTrainer);
    }

    @Operation(
            summary = "Update Trainer Profile",
            description = "Update Trainer Profile with trainer given input."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Profile updated successfully."
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
    @CheckTrainer
    public TrainerSendDTO updateTrainerProfile(
            @RequestBody @Valid TrainerUpdateDTO trainerUpdateDTO,
            HttpServletRequest request
    ) {
        var authenticatedTrainer = (Trainer) request.getAttribute(authenticatedUserRequestAttributeName);

        return trainerService.update(authenticatedTrainer, trainerUpdateDTO);
    }

    @Operation(
            summary = "Delete Trainee Profile",
            description = "Deletes the trainer profile of the authenticated trainer."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Successfully deleted the trainer profile."
    )
    @ApiResponse(
            responseCode = "401",
            description = ResponseMessages.UNAUTHORIZED_ACCESS_DESCRIPTION
    )
    @DeleteMapping(value = "/profiles")
    @CheckTrainer
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTrainer(HttpServletRequest request) {
        var authenticatedTrainer = (Trainer) request.getAttribute(authenticatedUserRequestAttributeName);

        trainerService.deleteById(authenticatedTrainer.getId());
    }

    //TODO: transfer all input to RequestParam
    @Operation(
            summary = "Get Trainer Trainings List"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Successfully retrieved list of trainings."
    )
    @ApiResponse(
            responseCode = "400",
            description = ResponseMessages.INVALID_INPUT_DESCRIPTION
    )
    @ApiResponse(
            responseCode = "401",
            description = ResponseMessages.UNAUTHORIZED_ACCESS_DESCRIPTION
    )
    @PostMapping("/trainings/filter")
    @CheckTrainer
    public List<TrainingBasicDTO> getTrainerTrainingsList(
            @RequestBody @Valid TrainerTrainingQuery trainerTrainingQuery,
            HttpServletRequest request
    ) {
        var authenticatedTrainer = (Trainer) request.getAttribute(authenticatedUserRequestAttributeName);

        return trainerService.findTrainingsFiltered(authenticatedTrainer.getId(), trainerTrainingQuery);
    }

    @Operation(
            summary = "Creates training for trainer",
            description = "Creates training with trainer specific input data."
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
    @CheckTrainer
    @ResponseStatus(HttpStatus.CREATED)
    public String createTraineeTraining(
            @RequestBody @Valid TrainerTrainingCreateDTO trainingDTO,
            HttpServletRequest request
    ) {
        var authenticatedTrainer = (Trainer) request.getAttribute(authenticatedUserRequestAttributeName);

        trainerService.createTraining(authenticatedTrainer, trainingDTO);

        return "Training Created Successfully";
    }
}
