package com.epam.wca.gym.dto;

import com.epam.wca.gym.util.AppConstants;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

import java.time.ZonedDateTime;

public record TraineeUpdateDTO(
        @NotBlank(message = "First name is required")
        @Size(min = 2, max = 50, message = "Username must be between 2 and 50 characters")
        String username,
        @NotBlank(message = "First name is required")
        @Size(min = 2, max = 25, message = "First name must be between 2 and 25 characters")
        String firstName,
        @NotBlank(message = "Last name is required")
        @Size(min = 2, max = 25, message = "Last name must be between 2 and 25 characters")
        String lastName,
        @Past(message = "Date of birth must be in the past")
        @JsonFormat(pattern = AppConstants.DEFAULT_DATE_FORMAT)
        ZonedDateTime dateOfBirth,
        @Size(min = 5, max = 50, message = "Address must be between 5 and 50 characters")
        String address,
        @NotNull(message = "Is active is required")
        Boolean isActive
) {
}
