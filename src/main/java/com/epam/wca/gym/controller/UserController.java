package com.epam.wca.gym.controller;

import com.epam.wca.gym.dto.training.TrainingGettingDTO;
import com.epam.wca.gym.dto.user.UserUpdateDTO;
import com.epam.wca.gym.entity.Trainee;
import com.epam.wca.gym.entity.Trainer;
import com.epam.wca.gym.entity.User;
import com.epam.wca.gym.exception.ValidationException;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/user", consumes = MediaType.APPLICATION_JSON_VALUE)
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
    public ResponseEntity<String> getUserInfo(HttpServletRequest request) {
        User authenticatedUser = (User) request.getAttribute("authenticatedUser");

        return ResponseEntity.ok(authenticatedUser.getUserName());
    }

    @PutMapping("/change/password")
    public ResponseEntity<String> changeUserPassword(
            @RequestBody @Valid UserUpdateDTO userUpdateDTO,
            HttpServletRequest request
    ) {
        User authenticatedUser = (User) request.getAttribute("authenticatedUser");

        if (!authenticatedUser.getUserName().equals(userUpdateDTO.username()) ||
            !authenticatedUser.getPassword().equals(userUpdateDTO.oldPassword())) {
            // authenticated as other user and trying to change password details of other user
            return new ResponseEntity<>("Not authorised", HttpStatus.UNAUTHORIZED);
        }

        authenticatedUser.setPassword(userUpdateDTO.newPassword());
        userService.update(authenticatedUser);

        return new ResponseEntity<>("Password changed successfully", HttpStatus.OK);
    }

    @PostMapping("/create/training")
    public ResponseEntity<String> createNewTraining(
            @RequestBody @Valid TrainingGettingDTO trainingGettingDTO,
            HttpServletRequest request
            ) {
        User authenticatedUser = (User) request.getAttribute("authenticatedUser");

        if (authenticatedUser instanceof Trainee trainee) {
            if (authenticatedUser.getUserName().equals(trainingGettingDTO.traineeUsername())) {
                Trainer trainer = trainerService.findByUniqueName(trainingGettingDTO.trainerUsername());
                if (trainer != null) {
                    trainingService.save(TrainingFactory.createTraining(trainingGettingDTO, trainee, trainer));
                    return ResponseEntity.ok(null);
                }
                throw new ValidationException("No trainer found with such username");
            }
        } else if (authenticatedUser instanceof Trainer trainer) {
            if (authenticatedUser.getUserName().equals(trainingGettingDTO.trainerUsername())) {
                Trainee trainee = traineeService.findByUniqueName(trainingGettingDTO.traineeUsername());
                if (trainee != null) {
                    trainingService.save(TrainingFactory.createTraining(trainingGettingDTO, trainee, trainer));
                    return ResponseEntity.ok(null);
                }
                throw new ValidationException("No trainee found with such username");
            }
        }

        return new ResponseEntity<>("Not authorised", HttpStatus.UNAUTHORIZED);
    }
}