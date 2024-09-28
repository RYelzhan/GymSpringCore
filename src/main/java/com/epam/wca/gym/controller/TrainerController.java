package com.epam.wca.gym.controller;

import com.epam.wca.gym.aop.Validate;
import com.epam.wca.gym.dto.TrainerSendDTO;
import com.epam.wca.gym.dto.UsernameGetDTO;
import com.epam.wca.gym.entity.Trainer;
import com.epam.wca.gym.entity.User;
import com.epam.wca.gym.service.impl.TrainerService;
import com.epam.wca.gym.util.DTOFactory;
import jakarta.servlet.http.HttpServletRequest;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "user/trainer", consumes = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class TrainerController {
    @NonNull
    private TrainerService trainerService;

    @GetMapping("/profile")
    @Validate
    public ResponseEntity<TrainerSendDTO> getTrainerProfile(@RequestBody UsernameGetDTO usernameGetDTO,
                                                            HttpServletRequest request,
                                                            BindingResult bindingResult /* used for aspect */) {
        User authenticatedUser = (User) request.getAttribute("authenticatedUser");

        if (!authenticatedUser.getUserName().equals(usernameGetDTO.username()) ||
                !(authenticatedUser instanceof Trainer trainer)) {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }

        return new ResponseEntity<>(DTOFactory.createTrainerSendDTO(trainer), HttpStatus.OK);
    }


}
