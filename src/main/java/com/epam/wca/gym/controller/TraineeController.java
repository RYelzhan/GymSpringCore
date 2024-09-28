package com.epam.wca.gym.controller;

import com.epam.wca.gym.dto.TraineeSendDTO;
import com.epam.wca.gym.dto.TraineeUpdateDTO;
import com.epam.wca.gym.dto.TrainerBasicDTO;
import com.epam.wca.gym.dto.UsernameGetDTO;
import com.epam.wca.gym.entity.Trainee;
import com.epam.wca.gym.entity.User;
import com.epam.wca.gym.service.impl.TraineeService;
import com.epam.wca.gym.util.DTOFactory;
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

@RestController
@RequestMapping(value = "/user/trainee", consumes = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class TraineeController {
    @NonNull
    private TraineeService traineeService;

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

    @GetMapping("/available/trainers")
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
}
