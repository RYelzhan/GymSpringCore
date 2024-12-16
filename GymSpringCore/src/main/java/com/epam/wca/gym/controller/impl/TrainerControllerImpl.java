package com.epam.wca.gym.controller.impl;

import com.epam.wca.common.gymcommon.aop.Logging;
import com.epam.wca.gym.aop.argument.InsertUser;
import com.epam.wca.gym.aop.argument.InsertUserId;
import com.epam.wca.gym.controller.documentation.TrainerControllerDocumentation;
import com.epam.wca.gym.dto.trainer.TrainerSendDTO;
import com.epam.wca.gym.dto.trainer.TrainerTrainingCreateDTO;
import com.epam.wca.gym.dto.trainer.TrainerUpdateDTO;
import com.epam.wca.gym.dto.training.TrainerTrainingQuery;
import com.epam.wca.gym.dto.training.TrainingBasicDTO;
import com.epam.wca.gym.entity.Trainer;
import com.epam.wca.gym.service.TrainerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class TrainerControllerImpl implements TrainerControllerDocumentation {
    private final TrainerService trainerService;

    @Override
    @Logging
    public TrainerSendDTO getProfile(
            @InsertUser Trainer authenticatedTrainer
    ) {
        return trainerService.getProfile(authenticatedTrainer);
    }

    @Override
    @Logging
    public TrainerSendDTO updateProfile(
            @RequestBody @Valid TrainerUpdateDTO trainerUpdateDTO,
            @InsertUser Trainer authenticatedTrainer
    ) {
        return trainerService.update(authenticatedTrainer, trainerUpdateDTO);
    }

    /**
     * @deprecated refactored Trainee deletion logic to be accessible through message receiver
     * @param id of Trainer getting deleted
     */
    @Deprecated(since = "2.3")
    @Override
    @Logging
    public void deleteProfile(
            @InsertUserId Long id
    ) {
        // TODO: invalidation refresh token logic

        trainerService.deleteById(id);
    }

    //TODO: transfer all input to RequestParam
    @Override
    @Logging
    public List<TrainingBasicDTO> getTrainings(
            @RequestBody @Valid TrainerTrainingQuery trainerTrainingQuery,
            @InsertUser Trainer authenticatedTrainer
    ) {
        return trainerService.findTrainingsFiltered(authenticatedTrainer.getId(), trainerTrainingQuery);
    }

    @Override
    @Logging
    public String createTraining(
            @RequestBody @Valid TrainerTrainingCreateDTO trainingDTO,
            @InsertUser Trainer authenticatedTrainer
    ) {
        trainerService.createTraining(authenticatedTrainer, trainingDTO);

        return "Training Creation Started Successfully";
    }
}
