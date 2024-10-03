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
import com.epam.wca.gym.service.deprecated.TraineeServiceOld;
import com.epam.wca.gym.service.deprecated.TrainerServiceOld;
import com.epam.wca.gym.service.deprecated.TrainingServiceOld;
import com.epam.wca.gym.service.deprecated.UserServiceOld;
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
    private UserServiceOld userServiceOld;
    @NonNull
    private TrainerServiceOld trainerServiceOld;
    @NonNull
    private TraineeServiceOld traineeServiceOld;
    @NonNull
    private TrainingServiceOld trainingServiceOld;

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
        userServiceOld.update(authenticatedUser);

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
            Trainer trainer = trainerServiceOld.findByUniqueName(trainingGettingDTO.trainerUsername());

            if (trainer != null) {
                trainingServiceOld.save(TrainingFactory.createTraining(trainingGettingDTO, trainee, trainer));
                return "Training Created Successfully";
            }

            throw new ControllerValidationException("No Trainer Found With Username"
                    + trainingGettingDTO.trainerUsername());
        } else if (authenticatedUser instanceof Trainer trainer) {
            Trainee trainee = traineeServiceOld.findByUniqueName(trainingGettingDTO.traineeUsername());

            if (trainee != null) {
                trainingServiceOld.save(TrainingFactory.createTraining(trainingGettingDTO, trainee, trainer));
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
        userServiceOld.update(authenticatedUser);

        return "Is Active Updated Successfully";
    }
}
