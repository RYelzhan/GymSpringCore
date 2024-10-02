package com.epam.wca.gym.controller;

import com.epam.wca.gym.dto.training.TrainingGettingDTO;
import com.epam.wca.gym.dto.user.UserActivationDTO;
import com.epam.wca.gym.dto.user.UserUpdateDTO;
import com.epam.wca.gym.entity.Trainee;
import com.epam.wca.gym.entity.Trainer;
import com.epam.wca.gym.entity.User;
import com.epam.wca.gym.exception.ControllerValidationException;
import com.epam.wca.gym.exception.ForbiddenActionException;
import com.epam.wca.gym.exception.InternalErrorException;
import com.epam.wca.gym.service.impl.TraineeService;
import com.epam.wca.gym.service.impl.TrainerService;
import com.epam.wca.gym.service.impl.TrainingService;
import com.epam.wca.gym.service.impl.UserService;
import com.epam.wca.gym.util.TrainingFactory;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/user")
@RequiredArgsConstructor
public class UserController {
    @NonNull
    private UserService userService;
    @NonNull
    private TrainerService trainerService;
    @NonNull
    private TraineeService traineeService;
    @NonNull
    private TrainingService trainingService;

    @GetMapping("/info")
    public String getUserInfo(HttpServletRequest request) {
        var authenticatedUser = (User) request.getAttribute("authenticatedUser");

        return authenticatedUser.getUserName();
    }

    @PutMapping(value = "/change/password", consumes = MediaType.APPLICATION_JSON_VALUE)
    public String changeUserPassword(
            @RequestBody @Valid UserUpdateDTO userUpdateDTO,
            HttpServletRequest request
    ) {
        var authenticatedUser = (User) request.getAttribute("authenticatedUser");

        if (!authenticatedUser.getPassword().equals(userUpdateDTO.oldPassword())) {
            // authenticated as other user and trying to change password details of other user
            throw new ForbiddenActionException("Not Correct Credentials");
        }

        authenticatedUser.setPassword(userUpdateDTO.newPassword());
        userService.update(authenticatedUser);

        return "Password Changed Successfully";
    }

    @PostMapping(value = "/create/training", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public String createNewTraining(
            @RequestBody @Valid TrainingGettingDTO trainingGettingDTO,
            HttpServletRequest request
            ) {
        User authenticatedUser = (User) request.getAttribute("authenticatedUser");

        if (authenticatedUser instanceof Trainee trainee) {
            Trainer trainer = trainerService.findByUniqueName(trainingGettingDTO.trainerUsername());

            if (trainer != null) {
                trainingService.save(TrainingFactory.createTraining(trainingGettingDTO, trainee, trainer));
                return "Training Created Successfully";
            }

            throw new ControllerValidationException("No Trainer Found With Username"
                    + trainingGettingDTO.trainerUsername());
        } else if (authenticatedUser instanceof Trainer trainer) {
            Trainee trainee = traineeService.findByUniqueName(trainingGettingDTO.traineeUsername());

            if (trainee != null) {
                trainingService.save(TrainingFactory.createTraining(trainingGettingDTO, trainee, trainer));
                return "Training Created Successfully";
            }

            throw new ControllerValidationException("No Trainee Found With Username"
                    + trainingGettingDTO.traineeUsername());
        }

        throw new InternalErrorException("User which is does not have a role.");
    }

    @PatchMapping(value = "/change/active", consumes = MediaType.APPLICATION_JSON_VALUE)
    public String activateDeactivateUser(
            @RequestBody @Valid UserActivationDTO userActivationDTO,
            HttpServletRequest request
            ) {
        var authenticatedUser = (User) request.getAttribute("authenticatedUser");

        authenticatedUser.setActive(userActivationDTO.isActive());
        userService.update(authenticatedUser);

        return "Is Active Updated Successfully";
    }
}
