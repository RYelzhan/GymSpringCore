package com.epam.wca.gym.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserUpdateDTO(
        @NotBlank(message = "New password is required")
        @Size(min = 11, max = 11, message = "Password must be exactly 11 characters")
        String newPassword
) {
}
