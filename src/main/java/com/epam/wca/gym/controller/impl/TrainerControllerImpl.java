package com.epam.wca.gym.controller.impl;

import com.epam.wca.gym.aop.check.CheckTrainer;
import com.epam.wca.gym.controller.TrainerController;
import com.epam.wca.gym.dto.trainer.TrainerSendDTO;
import com.epam.wca.gym.dto.trainer.TrainerTrainingCreateDTO;
import com.epam.wca.gym.dto.trainer.TrainerUpdateDTO;
import com.epam.wca.gym.dto.training.TrainerTrainingQuery;
import com.epam.wca.gym.dto.training.TrainingBasicDTO;
import com.epam.wca.gym.entity.Trainer;
import com.epam.wca.gym.service.TrainerService;
import com.epam.wca.gym.util.DTOFactory;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class TrainerControllerImpl implements TrainerController {
    private final TrainerService trainerService;

    @Value("${gym.api.request.attribute.user}")
    private String authenticatedUserRequestAttributeName;

    @Override
    @CheckTrainer
    public TrainerSendDTO getTrainerProfile(HttpServletRequest request) {
        var authenticatedTrainer = (Trainer) request.getAttribute(authenticatedUserRequestAttributeName);

        return DTOFactory.createTrainerSendDTO(authenticatedTrainer);
    }

    @Override
    @CheckTrainer
    public TrainerSendDTO updateTrainerProfile(
            @RequestBody @Valid TrainerUpdateDTO trainerUpdateDTO,
            HttpServletRequest request
    ) {
        var authenticatedTrainer = (Trainer) request.getAttribute(authenticatedUserRequestAttributeName);

        return trainerService.update(authenticatedTrainer, trainerUpdateDTO);
    }

    @Override
    @CheckTrainer
    public void deleteTrainer(HttpServletRequest request) {
        var authenticatedTrainer = (Trainer) request.getAttribute(authenticatedUserRequestAttributeName);

        // TODO: invalidation refresh token logic

        trainerService.deleteById(authenticatedTrainer.getId());
    }

    //TODO: transfer all input to RequestParam
    @Override
    @CheckTrainer
    public List<TrainingBasicDTO> getTrainerTrainingsList(
            @RequestBody @Valid TrainerTrainingQuery trainerTrainingQuery,
            HttpServletRequest request
    ) {
        var authenticatedTrainer = (Trainer) request.getAttribute(authenticatedUserRequestAttributeName);

        return trainerService.findTrainingsFiltered(authenticatedTrainer.getId(), trainerTrainingQuery);
    }

    @Override
    @CheckTrainer
    public String createTrainerTraining(
            @RequestBody @Valid TrainerTrainingCreateDTO trainingDTO,
            HttpServletRequest request
    ) {
        var authenticatedTrainer = (Trainer) request.getAttribute(authenticatedUserRequestAttributeName);

        trainerService.createTraining(authenticatedTrainer, trainingDTO);

        return "Training Created Successfully";
    }
}
