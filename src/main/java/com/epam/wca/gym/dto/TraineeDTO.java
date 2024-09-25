package com.epam.wca.gym.dto;

import com.epam.wca.gym.util.AppConstants;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

import java.time.ZonedDateTime;

// TODO: Hibernate Validation
public record TraineeDTO(
        @NotEmpty(message = "First name is required")
        @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
        String firstName,
        @NotEmpty(message = "Last name is required")
        @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters")
        String lastName,
        @Past(message = "Date of birth must be in the past")
        @JsonFormat(pattern = AppConstants.DEFAULT_DATE_FORMAT)
        ZonedDateTime dateOfBirth,
        @Size(min = 5, max = 100, message = "Address must be between 5 and 100 characters")
        String address
) {
}
