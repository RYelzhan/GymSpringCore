package com.epam.wca.gym.controller;

import com.epam.wca.gym.dto.user.UserActivationDTO;
import com.epam.wca.gym.dto.user.UserUpdateDTO;
import com.epam.wca.gym.util.ResponseMessages;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@SecurityRequirement(name = "jwtToken")
@RequestMapping(value = "/user")
public interface UserController {
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
    @GetMapping("/info")
    String getUserInfo(HttpServletRequest request);

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
    @PutMapping(value = "/change/password", consumes = MediaType.APPLICATION_JSON_VALUE)
    String changeUserPassword(
            @RequestBody @Valid UserUpdateDTO userDTO,
            HttpServletRequest request
    );

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
    @PatchMapping(value = "/change/active", consumes = MediaType.APPLICATION_JSON_VALUE)
    String activateDeactivateUser(
            @RequestBody @Valid UserActivationDTO userDTO,
            HttpServletRequest request
    );
}
