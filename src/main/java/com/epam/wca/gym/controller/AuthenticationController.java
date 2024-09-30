package com.epam.wca.gym.controller;

import com.epam.wca.gym.aop.Logging;
import com.epam.wca.gym.dto.trainee.TraineeRegistrationDTO;
import com.epam.wca.gym.dto.trainer.TrainerRegistrationDTO;
import com.epam.wca.gym.dto.trainer.TrainerSavingDTO;
import com.epam.wca.gym.dto.user.UserAuthenticatedDTO;
import com.epam.wca.gym.entity.Trainee;
import com.epam.wca.gym.entity.Trainer;
import com.epam.wca.gym.entity.TrainingType;
import com.epam.wca.gym.entity.User;
import com.epam.wca.gym.entity.Username;
import com.epam.wca.gym.exception.ValidationException;
import com.epam.wca.gym.repository.impl.UsernameDAO;
import com.epam.wca.gym.service.impl.TraineeService;
import com.epam.wca.gym.service.impl.TrainerService;
import com.epam.wca.gym.service.impl.TrainingTypeService;
import com.epam.wca.gym.service.impl.UserService;
import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/authenticate", consumes = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class AuthenticationController {
    @NonNull
    private UserService userService;
    @NonNull
    private TraineeService traineeService;
    @NonNull
    private TrainerService trainerService;
    @NonNull
    private TrainingTypeService trainingTypeService;
    @NonNull
    private UsernameDAO usernameDAO;

    @GetMapping("/login")
    @Logging
    public ResponseEntity<String> login(
            @RequestParam("username") String username,
            @RequestParam("password") String password
    ) {
        User user = userService.findByUniqueName(username);

        if (user == null || !user.getPassword().equals(password)) {
            return new ResponseEntity<>("Invalid Username or Password", HttpStatus.BAD_REQUEST);
        }

        return ResponseEntity.ok("Login Successful");
    }

    @PostMapping(value = "/register/trainee")
    public ResponseEntity<UserAuthenticatedDTO> registerTrainee(
            @RequestBody @Valid TraineeRegistrationDTO traineeRegistrationDto
    ) {
        Trainee trainee = traineeService.save(traineeRegistrationDto);
        var newUser = new UserAuthenticatedDTO(trainee.getUserName(), trainee.getPassword());

        return new ResponseEntity<>(newUser, HttpStatus.OK);
    }

    @PostMapping(value = "/register/trainer")
    public ResponseEntity<UserAuthenticatedDTO> registerTrainer(
            @RequestBody @Valid TrainerRegistrationDTO trainerRegistrationDTO
    ) {
        TrainingType trainingType = trainingTypeService.findByUniqueName(trainerRegistrationDTO.trainingType());

        if (trainingType == null) {
            throw new ValidationException("Invalid Training Type choice");
        }

        TrainerSavingDTO trainerSavingDTO = new TrainerSavingDTO(trainerRegistrationDTO.firstName(),
                trainerRegistrationDTO.lastName(),
                trainingType);

        Trainer trainer = trainerService.save(trainerSavingDTO);
        var newUser = new UserAuthenticatedDTO(trainer.getUserName(), trainer.getPassword());

        return new ResponseEntity<>(newUser, HttpStatus.OK);
    }

    @GetMapping("/register/username/availability/{baseUsername}")
    public ResponseEntity<Username> findUsernameAvailable(
            @PathVariable("baseUsername") String baseUsername
    ) {
        Username usernameAvailable = usernameDAO.findByUniqueName(baseUsername);

        return new ResponseEntity<>(usernameAvailable, HttpStatus.OK);
    }
}
