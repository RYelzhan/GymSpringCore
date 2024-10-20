package com.epam.wca.gym.controller.impl;

import com.epam.wca.gym.controller.UserController;
import com.epam.wca.gym.dto.user.UserActivationDTO;
import com.epam.wca.gym.dto.user.UserUpdateDTO;
import com.epam.wca.gym.entity.User;
import com.epam.wca.gym.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserControllerImpl implements UserController {
    private final UserService userService;
    @Value("${gym.api.request.attribute.user}")
    private String authenticatedUserRequestAttributeName;

    @Override
    public String getInfo(HttpServletRequest request) {
        var authenticatedUser = (User) request.getAttribute(authenticatedUserRequestAttributeName);

        return authenticatedUser.getUsername();
    }

    @Override
    public String changePassword(
            @RequestBody @Valid UserUpdateDTO userDTO,
            HttpServletRequest request
    ) {
        var authenticatedUser = (User) request.getAttribute(authenticatedUserRequestAttributeName);

        userService.update(authenticatedUser, userDTO);

        return "Password Changed Successfully";
    }

    // query is better suited for queries to database. e.g. Filter, Search
    @Override
    public String activateDeactivate(
            @RequestBody @Valid UserActivationDTO userDTO,
            HttpServletRequest request
    ) {
        var authenticatedUser = (User) request.getAttribute(authenticatedUserRequestAttributeName);

        userService.update(authenticatedUser, userDTO);

        return "Is Active Updated Successfully";
    }
}