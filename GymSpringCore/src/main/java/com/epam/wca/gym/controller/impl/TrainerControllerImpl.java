package com.epam.wca.gym.controller.impl;

import com.epam.wca.gym.controller.documentation.TrainerControllerDocumentation;
import com.epam.wca.common.gymcommon.statistics_dto.TrainerWorkloadSummary;
import com.epam.wca.gym.dto.trainer.TrainerSendDTO;
import com.epam.wca.gym.dto.trainer.TrainerTrainingCreateDTO;
import com.epam.wca.gym.dto.trainer.TrainerUpdateDTO;
import com.epam.wca.gym.dto.training.TrainerTrainingQuery;
import com.epam.wca.gym.dto.training.TrainingBasicDTO;
import com.epam.wca.gym.entity.Trainer;
import com.epam.wca.gym.feign.StatisticsClient;
import com.epam.wca.gym.service.TrainerService;
import com.epam.wca.gym.util.DTOFactory;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class TrainerControllerImpl implements TrainerControllerDocumentation {
    private final TrainerService trainerService;
    private final StatisticsClient statisticsClient;

    @Override
    public TrainerSendDTO getProfile(
            @AuthenticationPrincipal Trainer authenticatedTrainer
    ) {
        return DTOFactory.createTrainerSendDTO(authenticatedTrainer);
    }

    @Override
    public TrainerSendDTO updateProfile(
            @RequestBody @Valid TrainerUpdateDTO trainerUpdateDTO,
            @AuthenticationPrincipal Trainer authenticatedTrainer
    ) {
        return trainerService.update(authenticatedTrainer, trainerUpdateDTO);
    }

    @Override
    public void deleteProfile(
            @AuthenticationPrincipal Trainer authenticatedTrainer
    ) {
        // TODO: invalidation refresh token logic

        trainerService.deleteById(authenticatedTrainer.getId());
    }

    //TODO: transfer all input to RequestParam
    @Override
    public List<TrainingBasicDTO> getTrainings(
            @RequestBody @Valid TrainerTrainingQuery trainerTrainingQuery,
            @AuthenticationPrincipal Trainer authenticatedTrainer
    ) {
        return trainerService.findTrainingsFiltered(authenticatedTrainer.getId(), trainerTrainingQuery);
    }

    @Override
    public String createTraining(
            @RequestBody @Valid TrainerTrainingCreateDTO trainingDTO,
            @AuthenticationPrincipal Trainer authenticatedTrainer
    ) {
        trainerService.createTraining(authenticatedTrainer, trainingDTO);

        return "Training Created Successfully";
    }

    @Override
    public TrainerWorkloadSummary getStatistics(
            @AuthenticationPrincipal Trainer trainer
    ) {
        System.out.println(trainer.getUsername());
        return statisticsClient.getWorkload(trainer.getUsername());
    }
}
