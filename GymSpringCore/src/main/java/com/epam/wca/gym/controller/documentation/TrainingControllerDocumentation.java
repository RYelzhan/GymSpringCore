package com.epam.wca.gym.controller.documentation;

import com.epam.wca.gym.controller.TrainingController;
import com.epam.wca.gym.dto.training_type.TrainingTypeBasicDTO;
import com.epam.wca.common.gymcommon.util.ResponseMessages;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import java.util.List;

@SecurityRequirement(name = "jwtToken")
public interface TrainingControllerDocumentation extends TrainingController {
    @Operation(
            summary = "Get Training Types"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Successfully retrieved list of training types."
    )
    @ApiResponse(
            responseCode = "401",
            description = ResponseMessages.UNAUTHORIZED_ACCESS_DESCRIPTION
    )
    @Override
    List<TrainingTypeBasicDTO> getTypes();
}
