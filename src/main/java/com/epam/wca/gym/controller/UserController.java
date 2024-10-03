package com.epam.wca.gym.controller;

import com.epam.wca.gym.dto.user.UserActivationDTO;
import com.epam.wca.gym.dto.user.UserUpdateDTO;
import com.epam.wca.gym.entity.User;
import com.epam.wca.gym.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/info")
    public String getUserInfo(HttpServletRequest request) {
        var authenticatedUser = (User) request.getAttribute("authenticatedUser");

        return authenticatedUser.getUserName();
    }

    @PutMapping(value = "/change/password", consumes = MediaType.APPLICATION_JSON_VALUE)
    public String changeUserPassword(
            @RequestBody @Valid UserUpdateDTO userDTO,
            HttpServletRequest request
    ) {
        var authenticatedUser = (User) request.getAttribute("authenticatedUser");

        userService.update(authenticatedUser, userDTO);

        return "Password Changed Successfully";
    }

    @PatchMapping(value = "/change/active", consumes = MediaType.APPLICATION_JSON_VALUE)
    public String activateDeactivateUser(
            @RequestBody @Valid UserActivationDTO userDTO,
            HttpServletRequest request
            ) {
        var authenticatedUser = (User) request.getAttribute("authenticatedUser");

        userService.update(authenticatedUser, userDTO);

        return "Is Active Updated Successfully";
    }
}
