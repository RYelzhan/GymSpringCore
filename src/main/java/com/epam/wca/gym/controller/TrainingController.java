package com.epam.wca.gym.controller;

import com.epam.wca.gym.dto.training_type.TrainingTypeBasicDTO;
import com.epam.wca.gym.service.TrainingTypeService;
import com.epam.wca.gym.util.ResponseMessages;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@SecurityRequirement(name = "jwtToken")
@RestController
@RequestMapping(value = "/training")
@RequiredArgsConstructor
public class TrainingController {
    private final TrainingTypeService trainingTypeService;

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
    public List<TrainingTypeBasicDTO> getTrainingTypes() {
        return trainingTypeService.findAll();
    }
}
