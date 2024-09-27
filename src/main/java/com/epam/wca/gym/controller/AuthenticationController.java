package com.epam.wca.gym.controller;

import com.epam.wca.gym.aop.Validate;
import com.epam.wca.gym.dto.AuthenticatedUserDTO;
import com.epam.wca.gym.dto.TraineeGettingDTO;
import com.epam.wca.gym.dto.TrainerGettingDTO;
import com.epam.wca.gym.dto.TrainerSavingDTO;
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
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class AuthenticationController {
    @NonNull
    private TraineeService traineeService;
    @NonNull
    private TrainerService trainerService;
    @NonNull
    private TrainingTypeService trainingTypeService;
    @NonNull
    private UsernameDAO usernameDAO;

    @GetMapping("/info")
    public ResponseEntity<String> getUserInfo(HttpServletRequest request) {
        User authenticatedUser = (User) request.getAttribute("authenticatedUser");

        return ResponseEntity.ok(authenticatedUser.getUserName());
    }

    @PostMapping(value = "/register/trainee")
    @Validate
    public ResponseEntity<AuthenticatedUserDTO> registerTrainee(@Valid @RequestBody TraineeGettingDTO traineeGettingDto,
                                                                BindingResult bindingResult /* used for aspect */) {
        Trainee trainee = traineeService.save(traineeGettingDto);
        var newUser = new AuthenticatedUserDTO(trainee.getUserName(), trainee.getPassword());

        return new ResponseEntity<>(newUser, HttpStatus.OK);
    }

    @PostMapping(value = "/register/trainer")
    @Validate
    public ResponseEntity<AuthenticatedUserDTO> registerTrainer(@Valid @RequestBody TrainerGettingDTO trainerGettingDTO,
                                                                BindingResult bindingResult /* used for aspect */) {
        TrainingType trainingType = trainingTypeService.findByUniqueName(trainerGettingDTO.trainingType());

        if (trainingType == null) {
            Map<String, String> error = new HashMap<>();
            error.put("trainingType", "Invalid Training Type choice");
            throw new ValidationException(error);
        }

        TrainerSavingDTO trainerSavingDTO = new TrainerSavingDTO(trainerGettingDTO.firstName(),
                trainerGettingDTO.lastName(),
                trainingType);

        Trainer trainer = trainerService.save(trainerSavingDTO);
        var newUser = new AuthenticatedUserDTO(trainer.getUserName(), trainer.getPassword());

        return new ResponseEntity<>(newUser, HttpStatus.OK);
    }

    @GetMapping("/register/username/availability/{baseUsername}")
    public ResponseEntity<Username> findUsernameAvailable(@PathVariable("baseUsername") String baseUsername) {
        Username usernameAvailable = usernameDAO.findByUniqueName(baseUsername);

        return new ResponseEntity<>(usernameAvailable, HttpStatus.OK);
    }
}
