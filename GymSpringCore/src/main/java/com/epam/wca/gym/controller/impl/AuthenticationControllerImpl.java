package com.epam.wca.gym.controller.impl;

import com.epam.wca.common.gymcommon.aop.Logging;
import com.epam.wca.gym.controller.documentation.AuthenticationControllerDocumentation;
import com.epam.wca.gym.dto.trainee.TraineeRegistrationDTO;
import com.epam.wca.gym.dto.trainer.TrainerRegistrationDTO;
import com.epam.wca.gym.dto.user.UserAuthenticatedDTO;
import com.epam.wca.gym.entity.User;
import com.epam.wca.gym.service.AuthService;
import com.epam.wca.gym.service.TraineeService;
import com.epam.wca.gym.service.TrainerService;
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
    private final TraineeService traineeService;
    private final TrainerService trainerService;

    @Override
    public String login(@AuthenticationPrincipal User user) {
        String token = authService.generateToken(user);

        return "Login Successful. Token: %s".formatted(token);
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
    @Logging
    public UserAuthenticatedDTO registerTrainee(@RequestBody @Valid TraineeRegistrationDTO traineeDTO) {
        return traineeService.save(traineeDTO);
    }

    @Override
    @Logging
    public UserAuthenticatedDTO registerTrainer(@RequestBody @Valid TrainerRegistrationDTO trainerDTO) {
        return trainerService.save(trainerDTO);
    }
}
