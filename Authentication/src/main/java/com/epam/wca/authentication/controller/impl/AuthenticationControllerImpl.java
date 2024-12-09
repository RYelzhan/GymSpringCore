package com.epam.wca.authentication.controller.impl;

import com.epam.wca.authentication.controller.documentation.AuthenticationControllerDocumentation;
import com.epam.wca.authentication.dto.UserUpdateDTO;
import com.epam.wca.authentication.entity.User;
import com.epam.wca.authentication.service.AuthService;
import com.epam.wca.authentication.service.impl.UserService;
import com.epam.wca.common.gymcommon.aop.Logging;
import com.epam.wca.common.gymcommon.auth_dto.UserRegistrationDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthenticationControllerImpl implements
        AuthenticationControllerDocumentation {
    private final AuthService authService;
    private final UserService userService;

    @Override
    @Logging
    public Long authenticate(@AuthenticationPrincipal User user) {
        return user.getId();
    }

    @Override
    @Logging
    public String login(@AuthenticationPrincipal User user) {
        String token = authService.generateToken(user);

        return "Login Successful. Token: %s".formatted(token);
    }

    @Override
    @Logging
    public void delete(@AuthenticationPrincipal User user) {
        userService.delete(user);
    }

    @Override
    @Logging
    public String logout() {
        // not sure if there is need as both BasicAuth and JwtToken are stateless
        SecurityContextHolder.clearContext();

        // TODO: refresh token deleting logic

        return "Logout Successful";
    }

    @Override
    public String register(UserRegistrationDTO registrationDTO) {
        return null;
    }

    @Override
    @Logging
    public String changePassword(
            @RequestBody @Valid UserUpdateDTO userDTO,
            @AuthenticationPrincipal User authenticatedUser
    ) {
        userService.update(authenticatedUser, userDTO);

        return "Password Changed Successfully";
    }
}
