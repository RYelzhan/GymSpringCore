package com.epam.wca.gym.controller.impl;

import com.epam.wca.common.gymcommon.aop.Logging;
import com.epam.wca.gym.aop.argument.InsertUser;
import com.epam.wca.gym.controller.documentation.UserControllerDocumentation;
import com.epam.wca.gym.entity.User;
import com.epam.wca.gym.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserControllerImpl implements UserControllerDocumentation {
    private final UserService userService;

    @Override
    @Logging
    public String getInfo(@InsertUser User authenticatedUser) {
        return authenticatedUser.getUsername();
    }
}