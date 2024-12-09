package com.epam.wca.authentication.controller.documentation;

import com.epam.wca.authentication.controller.AuthenticationController;
import com.epam.wca.authentication.dto.UserUpdateDTO;
import com.epam.wca.authentication.entity.User;
import com.epam.wca.common.gymcommon.util.ResponseMessages;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestBody;

public interface AuthenticationControllerDocumentation extends AuthenticationController {
    @Operation(
            summary = "User Authentication",
            description = "Authenticates a user based on the jwt token."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Authentication successful"
    )
    @ApiResponse(
            responseCode = "401",
            description = ResponseMessages.UNAUTHORIZED_ACCESS_DESCRIPTION
    )
    @SecurityRequirement(name = "jwtToken")
    @Override
    Long authenticate(@AuthenticationPrincipal User user);

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
    @Override
    String login(@AuthenticationPrincipal User user);

    @Operation(
            summary = "User Deletion",
            description = "Deletes authenticated User."
    )
    @ApiResponse(
            responseCode = "204",
            description = "Deletion successful"
    )
    @ApiResponse(
            responseCode = "401",
            description = ResponseMessages.UNAUTHORIZED_ACCESS_DESCRIPTION
    )
    @SecurityRequirement(name = "jwtToken")
    @Override
    void delete(User user);

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
    @Override
    String logout();

    @Operation(
            summary = "Change User Password"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Password changed successfully."
    )
    @ApiResponse(
            responseCode = "400",
            description = ResponseMessages.INVALID_INPUT_DESCRIPTION
    )
    @ApiResponse(
            responseCode = "401",
            description = ResponseMessages.UNAUTHORIZED_ACCESS_DESCRIPTION
    )
    String changePassword(
            @RequestBody @Valid UserUpdateDTO userDTO,
            @AuthenticationPrincipal User authenticatedUser
    );
}
