package com.epam.wca.gym.controller;

import com.epam.wca.gym.dto.user.UserActivationDTO;
import com.epam.wca.gym.dto.user.UserUpdateDTO;
import com.epam.wca.gym.entity.User;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping(value = "/users", consumes = MediaType.APPLICATION_JSON_VALUE)
public interface UserController {
    @GetMapping(value = "/info", consumes = MediaType.ALL_VALUE)
    String getInfo(@AuthenticationPrincipal User authenticatedUser);

    @PutMapping(value = "/password")
    String changePassword(
            @RequestBody @Valid UserUpdateDTO userDTO,
            @AuthenticationPrincipal User authenticatedUser
    );

    @PatchMapping(value = "/active")
    String activateDeactivate(
            @RequestBody @Valid UserActivationDTO userDTO,
            @AuthenticationPrincipal User authenticatedUser
    );
}