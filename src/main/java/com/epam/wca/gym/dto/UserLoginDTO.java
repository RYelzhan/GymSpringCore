package com.epam.wca.gym.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserLoginDTO(
        @NotBlank(message = "Username is required")
        @Size(min = 2, max = 50, message = "Username must be between 2 and 50 characters")
        String username,
        @NotBlank(message = "Password is required")
        @Size(min = 10, max = 10, message = "Password must be exactly 10 characters")
        String password
){
}
