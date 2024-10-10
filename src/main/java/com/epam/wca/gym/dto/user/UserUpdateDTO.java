package com.epam.wca.gym.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserUpdateDTO(
        @NotBlank(message = "New password is required")
        @Size(min = 10, max = 10, message = "Password must be exactly 10 characters")
        String newPassword
) {
}
