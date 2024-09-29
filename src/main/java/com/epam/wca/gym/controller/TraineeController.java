package com.epam.wca.gym.controller;

import com.epam.wca.gym.dto.trainee.TraineeSendDTO;
import com.epam.wca.gym.dto.trainee.TraineeTrainersUpdateDTO;
import com.epam.wca.gym.dto.trainee.TraineeUpdateDTO;
import com.epam.wca.gym.dto.trainer.TrainerBasicDTO;
import com.epam.wca.gym.dto.training.TraineeTrainingDTO;
import com.epam.wca.gym.dto.training.TrainingBasicDTO;
import com.epam.wca.gym.dto.user.UsernameGetDTO;
import com.epam.wca.gym.entity.Trainee;
import com.epam.wca.gym.entity.Trainer;
import com.epam.wca.gym.entity.User;
import com.epam.wca.gym.service.impl.TraineeService;
import com.epam.wca.gym.service.impl.TrainerService;
import com.epam.wca.gym.util.DTOFactory;
import com.epam.wca.gym.util.Filter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/user/trainee", consumes = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class TraineeController {
    @NonNull
    private TraineeService traineeService;
    @NonNull
    private TrainerService trainerService;

    @GetMapping("/profile")
    public ResponseEntity<TraineeSendDTO> getTraineeProfile(
            @RequestBody @Valid UsernameGetDTO usernameGetDTO,
            HttpServletRequest request
    ) {
        User authenticatedUser = (User) request.getAttribute("authenticatedUser");

        if (!authenticatedUser.getUserName().equals(usernameGetDTO.username()) ||
                !(authenticatedUser instanceof Trainee trainee)) {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }

        return new ResponseEntity<>(DTOFactory.createTraineeSendDTO(trainee), HttpStatus.OK);
    }

    @PutMapping("/profile")
    public ResponseEntity<TraineeSendDTO> updateTraineeProfile(
            @RequestBody @Valid TraineeUpdateDTO traineeUpdateDTO,
            HttpServletRequest request
    ) {
        User authenticatedUser = (User) request.getAttribute("authenticatedUser");

        if (!(authenticatedUser instanceof Trainee trainee)) {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }

        if (!authenticatedUser.getUserName().equals(traineeUpdateDTO.username())) {
            throw new com.epam.wca.gym.exception.ValidationException("Can not change username");
        }

        trainee = traineeService.update(trainee, traineeUpdateDTO);

        return new ResponseEntity<>(DTOFactory.createTraineeSendDTO(trainee), HttpStatus.OK);
    }

    @DeleteMapping("/profile")
    public ResponseEntity<String> deleteTrainee(
            @RequestBody @Valid UsernameGetDTO usernameGetDTO,
            HttpServletRequest request
    ) {
        User authenticatedUser = (User) request.getAttribute("authenticatedUser");

        if (!(authenticatedUser instanceof Trainee) ||
                !authenticatedUser.getUserName().equals(usernameGetDTO.username())) {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }

        traineeService.deleteById(authenticatedUser.getId());

        return ResponseEntity.ok(null);
    }

    @GetMapping("/trainers/available")
    public ResponseEntity<List<TrainerBasicDTO>> getNotAssignedTrainers(
            @RequestBody @Valid UsernameGetDTO usernameGetDTO,
            HttpServletRequest request
    ) {
        User authenticatedUser = (User) request.getAttribute("authenticatedUser");

        if (!(authenticatedUser instanceof Trainee trainee) ||
                !authenticatedUser.getUserName().equals(usernameGetDTO.username())) {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }

        return new ResponseEntity<>(traineeService.getListOfNotAssignedTrainers(trainee), HttpStatus.OK);
    }

    @PutMapping("/trainers/add")
    public ResponseEntity<List<TrainerBasicDTO>> updateTrainerList(
            @RequestBody @Valid TraineeTrainersUpdateDTO traineeTrainersUpdateDTO,
            HttpServletRequest request
            ) {
        User authenticatedUser = (User) request.getAttribute("authenticatedUser");

        if (!(authenticatedUser instanceof Trainee trainee) ||
                !authenticatedUser.getUserName().equals(traineeTrainersUpdateDTO.username())) {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }

        List<Trainer> trainerList = new ArrayList<>();
        traineeTrainersUpdateDTO.trainerUsernames()
                .forEach(trainerUsername -> {
                Trainer trainer = trainerService.findByUniqueName(trainerUsername);

                if (trainer == null) {
                    throw new ValidationException("No Trainer Found with Username " +
                            trainerUsername +
                            ". No Trainers Added");
                }

                trainerList.add(trainer);
        });

        trainee.addTrainers(trainerList);
        traineeService.update(trainee);

        List<TrainerBasicDTO> addedTrainers = new ArrayList<>();
        trainerList.forEach((trainer) -> {
            addedTrainers.add(DTOFactory.createBasicTrainerDTO(trainer));
        });

        return new ResponseEntity<>(addedTrainers, HttpStatus.OK);
    }

    @GetMapping("/trainings/filter")
    public ResponseEntity<List<TrainingBasicDTO>> getTraineeTrainingsList(
            @RequestBody @Valid TraineeTrainingDTO traineeTrainingDTO,
            HttpServletRequest request
            ) {
        User authenticatedUser = (User) request.getAttribute("authenticatedUser");

        if (!(authenticatedUser instanceof Trainee trainee) ||
                !authenticatedUser.getUserName().equals(traineeTrainingDTO.username())) {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }

        List<TrainingBasicDTO> result = Filter.filterTraineeTrainings(trainee.getTrainings(),
                        traineeTrainingDTO)
                .stream()
                .map(DTOFactory::createTraineeBasicTrainingDTO)
                .collect(Collectors.toList());

        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
