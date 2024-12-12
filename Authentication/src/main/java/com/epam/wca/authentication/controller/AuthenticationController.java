package com.epam.wca.authentication.controller;

import com.epam.wca.authentication.dto.UserActivationDTO;
import com.epam.wca.authentication.dto.UserUpdateDTO;
import com.epam.wca.authentication.entity.User;
import com.epam.wca.common.gymcommon.auth_dto.UserAuthenticatedDTO;
import com.epam.wca.common.gymcommon.auth_dto.UserRegistrationDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

@RequestMapping
public interface AuthenticationController {

    @GetMapping(value = "/authenticate", consumes = MediaType.ALL_VALUE)
    String authenticate(@AuthenticationPrincipal User user);

    @PostMapping(value = "/login", consumes = MediaType.ALL_VALUE)
    String login(@AuthenticationPrincipal User user);

    @PostMapping(value = "/logout", consumes = MediaType.ALL_VALUE)
    String logout();

    @DeleteMapping(value = "/delete", consumes = MediaType.ALL_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void delete(@AuthenticationPrincipal User user);

    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    UserAuthenticatedDTO register(UserRegistrationDTO registrationDTO);

    @PutMapping(value = "/password", consumes = MediaType.APPLICATION_JSON_VALUE)
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
