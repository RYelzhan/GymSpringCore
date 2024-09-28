package com.epam.wca.gym.controller;

import com.epam.wca.gym.aop.Validate;
import com.epam.wca.gym.dto.TraineeSendDTO;
import com.epam.wca.gym.dto.TraineeUpdateDTO;
import com.epam.wca.gym.dto.UsernameGetDTO;
import com.epam.wca.gym.entity.Trainee;
import com.epam.wca.gym.entity.User;
import com.epam.wca.gym.service.impl.TraineeService;
import com.epam.wca.gym.util.DTOFactory;
import jakarta.servlet.http.HttpServletRequest;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/user/trainee", consumes = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class TraineeController {
    @NonNull
    private TraineeService traineeService;

    @GetMapping("/profile")
    @Validate
    public ResponseEntity<TraineeSendDTO> getTraineeProfile(@RequestBody UsernameGetDTO usernameGetDTO,
                                                            HttpServletRequest request,
                                                            BindingResult bindingResult /* used for aspect */) {
        User authenticatedUser = (User) request.getAttribute("authenticatedUser");

        if (!authenticatedUser.getUserName().equals(usernameGetDTO.username()) ||
                !(authenticatedUser instanceof Trainee trainee)) {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }

        return new ResponseEntity<>(DTOFactory.createTraineeSendDTO(trainee), HttpStatus.OK);
    }

    // TODO: Put methods validation not working
    @PutMapping("/profile")
    @Validate
    public ResponseEntity<TraineeSendDTO> getTraineeProfile(@RequestBody TraineeUpdateDTO traineeUpdateDTO,
                                                            HttpServletRequest request,
                                                            BindingResult bindingResult /* used for aspect */) {
        User authenticatedUser = (User) request.getAttribute("authenticatedUser");

        if (!(authenticatedUser instanceof Trainee trainee)) {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }

        trainee = traineeService.update(trainee, traineeUpdateDTO);

        return new ResponseEntity<>(DTOFactory.createTraineeSendDTO(trainee), HttpStatus.OK);
    }

    @DeleteMapping("/profile")
    @Validate
    public ResponseEntity<String> deleteTrainee(@RequestBody UsernameGetDTO usernameGetDTO,
                                                HttpServletRequest request,
                                                BindingResult bindingResult /* user for aspect */) {
        User authenticatedUser = (User) request.getAttribute("authenticatedUser");

        if (!(authenticatedUser instanceof Trainee) ||
                !authenticatedUser.getUserName().equals(usernameGetDTO.username())) {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }

        traineeService.deleteById(authenticatedUser.getId());

        return ResponseEntity.ok(null);
    }
}
