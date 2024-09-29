package com.epam.wca.gym.controller;

import com.epam.wca.gym.dto.trainer.TrainerSendDTO;
import com.epam.wca.gym.dto.trainer.TrainerUpdateDTO;
import com.epam.wca.gym.dto.training.TrainerTrainingDTO;
import com.epam.wca.gym.dto.training.TrainingBasicDTO;
import com.epam.wca.gym.dto.user.UsernameGetDTO;
import com.epam.wca.gym.entity.Trainer;
import com.epam.wca.gym.entity.TrainingType;
import com.epam.wca.gym.entity.User;
import com.epam.wca.gym.exception.ValidationException;
import com.epam.wca.gym.service.impl.TrainerService;
import com.epam.wca.gym.service.impl.TrainingTypeService;
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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "user/trainer", consumes = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class TrainerController {
    @NonNull
    private TrainerService trainerService;
    @NonNull
    private TrainingTypeService trainingTypeService;

    @GetMapping("/profile")
    public ResponseEntity<TrainerSendDTO> getTrainerProfile(
            @RequestBody @Valid UsernameGetDTO usernameGetDTO,
            HttpServletRequest request
    ) {
        User authenticatedUser = (User) request.getAttribute("authenticatedUser");

        if (!authenticatedUser.getUserName().equals(usernameGetDTO.username()) ||
                !(authenticatedUser instanceof Trainer trainer)) {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }

        return new ResponseEntity<>(DTOFactory.createTrainerSendDTO(trainer), HttpStatus.OK);
    }

    @PutMapping("/profile")
    public ResponseEntity<TrainerSendDTO> updateTrainerProfile(
            @RequestBody @Valid TrainerUpdateDTO trainerUpdateDTO,
            HttpServletRequest request
    ) {
        TrainingType trainingType = trainingTypeService.findByUniqueName(trainerUpdateDTO.trainingType());

        if (trainingType == null) {
            throw new ValidationException("Invalid Training Type choice");
        }

        User authenticatedUser = (User) request.getAttribute("authenticatedUser");

        if (!(authenticatedUser instanceof Trainer trainer)) {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }

        if (!authenticatedUser.getUserName().equals(trainerUpdateDTO.username())) {
            throw new com.epam.wca.gym.exception.ValidationException("Can not change username");
        }

        trainer = trainerService.update(trainer, trainerUpdateDTO, trainingType);

        return new ResponseEntity<>(DTOFactory.createTrainerSendDTO(trainer), HttpStatus.OK);
    }

    @DeleteMapping("/profile")
    public ResponseEntity<String> deleteTrainee(
            @RequestBody @Valid UsernameGetDTO usernameGetDTO,
            HttpServletRequest request
    ) {
        User authenticatedUser = (User) request.getAttribute("authenticatedUser");

        if (!(authenticatedUser instanceof Trainer) ||
                !authenticatedUser.getUserName().equals(usernameGetDTO.username())) {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }

        trainerService.deleteById(authenticatedUser.getId());

        return ResponseEntity.ok("Trainee Profile Deleted Successfully");
    }

    @GetMapping("/trainings/filter")
    public ResponseEntity<List<TrainingBasicDTO>> getTraineeTrainingsList(
            @RequestBody @Valid TrainerTrainingDTO trainerTrainingDTO,
            HttpServletRequest request
    ) {
        User authenticatedUser = (User) request.getAttribute("authenticatedUser");

        if (!(authenticatedUser instanceof Trainer trainer) ||
                !authenticatedUser.getUserName().equals(trainerTrainingDTO.username())) {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }

        List<TrainingBasicDTO> result = Filter.filterTrainerTrainings(trainer.getTrainings(),
                        trainerTrainingDTO)
                .stream()
                .map(DTOFactory::createTrainerBasicTrainingDTO)
                .collect(Collectors.toList());

        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
