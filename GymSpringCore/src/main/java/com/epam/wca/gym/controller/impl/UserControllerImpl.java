package com.epam.wca.gym.controller.impl;

import com.epam.wca.gym.controller.documentation.UserControllerDocumentation;
import com.epam.wca.gym.dto.user.UserActivationDTO;
import com.epam.wca.gym.dto.user.UserUpdateDTO;
import com.epam.wca.gym.entity.User;
import com.epam.wca.gym.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserControllerImpl implements UserControllerDocumentation {
    private final UserService userService;

    @Override
    public String getInfo(@AuthenticationPrincipal User authenticatedUser) {
        return authenticatedUser.getUsername();
    }

    @Override
    public String changePassword(
            @RequestBody @Valid UserUpdateDTO userDTO,
            @AuthenticationPrincipal User authenticatedUser
    ) {
        userService.update(authenticatedUser, userDTO);

        return "Password Changed Successfully";
    }

    // query is better suited for queries to database. e.g. Filter, Search
    @Override
    public String activateDeactivate(
            @RequestBody @Valid UserActivationDTO userDTO,
            @AuthenticationPrincipal User authenticatedUser
    ) {
        userService.update(authenticatedUser, userDTO);

        return "Is Active Updated Successfully";
    }
}