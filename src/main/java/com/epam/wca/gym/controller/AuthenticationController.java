package com.epam.wca.gym.controller;

import com.epam.wca.gym.aop.Logging;
import com.epam.wca.gym.dto.trainee.TraineeRegistrationDTO;
import com.epam.wca.gym.dto.trainer.TrainerRegistrationDTO;
import com.epam.wca.gym.dto.user.UserAuthenticatedDTO;
import com.epam.wca.gym.dto.user.UserLoginDTO;
import com.epam.wca.gym.entity.User;
import com.epam.wca.gym.entity.Username;
import com.epam.wca.gym.exception.BadControllerRequestException;
import com.epam.wca.gym.repository.deprecated.impl.UsernameDAO;
import com.epam.wca.gym.service.TraineeService;
import com.epam.wca.gym.service.TrainerService;
import com.epam.wca.gym.service.deprecated.UserServiceOld;
import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/authenticate", consumes = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class AuthenticationController {
    // TODO: make fields final. (everywhere)
    @NonNull
    private UserServiceOld userServiceOld;
    private final TraineeService traineeService;
    private final TrainerService trainerService;
    @NonNull
    private UsernameDAO usernameDAO;

    @PostMapping("/login")
    @Logging
    public String login(@RequestBody @Valid UserLoginDTO loginDTO) {
        User user = userServiceOld.findByUniqueName(loginDTO.username());

        if (user == null || !user.getPassword().equals(loginDTO.password())) {
             throw new BadControllerRequestException("Invalid Username or Password");
        }

        return "Login Successful";
    }

    @PostMapping(value = "/register/trainee")
    @ResponseStatus(HttpStatus.CREATED)
    public UserAuthenticatedDTO registerTrainee(@RequestBody @Valid TraineeRegistrationDTO traineeDTO) {
        return traineeService.save(traineeDTO);
    }

    @PostMapping(value = "/register/trainer")
    @ResponseStatus(HttpStatus.CREATED)
    public UserAuthenticatedDTO registerTrainer(@RequestBody @Valid TrainerRegistrationDTO trainerDTO) {
        return trainerService.save(trainerDTO);
    }

    @GetMapping("/register/username/availability/{baseUsername}")
    public Username findUsernameAvailable(@PathVariable("baseUsername") String baseUsername) {
        return usernameDAO.findByUniqueName(baseUsername);
    }
}
