package com.epam.wca.gym.controller;

import com.epam.wca.gym.dto.training_type.TrainingTypeBasicDTO;
import com.epam.wca.gym.util.ResponseMessages;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@SecurityRequirement(name = "jwtToken")
@RequestMapping(value = "/trainings", consumes = MediaType.ALL_VALUE)
public interface TrainingController {
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
    @GetMapping("/types")
    List<TrainingTypeBasicDTO> getTypes();
}
