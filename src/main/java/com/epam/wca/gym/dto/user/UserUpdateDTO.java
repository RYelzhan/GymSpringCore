package com.epam.wca.gym.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserUpdateDTO(
        @NotBlank(message = "Username is required")
        @Size(min = 2, max = 50, message = "Username must be between 2 and 50 characters")
        String username,
        @NotBlank(message = "Old password is required")
        @Size(min = 10, max = 10, message = "Password must be exactly 10 characters")
        String oldPassword,
        @NotBlank(message = "New password is required")
        @Size(min = 10, max = 10, message = "Password must be exactly 10 characters")
        String newPassword
) {
}
