package com.epam.wca.gym.controller.documentation;

import com.epam.wca.common.gymcommon.auth_dto.UserAuthenticatedDTO;
import com.epam.wca.common.gymcommon.util.ResponseMessages;
import com.epam.wca.gym.controller.AuthenticationController;
import com.epam.wca.gym.dto.trainee.TraineeRegistrationDTO;
import com.epam.wca.gym.dto.trainer.TrainerRegistrationDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

public interface AuthenticationControllerDocumentation extends AuthenticationController {
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
    @Override
    UserAuthenticatedDTO registerTrainee(TraineeRegistrationDTO traineeDTO);

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
    @Override
    UserAuthenticatedDTO registerTrainer(TrainerRegistrationDTO trainerDTO);
}
