package com.epam.wca.gym.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserLoginDTO(
        @NotBlank(message = "Username is required")
        @Size(min = 2, max = 50, message = "Username must be between 2 and 50 characters")
        String username,
        @NotBlank(message = "Password is required")
        @Size(min = 11, max = 11, message = "Password must be exactly 11 characters")
        String password
){
        @Override
        public String toString() {
                return "User[username=" + username + ", password=****]";
        }
}
