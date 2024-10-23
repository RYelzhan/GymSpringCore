package com.epam.wca.gym.controller.documentation;

import com.epam.wca.gym.controller.TrainerController;
import com.epam.wca.gym.dto.trainer.TrainerSendDTO;
import com.epam.wca.gym.dto.trainer.TrainerTrainingCreateDTO;
import com.epam.wca.gym.dto.trainer.TrainerUpdateDTO;
import com.epam.wca.gym.dto.training.TrainerTrainingQuery;
import com.epam.wca.gym.dto.training.TrainingBasicDTO;
import com.epam.wca.gym.util.ResponseMessages;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@SecurityRequirement(name = "jwtToken")
public interface TrainerControllerDocumentation extends TrainerController {
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
    TrainerSendDTO getProfile(HttpServletRequest request);

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
    TrainerSendDTO updateProfile(
            @RequestBody @Valid TrainerUpdateDTO trainerUpdateDTO,
            HttpServletRequest request
    );

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
    void deleteProfile(HttpServletRequest request);

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
    List<TrainingBasicDTO> getTrainings(
            @RequestBody @Valid TrainerTrainingQuery trainerTrainingQuery,
            HttpServletRequest request
    );

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
    String createTraining(
            @RequestBody @Valid TrainerTrainingCreateDTO trainingDTO,
            HttpServletRequest request
    );
}
