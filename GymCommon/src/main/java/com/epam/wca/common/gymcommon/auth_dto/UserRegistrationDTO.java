package com.epam.wca.common.gymcommon.auth_dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.Set;

public record UserRegistrationDTO(
        @NotBlank(message = "Firstname is required")
        @Size(min = 2, max = 25, message = "First name must be between 2 and 25 characters")
        String firstname,
        @NotBlank(message = "Last name is required")
        @Size(min = 2, max = 25, message = "Last name must be between 2 and 25 characters")
        String lastname,
        @NotEmpty(message = "Trainers list is required")
        Set<
                @NotBlank(message = "Role is required")
                @Size(min = 2, max = 15, message = "Role length must be between 2 and 15 characters")
                String> roles
) {
}
