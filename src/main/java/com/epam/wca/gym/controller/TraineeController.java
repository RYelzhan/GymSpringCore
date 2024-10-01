package com.epam.wca.gym.controller;

import com.epam.wca.gym.aop.CheckTrainee;
import com.epam.wca.gym.dto.trainee.TraineeSendDTO;
import com.epam.wca.gym.dto.trainee.TraineeTrainersUpdateDTO;
import com.epam.wca.gym.dto.trainee.TraineeUpdateDTO;
import com.epam.wca.gym.dto.trainer.TrainerBasicDTO;
import com.epam.wca.gym.dto.training.TraineeTrainingDTO;
import com.epam.wca.gym.dto.training.TrainingBasicDTO;
import com.epam.wca.gym.dto.user.UsernameGetDTO;
import com.epam.wca.gym.entity.Trainee;
import com.epam.wca.gym.entity.Trainer;
import com.epam.wca.gym.exception.ForbiddenActionException;
import com.epam.wca.gym.service.impl.TraineeService;
import com.epam.wca.gym.service.impl.TrainerService;
import com.epam.wca.gym.util.DTOFactory;
import com.epam.wca.gym.util.Filter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    @GetMapping("/profile/{username}")
    @CheckTrainee
    public ResponseEntity<TraineeSendDTO> getTraineeProfile(
            @PathVariable("username") String username,
            HttpServletRequest request
    ) {
        Trainee authenticatedTrainee = (Trainee) request.getAttribute("authenticatedUser");

        if (!authenticatedTrainee.getUserName().equals(username)) {
            throw new ForbiddenActionException("This is not your profile.");
        }

        return new ResponseEntity<>(DTOFactory.createTraineeSendDTO(authenticatedTrainee), HttpStatus.OK);
    }

    @PutMapping("/profile")
    @CheckTrainee
    public ResponseEntity<TraineeSendDTO> updateTraineeProfile(
            @RequestBody @Valid TraineeUpdateDTO traineeUpdateDTO,
            HttpServletRequest request
    ) {
        Trainee authenticatedTrainee = (Trainee) request.getAttribute("authenticatedUser");

        if (!authenticatedTrainee.getUserName().equals(traineeUpdateDTO.username())) {
            throw new ForbiddenActionException("Can not change username");
        }

        Trainee updatedTrainee = traineeService.update(authenticatedTrainee, traineeUpdateDTO);

        return new ResponseEntity<>(DTOFactory.createTraineeSendDTO(updatedTrainee), HttpStatus.OK);
    }

    @DeleteMapping("/profile")
    @CheckTrainee
    public ResponseEntity<String> deleteTrainee(
            @RequestBody @Valid UsernameGetDTO usernameGetDTO,
            HttpServletRequest request
    ) {
        Trainee authenticatedTrainee = (Trainee) request.getAttribute("authenticatedUser");

        if (!authenticatedTrainee.getUserName().equals(usernameGetDTO.username())) {
            throw new ForbiddenActionException("This is not your profile.");
        }

        traineeService.deleteById(authenticatedTrainee.getId());

        return ResponseEntity.ok(null);
    }

    @GetMapping("/trainers/available")
    @CheckTrainee
    public ResponseEntity<List<TrainerBasicDTO>> getNotAssignedTrainers(
            @RequestParam("username") String username,
            HttpServletRequest request
    ) {
        Trainee authenticatedTrainee = (Trainee) request.getAttribute("authenticatedUser");

        if (!authenticatedTrainee.getUserName().equals(username)) {
            throw new ForbiddenActionException("This is not your account.");
        }

        return new ResponseEntity<>(
                traineeService.getListOfNotAssignedTrainers(authenticatedTrainee),
                HttpStatus.OK
        );
    }

    // TODO: delegate all this work to service layer
    @PutMapping("/trainers/add")
    @CheckTrainee
    public ResponseEntity<List<TrainerBasicDTO>> updateTrainerList(
            @RequestBody @Valid TraineeTrainersUpdateDTO traineeTrainersUpdateDTO,
            HttpServletRequest request
            ) {
        Trainee authenticatedTrainee = (Trainee) request.getAttribute("authenticatedUser");

        if (!authenticatedTrainee.getUserName().equals(traineeTrainersUpdateDTO.username())) {
            throw new ForbiddenActionException("This is not your account.");
        }

        List<Trainer> trainerList = new ArrayList<>();
        traineeTrainersUpdateDTO.trainerUsernames()
                .forEach(trainerUsername -> {
                Trainer trainer = trainerService.findByUniqueName(trainerUsername);

                if (trainer == null) {
                    throw new ForbiddenActionException("No Trainer Found with Username " +
                            trainerUsername +
                            ". No Trainers Added");
                }

                trainerList.add(trainer);
        });

        authenticatedTrainee.addTrainers(trainerList);
        traineeService.update(authenticatedTrainee);

        List<TrainerBasicDTO> addedTrainers = new ArrayList<>();
        trainerList.forEach((trainer) -> {
            addedTrainers.add(DTOFactory.createBasicTrainerDTO(trainer));
        });

        return new ResponseEntity<>(addedTrainers, HttpStatus.OK);
    }

    @GetMapping("/trainings/filter")
    @CheckTrainee
    public ResponseEntity<List<TrainingBasicDTO>> getTraineeTrainingsList(
            @RequestBody @Valid TraineeTrainingDTO traineeTrainingDTO,
            HttpServletRequest request
            ) {
        Trainee authenticatedTrainee = (Trainee) request.getAttribute("authenticatedUser");

        if (!authenticatedTrainee.getUserName().equals(traineeTrainingDTO.username())) {
            throw new ForbiddenActionException("This is not your account.");
        }

        List<TrainingBasicDTO> result = Filter.filterTraineeTrainings(authenticatedTrainee.getTrainings(),
                        traineeTrainingDTO)
                .stream()
                .map(DTOFactory::createTraineeBasicTrainingDTO)
                .collect(Collectors.toList());

        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
