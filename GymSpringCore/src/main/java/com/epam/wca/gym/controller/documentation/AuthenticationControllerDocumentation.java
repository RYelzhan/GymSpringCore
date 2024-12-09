package com.epam.wca.gym.controller.documentation;

import com.epam.wca.gym.dto.trainee.TraineeRegistrationDTO;
import com.epam.wca.gym.dto.trainer.TrainerRegistrationDTO;
import com.epam.wca.gym.dto.user.UserAuthenticatedDTO;
import com.epam.wca.common.gymcommon.util.ResponseMessages;
import com.epam.wca.gym.controller.AuthenticationController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestBody;

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
    @Override
    UserAuthenticatedDTO registerTrainer(@RequestBody @Valid TrainerRegistrationDTO trainerDTO);
}
