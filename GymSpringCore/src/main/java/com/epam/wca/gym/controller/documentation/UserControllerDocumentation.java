package com.epam.wca.gym.controller.documentation;

import com.epam.wca.common.gymcommon.util.ResponseMessages;
import com.epam.wca.gym.aop.argument.InsertUser;
import com.epam.wca.gym.controller.UserController;
import com.epam.wca.gym.dto.user.UserActivationDTO;
import com.epam.wca.gym.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestBody;

@SecurityRequirement(name = "jwtToken")
public interface UserControllerDocumentation extends UserController {
    @Operation(
            summary = "Return User username",
            description = "First ever endpoint."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Returns authenticated user's username."
    )
    @ApiResponse(
            responseCode = "401",
            description = ResponseMessages.UNAUTHORIZED_ACCESS_DESCRIPTION
    )
    @Override
    String getInfo(@InsertUser User authenticatedUser);

    @Operation(
            summary = "Activate or Deactivate User"
    )
    @ApiResponse(
            responseCode = "200",
            description = "User activation status updated successfully."
    )
    @ApiResponse(
            responseCode = "400",
            description = ResponseMessages.INVALID_INPUT_DESCRIPTION
    )
    @ApiResponse(
            responseCode = "401",
            description = ResponseMessages.UNAUTHORIZED_ACCESS_DESCRIPTION
    )
    @Override
    String activateDeactivate(
            @RequestBody @Valid UserActivationDTO userDTO,
            @InsertUser User authenticatedUser
    );
}
